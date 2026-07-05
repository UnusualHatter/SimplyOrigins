package dev.originspaper.power.origins.raccoon;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ArmorUtil;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 * While sneaking, hostile mobs only notice the raccoon up close — meaningful stealth, not
 * invisibility (a mob that already has aggro keeps it; this only blocks new acquisitions from
 * afar). Heavy chestplates rattle too much and disable it.
 */
public class SneakyPawsPower extends AbstractPower {

    /** Squared detection radius (blocks) while sneaking. */
    private static final double SNEAK_RANGE_SQUARED = 8.0 * 8.0;

    public SneakyPawsPower(String id) {
        super(id);
    }

    @Override
    public void onEntityTarget(EntityTargetEvent e) {
        if (!(e.getTarget() instanceof Player raccoon) || !raccoon.isSneaking()
                || !(e.getEntity() instanceof Monster)) {
            return;
        }
        if (ArmorUtil.isWearingHeavyChest(raccoon)) {
            return;
        }
        if (e.getEntity().getLocation().distanceSquared(raccoon.getLocation()) > SNEAK_RANGE_SQUARED) {
            e.setCancelled(true);
        }
    }
}
