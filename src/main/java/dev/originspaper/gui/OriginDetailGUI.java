package dev.originspaper.gui;

import dev.originspaper.api.Origin;
import dev.originspaper.util.SkullBuilder;
import dev.originspaper.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** Screen 2: detail of a single origin, with Choose and Back buttons. */
public final class OriginDetailGUI {

    public static final int SLOT_HEAD = 13;
    public static final int SLOT_BACK = 29;
    public static final int SLOT_CHOOSE = 31;
    private static final int[] POWER_SLOTS = {19, 20, 21, 22, 23, 24, 25};

    private OriginDetailGUI() {}

    public static void open(Player player, Origin origin) {
        DetailHolder holder = new DetailHolder(origin.id());
        Inventory inv = Bukkit.createInventory(holder, 54, TextUtil.item("§6" + origin.displayName()));
        holder.setInventory(inv);

        ItemStack filler = SkullBuilder.filler();
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, filler);
        }

        // Origin head
        List<String> headLore = List.of("§7" + origin.infos().size() + " poderes");
        ItemStack head = origin.skullTexture() != null
                ? SkullBuilder.createSkull(origin.skullTexture(), "§6§l" + origin.displayName(), headLore)
                : SkullBuilder.create(origin.fallbackIcon(), "§6§l" + origin.displayName(), headLore);
        inv.setItem(SLOT_HEAD, head);

        // Powers
        for (int i = 0; i < POWER_SLOTS.length && i < origin.infos().size(); i++) {
            Origin.PowerInfo info = origin.infos().get(i);
            List<String> lore = new ArrayList<>();
            for (String line : info.lore()) {
                lore.addAll(wrap(line));
            }
            inv.setItem(POWER_SLOTS[i], SkullBuilder.create(Material.PAPER, "§e" + info.name(), lore));
        }

        inv.setItem(SLOT_CHOOSE, SkullBuilder.create(Material.LIME_WOOL, "§a§lEscolher esta Origin",
                List.of("§7Confirma sua escolha.", "§7Não poderá trocar depois.")));
        inv.setItem(SLOT_BACK, SkullBuilder.create(Material.ARROW, "§c§l← Voltar",
                List.of("§7Volta para a lista de origins.")));

        player.openInventory(inv);
    }

    /** Wraps a lore line to at most ~40 characters per line, preserving the leading colour. */
    private static List<String> wrap(String text) {
        List<String> out = new ArrayList<>();
        String prefix = "§7";
        StringBuilder current = new StringBuilder(prefix);
        for (String word : text.split(" ")) {
            if (current.length() - prefix.length() + word.length() > 40 && current.length() > prefix.length()) {
                out.add(current.toString());
                current = new StringBuilder(prefix);
            }
            if (current.length() > prefix.length()) {
                current.append(' ');
            }
            current.append(word);
        }
        if (current.length() > prefix.length()) {
            out.add(current.toString());
        }
        return out;
    }
}
