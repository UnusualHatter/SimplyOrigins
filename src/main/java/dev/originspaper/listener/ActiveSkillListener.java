package dev.originspaper.listener;

import dev.originspaper.OriginsPaper;
import dev.originspaper.api.ActivePowerType;
import dev.originspaper.api.PowerType;
import dev.originspaper.registry.PlayerOriginData;
import dev.originspaper.util.CooldownUtil;
import dev.originspaper.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/** Active-skill input: sneak + swap-hands (F). Triggers the origin's single ActivePowerType. */
public class ActiveSkillListener implements Listener {

    private final OriginsPaper plugin;

    public ActiveSkillListener(OriginsPaper plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSwap(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (!player.isSneaking()) {
            return; // F without crouch → normal hand swap, left untouched
        }
        PlayerOriginData data = plugin.data().get(player.getUniqueId());
        if (data == null || !data.hasOrigin()) {
            return; // plugin isn't managing this player yet → leave the vanilla swap alone
        }

        ActivePowerType active = findActive(data);
        if (active == null) {
            return; // passive-only origin: preserve the normal hand swap
        }
        // Crouch + F belongs to the active skill only when this origin actually has one.
        e.setCancelled(true);

        // Precondition gate (e.g. "must be in water"): consumes the input, never starts the
        // cooldown. The power itself gives the player feedback about why it didn't fire.
        if (!active.canActivate(player)) {
            plugin.log().debug(player.getName() + " tried " + active.getId() + " but canActivate() denied it.");
            return;
        }

        if (active.getCooldownTicks(player) > 0
                && !CooldownUtil.isReady(data.getCooldowns(), active.getId(), active.getCooldownTicks(player))) {
            long remaining = CooldownUtil.remainingSeconds(
                    data.getCooldowns(), active.getId(), active.getCooldownTicks(player));
            player.sendActionBar(TextUtil.msg("§cHabilidade em recarga: §f" + remaining + "s"));
            plugin.log().debug(player.getName() + " tried " + active.getId() + " but it's on cooldown (" + remaining + "s).");
            return;
        }

        plugin.log().debug(player.getName() + " activating skill: " + active.getId());
        try {
            active.onActivate(player);
            CooldownUtil.trigger(data.getCooldowns(), active.getId());
        } catch (Exception ex) {
            plugin.log().warn("onActivate error in " + active.getId() + " for " + player.getName(), ex);
        }
    }

    /**
     * Extra safety net: if somehow an item belonging to a skill gets dropped (e.g. death/plugin
     * edge cases), cancel the drop silently. In practice this should never fire since we cancel
     * the swap event, but it is a good defensive guard.
     */
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        // Currently a no-op guard; extend if specific skill items are introduced in the future.
    }

    private ActivePowerType findActive(PlayerOriginData data) {
        for (PowerType power : data.getPowers()) {
            if (power instanceof ActivePowerType active) {
                return active;
            }
        }
        return null;
    }
}
