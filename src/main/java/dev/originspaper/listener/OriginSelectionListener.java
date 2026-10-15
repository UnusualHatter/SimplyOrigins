package dev.originspaper.listener;

import dev.originspaper.OriginsPaper;
import dev.originspaper.api.Origin;
import dev.originspaper.gui.DetailHolder;
import dev.originspaper.gui.OriginDetailGUI;
import dev.originspaper.gui.OriginProgressGUI;
import dev.originspaper.gui.OriginSelectionGUI;
import dev.originspaper.gui.ProgressHolder;
import dev.originspaper.gui.SelectionHolder;
import org.bukkit.Location;
import org.bukkit.Sound;

import dev.originspaper.registry.PlayerOriginData;
import dev.originspaper.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

/** Drives the selection flow: join/quit, GUI clicks and forced reselection until an origin is chosen. */
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
            long configuredDelay = plugin.getConfig().getLong("join-gui-delay-ticks", 20L);
            long delay = Math.max(0L, Math.min(1200L, configuredDelay));
            if (delay != configuredDelay) {
                plugin.log().warn("join-gui-delay-ticks out of range (0..1200): "
                        + configuredDelay + "; using " + delay + ".");
            }
            plugin.log().info(player.getName() + " joined without an origin — opening selection in " + delay + " ticks.");
            UUID playerId = player.getUniqueId();
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                Player resolved = plugin.getServer().getPlayer(playerId);
                PlayerOriginData current = plugin.data().get(playerId);
                if (resolved != null && (current == null || !current.hasOrigin())) {
                    OriginSelectionGUI.open(resolved);
                }
            }, delay);
        } else if (data != null && data.hasOrigin()) {
            // Flash the progression bar shortly after join so the player sees their origin level.
            UUID playerId = player.getUniqueId();
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                Player resolved = plugin.getServer().getPlayer(playerId);
                PlayerOriginData current = plugin.data().get(playerId);
                if (resolved != null && current != null && current.hasOrigin()) {
                    plugin.progression().showBar(resolved);
                }
            }, 25L);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        plugin.progression().handleQuit(e.getPlayer()); // save progress + clear bar before unloading
        plugin.data().unload(e.getPlayer(), true);
    }

    /**
     * Freezes a player in place until they pick an origin: they can still look around (so the
     * selection GUI is usable) but cannot walk away while the choice is pending. Falling is let
     * through: holding someone in mid-air (reset while gliding or jumping) gets them kicked for
     * flying.
     */
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!plugin.getConfig().getBoolean("force-selection-on-join", true) || !e.hasChangedPosition()) {
            return;
        }
        Player player = e.getPlayer();
        PlayerOriginData data = plugin.data().get(player.getUniqueId());
        if (data != null && data.hasOrigin()) {
            return;
        }
        Location from = e.getFrom();
        Location to = e.getTo();
        double y = to != null && to.getY() < from.getY() ? to.getY() : from.getY();
        // Keep their position, but carry over the new yaw/pitch so they can still look around.
        e.setTo(new Location(from.getWorld(), from.getX(), y, from.getZ(),
                to != null ? to.getYaw() : from.getYaw(),
                to != null ? to.getPitch() : from.getPitch()));
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
        } else if (holder instanceof ProgressHolder) {
            e.setCancelled(true); // read-only screen
            if (e.getRawSlot() == OriginProgressGUI.SLOT_CLOSE) {
                Player player = (Player) e.getWhoClicked();
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.2f);
            }
        }
    }

    /** Dragging onto our screens would drop the stack into an inventory that is thrown away. */
    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        InventoryHolder holder = e.getView().getTopInventory().getHolder();
        if (holder instanceof SelectionHolder || holder instanceof DetailHolder
                || holder instanceof ProgressHolder) {
            e.setCancelled(true);
        }
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
            PlayerOriginData data = plugin.data().get(player.getUniqueId());
            if (data != null && data.hasOrigin()) {
                player.sendActionBar(TextUtil.msg("§cEsta seleção expirou; sua origem já foi definida."));
                player.closeInventory();
                return;
            }
            Origin origin = plugin.origins().get(detail.getOriginId());
            if (origin == null) {
                return;
            }
            plugin.data().setOrigin(player, origin);
            player.sendMessage(TextUtil.msg("§6Você escolheu a origem §f" + origin.displayName() + "§6!"));
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
            UUID playerId = player.getUniqueId();
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                Player resolved = plugin.getServer().getPlayer(playerId);
                PlayerOriginData current = plugin.data().get(playerId);
                if (resolved != null && (current == null || !current.hasOrigin())) {
                    OriginSelectionGUI.open(resolved);
                }
            }, 1L);
        }
    }
}
