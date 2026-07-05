package dev.originspaper.power.origins.skunk;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ArmorUtil;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 * Sneaking advertises that approaching is a bad idea: hostile mobs cannot newly acquire the
 * skunk from long range. Existing aggression is preserved, so this is caution rather than
 * invisibility.
 */
public class WarningStripesPower extends AbstractPower {

    private static final double WARNING_RANGE_SQUARED = 9.0 * 9.0;

    public WarningStripesPower(String id) {
        super(id);
    }

    @Override
    public void onEntityTarget(EntityTargetEvent e) {
        if (!(e.getTarget() instanceof Player skunk) || !(e.getEntity() instanceof Monster)
                || !skunk.isSneaking() || ArmorUtil.isWearingHeavyChest(skunk)) {
            return;
        }
        if (e.getEntity().getLocation().distanceSquared(skunk.getLocation()) > WARNING_RANGE_SQUARED) {
            e.setCancelled(true);
        }
    }
}
