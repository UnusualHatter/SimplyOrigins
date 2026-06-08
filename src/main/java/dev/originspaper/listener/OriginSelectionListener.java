package dev.originspaper.listener;

import dev.originspaper.OriginsPaper;
import dev.originspaper.api.Origin;
import dev.originspaper.gui.DetailHolder;
import dev.originspaper.gui.OriginDetailGUI;
import dev.originspaper.gui.OriginSelectionGUI;
import dev.originspaper.gui.SelectionHolder;

import dev.originspaper.registry.PlayerOriginData;
import dev.originspaper.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/** Drives the selection flow: join/quit, GUI clicks, forced reselection and Shulk bag saving. */
public class OriginSelectionListener implements Listener {

    private final OriginsPaper plugin;

    public OriginSelectionListener(OriginsPaper plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        plugin.data().load(player);
        PlayerOriginData data = plugin.data().get(player.getUniqueId());
        if ((data == null || !data.hasOrigin())
                && plugin.getConfig().getBoolean("force-selection-on-join", true)) {
            long delay = plugin.getConfig().getLong("join-gui-delay-ticks", 20L);
            plugin.log().info(player.getName() + " joined without an origin — opening selection in " + delay + " ticks.");
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline()) {
                    OriginSelectionGUI.open(player);
                }
            }, delay);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        plugin.data().unload(e.getPlayer(), true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getView().getTopInventory().getHolder();
        if (holder instanceof SelectionHolder) {
            e.setCancelled(true);
            handleSelectionClick((Player) e.getWhoClicked(), e.getCurrentItem());
        } else if (holder instanceof DetailHolder detail) {
            e.setCancelled(true);
            handleDetailClick((Player) e.getWhoClicked(), detail, e.getRawSlot());
        }
        // HoarderHolder: not cancelled, the player may freely move items in their bag.
    }

    private void handleSelectionClick(Player player, ItemStack clicked) {
        if (clicked == null || !clicked.hasItemMeta()) {
            return;
        }
        String originId = clicked.getItemMeta().getPersistentDataContainer()
                .get(OriginSelectionGUI.originKey(), PersistentDataType.STRING);
        if (originId == null) {
            return;
        }
        Origin origin = plugin.origins().get(originId);
        if (origin != null) {
            plugin.log().debug(player.getName() + " opened detail for: " + origin.displayName());
            OriginDetailGUI.open(player, origin);
        }
    }

    private void handleDetailClick(Player player, DetailHolder detail, int rawSlot) {
        if (rawSlot == OriginDetailGUI.SLOT_BACK) {
            plugin.log().debug(player.getName() + " went back to origin selection.");
            OriginSelectionGUI.open(player);
            return;
        }
        if (rawSlot == OriginDetailGUI.SLOT_CHOOSE) {
            Origin origin = plugin.origins().get(detail.getOriginId());
            if (origin == null) {
                return;
            }
            plugin.data().setOrigin(player, origin);
            player.sendMessage(TextUtil.msg("§6Você escolheu a origin §f" + origin.displayName() + "§6!"));
            player.closeInventory();
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();

        if (!(holder instanceof SelectionHolder || holder instanceof DetailHolder)) {
            return;
        }
        // Don't reopen when the player is just navigating between our own GUIs.
        if (e.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) {
            return;
        }
        if (!plugin.getConfig().getBoolean("force-selection-on-join", true)) {
            return;
        }
        Player player = (Player) e.getPlayer();
        PlayerOriginData data = plugin.data().get(player.getUniqueId());
        if (data == null || !data.hasOrigin()) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline()) {
                    OriginSelectionGUI.open(player);
                }
            }, 1L);
        }
    }
}
