package dev.originspaper.util;

import dev.originspaper.OriginsPaper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

/** YAML persistence: one file per player for the chosen origin, plus the Shulk extra inventory. */
public final class PersistenceUtil {

    private final OriginsPaper plugin;
    private final File dataDir;
    private final File shulkDir;

    public PersistenceUtil(OriginsPaper plugin) {
        this.plugin = plugin;
        this.dataDir = new File(plugin.getDataFolder(), "data");
        this.shulkDir = new File(plugin.getDataFolder(), "shulk_inv");
        dataDir.mkdirs();
        shulkDir.mkdirs();
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

    // --- Shulk extra inventory ---

    public ItemStack[] loadShulkInventory(UUID id, int size) {
        File f = new File(shulkDir, id + ".yml");
        if (!f.exists()) {
            return new ItemStack[size];
        }
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        @SuppressWarnings("unchecked")
        java.util.List<ItemStack> list = (java.util.List<ItemStack>) cfg.getList("items");
        ItemStack[] contents = new ItemStack[size];
        if (list != null) {
            for (int i = 0; i < size && i < list.size(); i++) {
                contents[i] = list.get(i);
            }
        }
        return contents;
    }

    public void saveShulkInventory(UUID id, ItemStack[] contents) {
        File f = new File(shulkDir, id + ".yml");
        YamlConfiguration cfg = new YamlConfiguration();
        cfg.set("items", java.util.Arrays.asList(contents));
        try {
            cfg.save(f);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save shulk inventory for " + id, e);
        }
    }
}
