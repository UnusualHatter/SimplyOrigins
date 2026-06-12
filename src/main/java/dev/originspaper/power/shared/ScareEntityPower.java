package dev.originspaper.power.shared;

import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.util.Vector;

/**
 * Prevents a given mob type from targeting the player and makes nearby ones flee on sight,
 * like a creeper bolting from a cat.
 */
public class ScareEntityPower extends AbstractPower {

    private static final double SCARE_RADIUS = 6.0;
    private static final double FLEE_DISTANCE = 8.0;
    private static final double FLEE_SPEED = 1.2;

    private final EntityType type;

    public ScareEntityPower(String id, EntityType type) {
        super(id);
        this.type = type;
    }

    @Override
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getEntityType() == type) {
            e.setCancelled(true);
            // The frightened mob recoils in a puff; the predator flashes a brief threat.
            ParticleUtil.spawnTrail(Particle.SMOKE, e.getEntity().getLocation().add(0, 0.5, 0), 5, 0.3);
            if (e.getTarget() instanceof Player player) {
                ParticleUtil.spawnTrail(Particle.CRIT, player.getLocation().add(0, 1.0, 0), 4, 0.3);
            }
        }
    }

    @Override
    public void onTick(Player player) {
        for (LivingEntity entity : player.getWorld().getNearbyEntitiesByType(LivingEntity.class, player.getLocation(), SCARE_RADIUS)) {
            if (entity.getType() != type || !(entity instanceof Mob mob)) {
                continue;
            }
            fleeFrom(mob, player.getLocation());
        }
    }

    /** Sends {@code mob} running away from {@code from}, like a cat-scared creeper. */
    static void fleeFrom(Mob mob, Location from) {
        Vector away = mob.getLocation().toVector().subtract(from.toVector());
        away.setY(0);
        if (away.lengthSquared() < 1.0E-4) {
            away = new Vector(1, 0, 0);
        }
        away.normalize().multiply(FLEE_DISTANCE);
        Location fleeTo = mob.getLocation().add(away);
        mob.getPathfinder().moveTo(fleeTo, FLEE_SPEED);
    }
}
