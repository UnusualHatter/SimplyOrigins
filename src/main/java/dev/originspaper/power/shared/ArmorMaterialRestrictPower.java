package dev.originspaper.power.shared;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

/**
 * Disallows equipping armor matching a predicate in <b>any</b> slot — e.g. banning all
 * diamond/netherite pieces, which a per-defense-point threshold can't cleanly separate from iron.
 */
public class ArmorMaterialRestrictPower extends ArmorRevertSupport {

    private final Predicate<ItemStack> denied;
    private final String message;

    public ArmorMaterialRestrictPower(String id, Predicate<ItemStack> denied, String message) {
        super(id);
        this.denied = denied;
        this.message = message;
    }

    @Override
    public void onArmorChange(PlayerArmorChangeEvent e) {
        ItemStack item = e.getNewItem();
        if (item != null && !item.getType().isAir() && denied.test(item)) {
            revert(e.getPlayer(), e.getSlot(), item, message);
        }
    }
}
