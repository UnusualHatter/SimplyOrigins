package dev.originspaper.power.origins.rat;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.concurrent.ThreadLocalRandom;

/** 30% chance to dodge projectiles, leaving a quick puff; light skittering dust while moving. */
public class EvasionPower extends AbstractPower {

    public EvasionPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        // Tiny ground-tinted skitter dust while scurrying about.
        if (plugin().tick() % 2 == 0
                && (player.isSprinting() || player.getVelocity().lengthSquared() > 0.01)) {
            Block below = player.getLocation().clone().subtract(0, 0.1, 0).getBlock();
            ParticleUtil.spawnTrail(Particle.BLOCK, player.getLocation().add(0, 0.05, 0), 2, 0.15,
                    below.getBlockData());
        }
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player
                && e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (ThreadLocalRandom.current().nextDouble() < 0.3) {
                e.setCancelled(true);
                Location loc = player.getLocation().add(0, 1.0, 0);
                ParticleUtil.spawnGroundBurst(Particle.CLOUD, loc, 0.4, 6, 0.05);
                ParticleUtil.spawnGroundBurst(Particle.POOF, loc, 0.3, 3, 0.02);
            }
        }
    }
}
