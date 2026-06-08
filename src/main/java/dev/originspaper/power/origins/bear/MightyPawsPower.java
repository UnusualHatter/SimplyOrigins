package dev.originspaper.power.origins.bear;

import dev.originspaper.power.shared.AbstractPower;
import org.bukkit.Material;
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
    }
}
