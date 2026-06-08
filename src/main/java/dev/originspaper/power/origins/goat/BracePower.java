package dev.originspaper.power.origins.goat;

import dev.originspaper.power.shared.AbstractPower;
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
        }
    }
}
