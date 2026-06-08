package dev.originspaper.power.shared;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

/** Disallows equipping items matching a predicate in a specific armor slot. */
public class ArmorSlotRestrictPower extends ArmorRevertSupport {

    private final EquipmentSlot slot;
    private final Predicate<ItemStack> denied;
    private final String message;

    public ArmorSlotRestrictPower(String id, EquipmentSlot slot, Predicate<ItemStack> denied, String message) {
        super(id);
        this.slot = slot;
        this.denied = denied;
        this.message = message;
    }

    @Override
    public void onArmorChange(PlayerArmorChangeEvent e) {
        if (e.getSlot() != slot) {
            return;
        }
        ItemStack item = e.getNewItem();
        if (item != null && !item.getType().isAir() && denied.test(item)) {
            revert(e.getPlayer(), slot, item, message);
        }
    }
}
