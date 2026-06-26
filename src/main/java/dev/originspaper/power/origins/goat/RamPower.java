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

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        if (!player.isSprinting()) {
            return;
        }
        // Nv6 "Impacto": a heavier charge — more damage and a stronger shove.
        boolean impact = levelOf(player) >= 6;
        e.setDamage(e.getDamage() + (impact ? 6.0 : 4.0));
        // Cabra balance: the Investida bonus must not push a spear hit past 8 (4 hearts).
        if (isHoldingSpear(player) && e.getDamage() > 8.0) {
            e.setDamage(8.0);
        }

        Location loc = e.getEntity().getLocation().add(0, 0.5, 0);
        ParticleUtil.spawnGroundBurst(Particle.POOF, loc, 0.4, 5, 0.05);
        ParticleUtil.spawnGroundBurst(Particle.CLOUD, loc, 0.4, 6, 0.05);

        Vector dir = e.getEntity().getLocation().toVector()
                .subtract(player.getLocation().toVector());
        if (dir.lengthSquared() < 1.0e-4) {
            dir = player.getLocation().getDirection();
        }
        final Vector knockback = dir.normalize().multiply(impact ? 3.5 : 2.5).setY(0.8);
        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            if (e.getEntity().isValid()) {
                e.getEntity().setVelocity(knockback);
            }
        }, 1L);
    }
}
