package dev.originspaper.util;

import dev.originspaper.OriginsPaper;
import dev.originspaper.progression.OriginProgress;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/** YAML persistence: one file per player storing the chosen origin id and per-origin progression. */
public final class PersistenceUtil {

    private final OriginsPaper plugin;
    private final File dataDir;

    public PersistenceUtil(OriginsPaper plugin) {
        this.plugin = plugin;
        this.dataDir = new File(plugin.getDataFolder(), "data");
        dataDir.mkdirs();
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

    /** Writes the chosen origin and the full per-origin progression map. */
    public void save(UUID id, String originId, Map<String, OriginProgress> progress) {
        File f = originFile(id);
        YamlConfiguration cfg = new YamlConfiguration();
        cfg.set("origin", originId);
        for (Map.Entry<String, OriginProgress> entry : progress.entrySet()) {
            cfg.set("progress." + entry.getKey() + ".level", entry.getValue().level());
            cfg.set("progress." + entry.getKey() + ".xp", entry.getValue().xp());
        }
        try {
            cfg.save(f);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save data for " + id, e);
        }
    }

    public void deleteOrigin(UUID id) {
        File f = originFile(id);
        if (f.exists()) {
            f.delete();
        }
    }
}
