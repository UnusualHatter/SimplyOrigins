package dev.originspaper.power.shared;

import dev.originspaper.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

/**
 * Shared helper for armor-restriction powers. {@code PlayerArmorChangeEvent} is not cancellable,
 * so we let the equip happen and revert it one tick later, returning the piece to the inventory.
 */
abstract class ArmorRevertSupport extends AbstractPower {

    protected ArmorRevertSupport(String id) {
        super(id);
    }

    protected void revert(Player player, EquipmentSlot slot, ItemStack piece, String message) {
        UUID playerId = player.getUniqueId();
        ItemStack expected = piece.clone();
        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            Player resolved = resolveActivePlayer(playerId);
            if (resolved == null) {
                return;
            }
            ItemStack current = resolved.getInventory().getItem(slot);
            if (current == null || current.getType().isAir()) {
                return;
            }
            // Only revert if the offending piece is still equipped. Another power (e.g. an
            // ElytraFlightPower forcing wings back into the chest) may have already replaced it
            // this same tick — in that case leave the new item alone instead of stripping it.
            if (!current.isSimilar(expected)) {
                return;
            }
            resolved.getInventory().setItem(slot, null);
            Map<Integer, ItemStack> leftover = resolved.getInventory().addItem(current);
            for (ItemStack drop : leftover.values()) {
                resolved.getWorld().dropItemNaturally(resolved.getLocation(), drop);
            }
            if (message != null) {
                resolved.sendActionBar(TextUtil.msg(message));
            }
        }, 1L);
    }
}
