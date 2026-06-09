package dev.originspaper.power.origins.bear;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

/** Devastating bare-handed strikes with extra knockback. */
public class MightyPawsPower extends AbstractPower {

    public MightyPawsPower(String id) {
        super(id);
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            return;
        }
        e.setDamage(e.getDamage() + 3.0);
        Vector dir = e.getEntity().getLocation().toVector()
                .subtract(player.getLocation().toVector());
        if (dir.lengthSquared() < 1.0e-4) {
            dir = player.getLocation().getDirection();
        }
        e.getEntity().setVelocity(dir.normalize().multiply(1.4).setY(0.5));

        // Crushing blow: crits plus a heavy crunch tinted by the ground under the target.
        Location loc = e.getEntity().getLocation();
        ParticleUtil.spawnTrail(Particle.CRIT, loc.clone().add(0, 1.0, 0), 8, 0.4);
        Object below = loc.clone().subtract(0, 0.2, 0).getBlock().getBlockData();
        ParticleUtil.spawnGroundBurst(Particle.BLOCK, loc, 0.4, 6, 0.0, below);
    }
}
