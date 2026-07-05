package dev.originspaper.util;

import dev.originspaper.OriginsPaper;
import dev.originspaper.progression.OriginProgress;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

/** YAML persistence: one file per player storing the chosen origin id and per-origin progression. */
public final class PersistenceUtil {

    private final OriginsPaper plugin;
    private final File dataDir;

    public PersistenceUtil(OriginsPaper plugin) {
        this.plugin = plugin;
        this.dataDir = new File(plugin.getDataFolder(), "data");
        try {
            Files.createDirectories(dataDir.toPath());
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create player data directory", e);
        }
    }

    private File originFile(UUID id) {
        return new File(dataDir, id + ".yml");
    }

    public String loadOrigin(UUID id) {
        File f = originFile(id);
        if (!f.exists()) {
            return null;
        }
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        return cfg.getString("origin");
    }

    /** Loads every origin's saved progression for this player (empty map if none). */
    public Map<String, OriginProgress> loadProgress(UUID id) {
        Map<String, OriginProgress> map = new HashMap<>();
        File f = originFile(id);
        if (!f.exists()) {
            return map;
        }
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        ConfigurationSection section = cfg.getConfigurationSection("progress");
        if (section != null) {
            for (String originId : section.getKeys(false)) {
                int level = section.getInt(originId + ".level", 1);
                int xp = section.getInt(originId + ".xp", 0);
                map.put(originId, new OriginProgress(level, xp));
            }
        }
        return map;
    }

    /** Loads only cooldown ids belonging to the currently selected origin. */
    public Map<String, Long> loadCooldowns(UUID id, Set<String> allowedPowerIds) {
        Map<String, Long> map = new HashMap<>();
        File f = originFile(id);
        if (!f.exists() || allowedPowerIds.isEmpty()) {
            return map;
        }
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        ConfigurationSection section = cfg.getConfigurationSection("cooldowns");
        if (section == null) {
            return map;
        }
        long now = System.currentTimeMillis();
        for (String powerId : section.getKeys(false)) {
            if (!allowedPowerIds.contains(powerId)) {
                continue;
            }
            long timestamp = section.getLong(powerId, 0L);
            if (timestamp <= 0L) {
                continue;
            }
            // A future timestamp caused by clock skew or manual file editing must not lock a
            // player's ability indefinitely. Treat it as a fresh use instead.
            map.put(powerId, timestamp > now + 5000L ? now : timestamp);
        }
        return map;
    }

    /** Writes the chosen origin and the full per-origin progression map. */
    public void save(UUID id, String originId, Map<String, OriginProgress> progress,
                     Map<String, Long> cooldowns) {
        File f = originFile(id);
        File temp = new File(dataDir, id + ".yml.tmp");
        YamlConfiguration cfg = new YamlConfiguration();
        cfg.set("origin", originId);
        for (Map.Entry<String, OriginProgress> entry : progress.entrySet()) {
            cfg.set("progress." + entry.getKey() + ".level", entry.getValue().level());
            cfg.set("progress." + entry.getKey() + ".xp", entry.getValue().xp());
        }
        long now = System.currentTimeMillis();
        for (Map.Entry<String, Long> entry : cooldowns.entrySet()) {
            long timestamp = entry.getValue();
            if (timestamp > 0L && timestamp <= now + 5000L) {
                cfg.set("cooldowns." + entry.getKey(), timestamp);
            }
        }
        try {
            cfg.save(temp);
            try {
                Files.move(temp.toPath(), f.toPath(), StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temp.toPath(), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save data for " + id, e);
            try {
                Files.deleteIfExists(temp.toPath());
            } catch (IOException ignored) {
                // The original save error is the useful one.
            }
        }
    }

    public void deleteOrigin(UUID id) {
        File f = originFile(id);
        try {
            Files.deleteIfExists(f.toPath());
            Files.deleteIfExists(new File(dataDir, id + ".yml.tmp").toPath());
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to delete data for " + id, e);
        }
    }
}
