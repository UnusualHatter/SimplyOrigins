package dev.originspaper.gui;

import dev.originspaper.OriginsPaper;
import dev.originspaper.api.Origin;
import dev.originspaper.util.SkullBuilder;
import dev.originspaper.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/** Screen 1: a grid of all origins shown as custom heads. Clicking one opens the detail screen. */
public final class OriginSelectionGUI {

    /** Inner grid slots, skipping the border columns/rows. */
    public static final int[] INNER_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43};

    private OriginSelectionGUI() {}

    public static NamespacedKey originKey() {
        return new NamespacedKey(OriginsPaper.instance(), "origin_id");
    }

    public static void open(Player player) {
        OriginsPaper plugin = OriginsPaper.instance();
        SelectionHolder holder = new SelectionHolder();
        Inventory inv = Bukkit.createInventory(holder, 54, TextUtil.item("§6Escolha sua Origin"));
        holder.setInventory(inv);

        ItemStack filler = SkullBuilder.filler();
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, filler);
        }

        List<Origin> origins = new ArrayList<>(plugin.origins().all());
        for (int i = 0; i < origins.size() && i < INNER_SLOTS.length; i++) {
            Origin origin = origins.get(i);
            inv.setItem(INNER_SLOTS[i], icon(origin));
        }

        player.openInventory(inv);
    }

    private static ItemStack icon(Origin origin) {
        List<String> lore = new ArrayList<>();
        if (origin.infos().isEmpty()) {
            lore.add("§7Sem poderes especiais.");
        } else {
            int shown = Math.min(3, origin.infos().size());
            for (int i = 0; i < shown; i++) {
                lore.add("§7• " + origin.infos().get(i).name());
            }
            if (origin.infos().size() > shown) {
                lore.add("§8...e mais " + (origin.infos().size() - shown) + " poderes");
            }
        }
        lore.add("");
        lore.add("§e▶ Clique para ver detalhes");

        ItemStack icon;
        if (origin.skullTexture() != null) {
            icon = SkullBuilder.createSkull(origin.skullTexture(), "§6" + origin.displayName(), lore);
        } else {
            icon = SkullBuilder.create(origin.fallbackIcon(), "§6" + origin.displayName(), lore);
        }
        icon.editMeta(meta -> meta.getPersistentDataContainer()
                .set(originKey(), PersistentDataType.STRING, origin.id()));
        return icon;
    }
}
