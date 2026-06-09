package dev.originspaper.power.shared;

import dev.originspaper.util.ParticleUtil;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityTargetEvent;

/** Prevents a given mob type from targeting the player (e.g. creepers fear felines). */
public class ScareEntityPower extends AbstractPower {

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
}
