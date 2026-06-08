package dev.originspaper.power.origins.owl;

import dev.originspaper.power.shared.AbstractPower;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 * Reduces how far mobs can detect the owl while it is gliding. The player's own follow-range
 * attribute has no effect on being detected, so we instead reject target acquisitions that
 * originate from beyond a short range — a gliding owl is only noticed by very close mobs.
 */
public class SilentFlightPower extends AbstractPower {

    /** Squared detection radius (blocks) while gliding. Beyond this, mobs lose/ignore the owl. */
    private static final double SILENT_RANGE_SQUARED = 7.0 * 7.0;

    public SilentFlightPower(String id) {
        super(id);
    }

    @Override
    public void onEntityTarget(EntityTargetEvent e) {
        if (!(e.getTarget() instanceof Player owl) || !owl.isGliding()) {
            return;
        }
        if (e.getEntity().getLocation().distanceSquared(owl.getLocation()) > SILENT_RANGE_SQUARED) {
            e.setCancelled(true);
        }
    }
}
