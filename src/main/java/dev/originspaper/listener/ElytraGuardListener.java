package dev.originspaper.listener;

import dev.originspaper.OriginsPaper;
import dev.originspaper.api.PowerType;
import dev.originspaper.power.shared.ElytraFlightPower;
import dev.originspaper.registry.PlayerOriginData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Anti-duplication guard for the origins' permanent wings. The wings live only in the chest slot
 * and re-equip themselves; this listener makes sure a player can never pull a second copy out of
 * that slot (cursor, drop, hotbar swap, death drop), which was the duplication vector.
 */
public class ElytraGuardListener implements Listener {

    private final OriginsPaper plugin;

    public ElytraGuardListener(OriginsPaper plugin) {
        this.plugin = plugin;
    }

    /** Block any click that would move the wings out of where they belong. */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (ElytraFlightPower.isProtectedWings(e.getCurrentItem())
                || ElytraFlightPower.isProtectedWings(e.getCursor())) {
            e.setCancelled(true);
            return;
        }
        if (e.getClick() == ClickType.NUMBER_KEY
                && ElytraFlightPower.isProtectedWings(e.getWhoClicked().getInventory().getItem(e.getHotbarButton()))) {
            e.setCancelled(true);
            return;
        }
        if (e.getClick() == ClickType.SWAP_OFFHAND
                && ElytraFlightPower.isProtectedWings(e.getWhoClicked().getInventory().getItemInOffHand())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (ElytraFlightPower.isProtectedWings(e.getOldCursor())) {
            e.setCancelled(true);
        }
    }

    /** The wings can never become a free-floating item. */
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (ElytraFlightPower.isProtectedWings(e.getItemDrop().getItemStack())) {
            e.setCancelled(true);
        }
    }

    /** Don't drop the wings on death — they get re-equipped on respawn instead. */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.getDrops().removeIf(ElytraFlightPower::isProtectedWings);
    }

    @EventHandler
    public void onArmorStand(org.bukkit.event.player.PlayerArmorStandManipulateEvent e) {
        if (ElytraFlightPower.isProtectedWings(e.getPlayerItem())
                || ElytraFlightPower.isProtectedWings(e.getArmorStandItem())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDispenseArmor(org.bukkit.event.block.BlockDispenseArmorEvent e) {
        if (e.getTargetEntity() instanceof Player player) {
            if (ElytraFlightPower.isProtectedWings(player.getInventory().getChestplate())) {
                e.setCancelled(true);
            }
        }
    }

    /** Re-equip the wings after respawn (the only path that bypasses onArmorChange). */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }
            PlayerOriginData data = plugin.data().get(player.getUniqueId());
            if (data == null) {
                return;
            }
            for (PowerType power : data.getPowers()) {
                if (power instanceof ElytraFlightPower wings) {
                    wings.onApply(player);
                }
            }
        }, 1L);
    }
}
