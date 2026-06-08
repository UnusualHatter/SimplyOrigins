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
        String originId = persistence.loadOrigin(player.getUniqueId());
        if (originId != null) {
            Origin origin = plugin.origins().get(originId);
            if (origin != null) {
                plugin.log().info(player.getName() + " joined with origin: " + origin.displayName());
                applyOrigin(player, d, origin);
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
        applyOrigin(player, d, origin);
        persistence.saveOrigin(player.getUniqueId(), origin.id());
    }

    private void applyOrigin(Player player, PlayerOriginData d, Origin origin) {
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

        // After all powers are applied, clamp the player's current HP to the new max
        // and heal them if the new max is higher (e.g. Dragon gaining extra hearts).
        clampAndHeal(player);
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
        }
        persistence.deleteOrigin(player.getUniqueId());
        // Restore HP to vanilla max after all modifiers are cleared.
        clampAndHeal(player);
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
     * Clamps the player's health to their current max (in case it dropped) and heals them
     * to full when the new max is higher than their previous health.
     */
    private void clampAndHeal(Player player) {
        AttributeInstance maxHpAttr = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHpAttr == null) {
            return;
        }
        double newMax = maxHpAttr.getValue();
        double current = player.getHealth();
        if (current > newMax) {
            // Max shrank — clamp so the player isn't above max.
            player.setHealth(newMax);
        } else if (newMax > current) {
            // Max grew — fill the new hearts completely.
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
