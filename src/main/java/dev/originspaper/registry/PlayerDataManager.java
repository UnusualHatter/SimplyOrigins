package dev.originspaper.registry;

import dev.originspaper.OriginsPaper;
import dev.originspaper.api.Origin;
import dev.originspaper.api.PowerType;
import dev.originspaper.util.PersistenceUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Owns the live {@link PlayerOriginData} for every online player and drives apply/remove. */
public class PlayerDataManager {

    private final OriginsPaper plugin;
    private final PersistenceUtil persistence;
    private final Map<UUID, PlayerOriginData> data = new ConcurrentHashMap<>();

    public PlayerDataManager(OriginsPaper plugin) {
        this.plugin = plugin;
        this.persistence = new PersistenceUtil(plugin);
    }

    public PersistenceUtil persistence() {
        return persistence;
    }

    public PlayerOriginData get(UUID id) {
        return data.get(id);
    }

    public PlayerOriginData getOrCreate(UUID id) {
        return data.computeIfAbsent(id, PlayerOriginData::new);
    }

    /** Loads the saved origin (if any) and applies its powers. Called on join. */
    public void load(Player player) {
        PlayerOriginData d = getOrCreate(player.getUniqueId());
        d.getProgressMap().putAll(persistence.loadProgress(player.getUniqueId()));
        String originId = persistence.loadOrigin(player.getUniqueId());
        if (originId != null) {
            Origin origin = plugin.origins().get(originId);
            if (origin != null) {
                plugin.log().info(player.getName() + " joined with origin: " + origin.displayName());
                // Join is not a deliberate (re)selection: never top players up to full, or
                // relogging at low health would become a free heal.
                applyOrigin(player, d, origin, false);
            } else {
                plugin.log().warn(player.getName() + " had unknown saved origin '" + originId + "' — ignored.");
            }
        } else {
            plugin.log().debug(player.getName() + " joined without a saved origin.");
        }
    }

    /** Admin/selection entry point: switch the player to an origin and persist it. */
    public void setOrigin(Player player, Origin origin) {
        PlayerOriginData d = getOrCreate(player.getUniqueId());
        plugin.log().info(player.getName() + " selected origin: " + origin.displayName());
        // Deliberate selection (GUI choice / admin set): fill the player up to the new max.
        applyOrigin(player, d, origin, true);
        d.progress(origin.id()); // ensure a level-1 progression entry exists for this origin
        persistence.save(player.getUniqueId(), origin.id(), d.getProgressMap());
        plugin.progression().showBar(player); // surface the new origin's level bar right away
    }

    private void applyOrigin(Player player, PlayerOriginData d, Origin origin, boolean healToFull) {
        removeAll(player, d);
        d.setOrigin(origin);
        d.getPowers().clear();
        d.getCooldowns().clear();
        d.getPowers().addAll(origin.powers());

        for (PowerType power : origin.powers()) {
            try {
                plugin.log().debug("Applying power " + power.getId() + " to " + player.getName());
                power.onApply(player);
            } catch (Exception e) {
                plugin.log().warn("onApply failed for " + power.getId() + " on " + player.getName(), e);
            }
        }

        // Always clamp current HP down to the new max (in case it shrank). Only top the player
        // up to the new max on a deliberate selection — never on a plain join/load.
        clampHealth(player, healToFull);
    }

    /** Removes the player's origin and deletes their save, forcing reselection. */
    public void reset(Player player) {
        plugin.log().info("Resetting origin for " + player.getName());
        PlayerOriginData d = data.get(player.getUniqueId());
        if (d != null) {
            removeAll(player, d);
            d.setOrigin(null);
            d.getPowers().clear();
            d.getCooldowns().clear();
            // Wipe progression too: deleteOrigin clears the file, but the in-memory map would
            // otherwise be written straight back on the next selection — making the "reset"
            // silently keep every origin's old level/XP.
            d.getProgressMap().clear();
        }
        persistence.deleteOrigin(player.getUniqueId());
        plugin.progression().clearBar(player); // drop the stale level bar of the removed origin
        // Clamp HP to the restored vanilla max after all modifiers are cleared (no free heal).
        clampHealth(player, false);
    }

    private void removeAll(Player player, PlayerOriginData d) {
        // Iterate over a snapshot so powers can safely mutate the list during removal.
        List<PowerType> snapshot = new ArrayList<>(d.getPowers());
        for (PowerType power : snapshot) {
            try {
                plugin.log().debug("Removing power " + power.getId() + " from " + player.getName());
                power.onRemove(player);
            } catch (Exception e) {
                plugin.log().warn("onRemove failed for " + power.getId() + " on " + player.getName(), e);
            }
        }
    }

    /**
     * Clamps the player's health to their current max (in case it dropped below). When
     * {@code healToFull} is set — only on a deliberate origin selection — the player is also
     * topped up to the new max. On a plain join/load this stays a pure clamp, so relogging at
     * low health is never a free heal.
     */
    private void clampHealth(Player player, boolean healToFull) {
        AttributeInstance maxHpAttr = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHpAttr == null) {
            return;
        }
        double newMax = maxHpAttr.getValue();
        double current = player.getHealth();
        if (current > newMax) {
            // Max shrank — clamp so the player isn't above max.
            player.setHealth(newMax);
        } else if (healToFull && newMax > current) {
            // Deliberate selection — fill up to the new max.
            player.setHealth(newMax);
        }
    }

    /** Called on quit/disable: remove power effects; optionally drop the cached data. */
    public void unload(Player player, boolean forget) {
        PlayerOriginData d = data.get(player.getUniqueId());
        if (d != null) {
            plugin.log().debug(player.getName() + " unloading — removing " + d.getPowers().size() + " powers.");
            removeAll(player, d);
        }
        if (forget) {
            data.remove(player.getUniqueId());
        }
    }
}
