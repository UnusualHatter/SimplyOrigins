package dev.originspaper.gui;

import dev.originspaper.OriginsPaper;
import dev.originspaper.api.Origin;
import dev.originspaper.progression.OriginProgress;
import dev.originspaper.progression.OriginProgression;
import dev.originspaper.progression.ProgressionDefs;
import dev.originspaper.progression.ProgressionManager;
import dev.originspaper.registry.PlayerOriginData;
import dev.originspaper.util.SkullBuilder;
import dev.originspaper.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** "Minha Origem": a player's own origin — level, XP, abilities, challenges and milestone unlocks. */
public final class OriginProgressGUI {

    public static final int SLOT_CLOSE = 49;

    private static final int SLOT_HEAD = 4;
    private static final int[] ABILITY_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25};
    private static final int SLOT_CHALLENGE_LABEL = 27;
    private static final int[] CHALLENGE_SLOTS = {28, 29, 30, 31, 32, 33, 34};
    private static final int SLOT_UNLOCK_LABEL = 36;
    private static final int[] UNLOCK_SLOTS = {37, 38, 39, 40, 41, 42, 43};

    private OriginProgressGUI() {}

    public static void open(Player player) {
        OriginsPaper plugin = OriginsPaper.instance();
        PlayerOriginData data = plugin.data().get(player.getUniqueId());
        if (data == null || !data.hasOrigin()) {
            return;
        }
        Origin origin = data.getOrigin();
        OriginProgress prog = data.currentProgress();
        OriginProgression pdef = ProgressionDefs.get(origin.id());

        ProgressHolder holder = new ProgressHolder();
        Inventory inv = Bukkit.createInventory(holder, 54, TextUtil.item("§6Minha Origem §8— §e" + origin.displayName()));
        holder.setInventory(inv);

        ItemStack filler = SkullBuilder.filler();
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, filler);
        }

        inv.setItem(SLOT_HEAD, headItem(origin, prog));

        // Current abilities (the origin's base kit).
        for (int i = 0; i < ABILITY_SLOTS.length && i < origin.infos().size(); i++) {
            Origin.PowerInfo info = origin.infos().get(i);
            List<String> lore = new ArrayList<>();
            for (String line : info.lore()) {
                lore.addAll(wrap(line, "§7"));
            }
            inv.setItem(ABILITY_SLOTS[i], SkullBuilder.create(Material.PAPER, "§e" + info.name(), lore));
        }

        // Challenges: how to earn XP toward the next level.
        inv.setItem(SLOT_CHALLENGE_LABEL, SkullBuilder.create(Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                "§b§lDesafios", List.of("§7Complete-os para ganhar XP")));
        if (pdef != null && !pdef.objectives().isEmpty()) {
            List<OriginProgression.Objective> objectives = pdef.objectives();
            int count = Math.min(objectives.size(), CHALLENGE_SLOTS.length);
            int offset = (CHALLENGE_SLOTS.length - count) / 2;
            for (int i = 0; i < count; i++) {
                OriginProgression.Objective obj = objectives.get(i);
                inv.setItem(CHALLENGE_SLOTS[offset + i], SkullBuilder.create(Material.TARGET,
                        "§b🎯 " + obj.description(),
                        List.of("§7Recompensa: §e+" + obj.xp() + " XP")));
            }
        }

        // Milestone unlocks (lit up once reached).
        inv.setItem(SLOT_UNLOCK_LABEL, SkullBuilder.create(Material.LIME_STAINED_GLASS_PANE,
                "§a§lDesbloqueios", List.of("§7Recompensas por nível")));
        if (pdef != null && !pdef.unlocks().isEmpty()) {
            List<OriginProgression.Unlock> unlocks = pdef.unlocks();
            int count = Math.min(unlocks.size(), UNLOCK_SLOTS.length);
            int offset = (UNLOCK_SLOTS.length - count) / 2;
            for (int i = 0; i < count; i++) {
                inv.setItem(UNLOCK_SLOTS[offset + i], unlockItem(unlocks.get(i), prog.level()));
            }
        }

        inv.setItem(SLOT_CLOSE, SkullBuilder.create(Material.BARRIER, "§c§lFechar", List.of("§7Sair desta tela.")));

        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 0.6f, 1.4f);
    }

    private static ItemStack headItem(Origin origin, OriginProgress prog) {
        List<String> lore = new ArrayList<>();
        lore.add("§7Nível §e" + prog.level() + "§7/§e" + OriginProgress.MAX_LEVEL);
        lore.add(xpBar(prog));
        lore.add("");
        lore.add("§7Complete os §bdesafios §7para evoluir.");
        lore.add("§8" + origin.infos().size() + " habilidades");

        if (origin.skullTexture() != null) {
            return SkullBuilder.createSkull(origin.skullTexture(), "§6§l" + origin.displayName(), lore);
        }
        return SkullBuilder.create(origin.fallbackIcon(), "§6§l" + origin.displayName(), lore);
    }

    /** A 20-segment text XP bar like {@code §a▉▉▉▉§7▉▉▉ 450/900 XP}. */
    private static String xpBar(OriginProgress prog) {
        if (prog.isMax()) {
            return "§6§lNÍVEL MÁXIMO";
        }
        int need = ProgressionManager.xpToNext(prog.level());
        int filled = (int) Math.round(20.0 * prog.xp() / need);
        StringBuilder bar = new StringBuilder("§a");
        boolean switched = false;
        for (int i = 0; i < 20; i++) {
            if (i >= filled && !switched) {
                bar.append("§7");
                switched = true;
            }
            bar.append("▉");
        }
        return bar + " §f" + prog.xp() + "§7/§f" + need + " XP";
    }

    private static ItemStack unlockItem(OriginProgression.Unlock unlock, int level) {
        boolean unlocked = level >= unlock.level();
        Material icon = unlocked ? Material.LIME_DYE : Material.GRAY_DYE;
        String title = (unlocked ? "§a✔ " : "§c🔒 ") + unlock.name() + " §8(Nv " + unlock.level() + ")";
        List<String> lore = new ArrayList<>(wrap(unlock.description(), "§7"));
        lore.add("");
        lore.add(unlocked ? "§aDesbloqueado!" : "§cDesbloqueia no nível " + unlock.level() + ".");
        return SkullBuilder.create(icon, title, lore);
    }

    /** Wraps a lore line to ~40 chars, keeping the given colour prefix. */
    private static List<String> wrap(String text, String prefix) {
        List<String> out = new ArrayList<>();
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
