package dev.originspaper.power.origins.rabbit;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Prey instinct: senses nearby predators (wolves, foxes, cats/ocelots) and marks them with a clear
 * danger flag so the rabbit gets a moment to flee. Pure awareness — no combat.
 */
public class PredatorScentPower extends AbstractPower {

    private static final double RANGE = 16.0;

    public PredatorScentPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        boolean sensed = false;
        for (LivingEntity entity : player.getWorld()
                .getNearbyEntitiesByType(LivingEntity.class, player.getLocation(), RANGE)) {
            if (!isPredator(entity)) {
                continue;
            }
            sensed = true;
            Location head = entity.getLocation().add(0, entity.getHeight() + 0.4, 0);
            ParticleUtil.spawn(Particle.ANGRY_VILLAGER, head, 2, 0.15, 0.1, 0.15, 0.0);
        }
        if (sensed) {
            // A nervous twitch of scent above the rabbit while a predator is in range.
            ParticleUtil.spawnTrail(Particle.WARPED_SPORE, player.getLocation().add(0, 1.0, 0), 3, 0.3);
        }
    }

    private boolean isPredator(LivingEntity entity) {
        EntityType type = entity.getType();
        if (type == EntityType.WOLF || type == EntityType.FOX
                || type == EntityType.CAT || type == EntityType.OCELOT) {
            return true;
        }
        if (entity instanceof Player p) {
            dev.originspaper.registry.PlayerOriginData data = plugin().data().get(p.getUniqueId());
            if (data != null && data.hasOrigin()) {
                String id = data.getOrigin().id();
                return id.equals("wolf") || id.equals("fox") || id.equals("feline");
            }
        }
        return false;
    }
}
