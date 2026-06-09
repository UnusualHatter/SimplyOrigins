package dev.originspaper.power.origins.goat;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

/** Heavy knockback on melee hits. */
public class RamPower extends AbstractPower {

    public RamPower(String id) {
        super(id);
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        if (!player.isSprinting()) {
            return;
        }
        // Bonus damage from the headbutt impact.
        e.setDamage(e.getDamage() + 4.0);

        Location loc = e.getEntity().getLocation().add(0, 0.5, 0);
        ParticleUtil.spawnGroundBurst(Particle.POOF, loc, 0.4, 5, 0.05);
        ParticleUtil.spawnGroundBurst(Particle.CLOUD, loc, 0.4, 6, 0.05);

        Vector dir = e.getEntity().getLocation().toVector()
                .subtract(player.getLocation().toVector());
        if (dir.lengthSquared() < 1.0e-4) {
            dir = player.getLocation().getDirection();
        }
        final Vector knockback = dir.normalize().multiply(2.5).setY(0.8);
        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            if (e.getEntity().isValid()) {
                e.getEntity().setVelocity(knockback);
            }
        }, 1L);
    }
}
