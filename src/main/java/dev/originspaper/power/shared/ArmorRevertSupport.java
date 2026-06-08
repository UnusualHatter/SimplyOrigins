package dev.originspaper.power.shared;

import dev.originspaper.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Shared helper for armor-restriction powers. {@code PlayerArmorChangeEvent} is not cancellable,
 * so we let the equip happen and revert it one tick later, returning the piece to the inventory.
 */
abstract class ArmorRevertSupport extends AbstractPower {

    protected ArmorRevertSupport(String id) {
        super(id);
    }

    protected void revert(Player player, EquipmentSlot slot, ItemStack piece, String message) {
        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            ItemStack current = player.getInventory().getItem(slot);
            if (current == null || current.getType().isAir()) {
                return;
            }
            player.getInventory().setItem(slot, null);
            Map<Integer, ItemStack> leftover = player.getInventory().addItem(current);
            for (ItemStack drop : leftover.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
            if (message != null) {
                player.sendActionBar(TextUtil.msg(message));
            }
        }, 1L);
    }
}
