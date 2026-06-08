package dev.originspaper.power.shared;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import dev.originspaper.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

/** Grants a permanent, protected Elytra in the chest slot that re-equips itself if removed. */
public class ElytraFlightPower extends AbstractPower {

    private final NamespacedKey markerKey;
    private final String displayName;

    public ElytraFlightPower(String id, String markerName, String displayName) {
        super(id);
        this.markerKey = new NamespacedKey(plugin(), markerName);
        this.displayName = displayName;
    }

    private ItemStack makeWings() {
        ItemStack wings = new ItemStack(Material.ELYTRA);
        wings.editMeta(meta -> {
            meta.getPersistentDataContainer().set(markerKey, PersistentDataType.BYTE, (byte) 1);
            meta.setUnbreakable(true);
            meta.displayName(TextUtil.item("§d" + displayName));
        });
        return wings;
    }

    private boolean isWings(ItemStack item) {
        return item != null
                && item.getType() == Material.ELYTRA
                && item.hasItemMeta()
                && item.getItemMeta().getPersistentDataContainer()
                .has(markerKey, PersistentDataType.BYTE);
    }

    private void equip(Player player) {
        PlayerInventory inv = player.getInventory();
        // Remove any stray copies of the wings that may already be in the inventory.
        for (int i = 0; i < inv.getSize(); i++) {
            if (isWings(inv.getItem(i))) {
                inv.setItem(i, null);
            }
        }
        // Return the current chestplate to the player before replacing it with wings.
        ItemStack existing = inv.getChestplate();
        if (existing != null && !existing.getType().isAir() && !isWings(existing)) {
            Map<Integer, ItemStack> leftover = inv.addItem(existing);
            for (ItemStack drop : leftover.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
        }
        inv.setChestplate(makeWings());
    }

    @Override
    public void onApply(Player player) {
        if (!isWings(player.getInventory().getChestplate())) {
            equip(player);
        }
    }

    @Override
    public void onArmorChange(PlayerArmorChangeEvent e) {
        if (e.getSlot() != EquipmentSlot.CHEST) {
            return;
        }
        if (!isWings(e.getNewItem())) {
            plugin().getServer().getScheduler().runTaskLater(plugin(), () -> equip(e.getPlayer()), 1L);
        }
    }

    @Override
    public void onRemove(Player player) {
        PlayerInventory inv = player.getInventory();
        if (isWings(inv.getChestplate())) {
            inv.setChestplate(null);
        }
    }
}
