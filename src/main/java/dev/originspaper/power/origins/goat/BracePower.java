package dev.originspaper.power.origins.goat;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

/** Bracing (sneaking) negates fall damage. */
public class BracePower extends AbstractPower {

    public BracePower(String id) {
        super(id);
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL
                && e.getEntity() instanceof Player player && player.isSneaking()) {
            e.setCancelled(true);
            // Dug-in landing: ground crunch + a plume of kicked-up dust.
            Location loc = player.getLocation();
            Object below = loc.clone().subtract(0, 0.2, 0).getBlock().getBlockData();
            ParticleUtil.spawnGroundBurst(Particle.BLOCK, loc, 0.5, 8, 0.0, below);
            ParticleUtil.spawnGroundBurst(Particle.DUST_PLUME, loc, 0.4, 5, 0.05);
        }
    }
}
