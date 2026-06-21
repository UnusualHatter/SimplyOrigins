package dev.originspaper.power.shared;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Cancels fall damage for falls up to {@code maxSafeBlocks} blocks; larger falls still hurt
 * normally. Unlike {@link NoFallDamagePower}, this only forgives short falls.
 */
public class FallDamageThresholdPower extends AbstractPower {

    private final double maxSafeBlocks;

    public FallDamageThresholdPower(String id, double maxSafeBlocks) {
        super(id);
        this.maxSafeBlocks = maxSafeBlocks;
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL || !(e.getEntity() instanceof Player player)) {
            return;
        }
        if (player.getFallDistance() <= maxSafeBlocks) {
            e.setCancelled(true);
        }
    }
}
