package dev.originspaper.util;

import dev.originspaper.OriginsPaper;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

/** YAML persistence: one file per player storing the chosen origin id. */
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

    public void saveOrigin(UUID id, String originId) {
        File f = originFile(id);
        YamlConfiguration cfg = new YamlConfiguration();
        cfg.set("origin", originId);
        try {
            cfg.save(f);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save origin for " + id, e);
        }
    }

    public void deleteOrigin(UUID id) {
        File f = originFile(id);
        if (f.exists()) {
            f.delete();
        }
    }
}
