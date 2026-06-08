package dev.originspaper.power.origins.dragon;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Active skill: exhales a short frontal cone of fire. Deals modest direct damage to everything
 * caught in the cone and sets it ablaze, so the lingering burn does most of the work. Kept on the
 * utility/crowd-control side with a long cooldown — the dragon already runs hot on raw stats.
 */
public class DragonBreathPower extends AbstractPower implements ActivePowerType {

    private static final double RANGE = 5.0;          // blocks the breath reaches
    private static final double HALF_ANGLE_COS = 0.766; // cos(40°): ~80° total cone
    private static final double DIRECT_DAMAGE = 4.0;  // 2 hearts up front
    private static final int FIRE_TICKS = 100;        // 5s of burning afterwards
    private static final long COOLDOWN_TICKS = 1000L; // 50s

    public DragonBreathPower(String id) {
        super(id);
    }

    @Override
    public void onActivate(Player player) {
        Location eye = player.getEyeLocation();
        Vector dir = eye.getDirection().normalize();
        World world = player.getWorld();

        world.playSound(eye, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.2f);
        spawnConeParticles(world, eye, dir);

        int hits = 0;
        for (Entity entity : player.getNearbyEntities(RANGE, RANGE, RANGE)) {
            if (!(entity instanceof LivingEntity target) || entity.equals(player)
                    || entity instanceof ArmorStand) {
                continue;
            }
            // Aim at the target's torso, then keep only what falls inside the forward cone.
            Vector toTarget = target.getLocation().add(0, target.getHeight() * 0.5, 0)
                    .toVector().subtract(eye.toVector());
            double distance = toTarget.length();
            if (distance < 0.001 || distance > RANGE) {
                continue;
            }
            if (toTarget.normalize().dot(dir) < HALF_ANGLE_COS) {
                continue;
            }
            if (!player.hasLineOfSight(target)) {
                continue; // fire doesn't curve around walls
            }
            target.damage(DIRECT_DAMAGE, player); // attribute the hit so kills credit the dragon
            target.setFireTicks(FIRE_TICKS);
            hits++;
        }

        if (hits > 0) {
            player.sendActionBar(TextUtil.msg("§6Sopro de dragão atingiu §f" + hits + "§6 alvo(s)!"));
        }
    }

    private void spawnConeParticles(World world, Location eye, Vector dir) {
        for (double d = 1.0; d <= RANGE; d += 0.5) {
            Location point = eye.clone().add(dir.clone().multiply(d));
            double spread = 0.12 * d; // cone widens with distance
            world.spawnParticle(Particle.FLAME, point, 6, spread, spread, spread, 0.01);
            world.spawnParticle(Particle.DRAGON_BREATH, point, 4, spread, spread, spread, 0.0);
        }
    }

    @Override
    public long getCooldownTicks() {
        return COOLDOWN_TICKS;
    }
}
