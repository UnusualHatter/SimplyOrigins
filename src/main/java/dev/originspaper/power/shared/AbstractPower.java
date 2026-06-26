package dev.originspaper.power.shared;

import dev.originspaper.OriginsPaper;
import dev.originspaper.api.PowerType;

/** Convenience base storing the power id and exposing the plugin singleton. */
public abstract class AbstractPower implements PowerType {

    protected final String id;

    protected AbstractPower(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    protected OriginsPaper plugin() {
        return OriginsPaper.instance();
    }

    @Override
    public void onApply(org.bukkit.entity.Player player) {}

    @Override
    public void onRemove(org.bukkit.entity.Player player) {}

    /**
     * True when the player's main hand holds any tier of spear — the vanilla 26.x charge weapon.
     * Used to cap coelho/cabra dash hits (RabbitDashPower / LeapPower / RamPower).
     */
    protected static boolean isHoldingSpear(org.bukkit.entity.Player player) {
        return player.getInventory().getItemInMainHand().getType().name().endsWith("_SPEAR");
    }
}
