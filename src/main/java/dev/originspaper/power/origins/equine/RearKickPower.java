package dev.originspaper.power.origins.equine;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.PerfStats;
import dev.originspaper.util.SafeTargetUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

/** Active skill: a low-damage, high-knockback cone behind the player. */
public class RearKickPower extends AbstractPower implements ActivePowerType {

    private static final double CONE_DOT = 0.35;

    private final long cooldownTicks;
    private final double radius;
    private final double damage;

    public RearKickPower(String id, long cooldownTicks, double radius, double damage) {
        super(id);
        this.cooldownTicks = cooldownTicks;
        this.radius = radius;
        this.damage = damage;
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onActivate(Player player) {
        Location center = player.getLocation();
        Vector backward = center.getDirection().setY(0);
        if (backward.lengthSquared() < 1.0e-6) {
            backward = new Vector(0, 0, 1);
        }
        backward.normalize().multiply(-1);

        boolean hit = false;
        PerfStats.entityScan();
        for (Entity entity : player.getWorld().getNearbyEntities(center, radius, radius, radius)) {
            if (!(entity instanceof LivingEntity target) || entity.equals(player)
                    || SafeTargetUtil.isProtected(target) || !claims().canDamage(player, target)) {
                continue;
            }
            Vector offset = target.getLocation().toVector().subtract(center.toVector()).setY(0);
            if (offset.lengthSquared() < 1.0e-6 || offset.normalize().dot(backward) < CONE_DOT) {
                continue;
            }
            target.damage(damage, player);
            knockBackLater(target.getUniqueId(), backward);
            hit = true;
        }

        player.getWorld().playSound(center, Sound.ENTITY_HORSE_ANGRY, 0.9f, 0.9f);
        Location burst = center.clone().add(backward.clone().multiply(1.0)).add(0, 0.7, 0);
        ParticleUtil.spawnGroundBurst(Particle.POOF, burst, 0.8, 14, 0.05);
        ParticleUtil.spawnTrail(Particle.CRIT, burst, 8, 0.55);
        if (hit) {
            plugin().progression().awardXp(player, 8);
        }
    }

    private void knockBackLater(UUID targetId, Vector direction) {
        Vector knockback = direction.clone().multiply(1.45).setY(0.45);
        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            Entity resolved = plugin().getServer().getEntity(targetId);
            if (resolved instanceof LivingEntity living && living.isValid()) {
                living.setVelocity(knockback);
            }
        }, 1L);
    }

    @Override
    public long getCooldownTicks(Player player) {
        return levelOf(player) >= 6 ? (long) (cooldownTicks * 0.7) : cooldownTicks;
    }
}
