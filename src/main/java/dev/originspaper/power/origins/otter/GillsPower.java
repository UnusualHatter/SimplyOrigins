package dev.originspaper.power.origins.otter;

import dev.originspaper.power.shared.AbstractPower;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Breathes underwater but drowns on land: air is refilled while submerged and drains while out
 * of water, dealing damage once it is depleted.
 */
public class GillsPower extends AbstractPower {

    public GillsPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (player.isInWater()) {
            player.setRemainingAir(player.getMaximumAir());
        } else {
            int air = player.getRemainingAir();
            if (air > 0) {
                player.setRemainingAir(Math.max(0, air - 60));
            } else {
                player.damage(1.0);
            }
        }
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.DROWNING
                && e.getEntity() instanceof Player p && p.isInWater()) {
            e.setCancelled(true);
        }
    }
}
