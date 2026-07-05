package dev.originspaper.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/** Armor classification used by "light armor only" style powers. */
public final class ArmorUtil {

    private ArmorUtil() {}

    /**
     * True when the player wears a diamond/netherite chestplate — the "heavy armor" condition that
     * suppresses agility/stealth powers (red panda acrobatics, raccoon stealth, equine gallop,
     * skunk stripes).
     */
    public static boolean isWearingHeavyChest(Player player) {
        ItemStack chest = player.getInventory().getChestplate();
        if (chest == null) {
            return false;
        }
        String name = chest.getType().name();
        return name.startsWith("DIAMOND_") || name.startsWith("NETHERITE_");
    }

    /** Approximate defense points of a single armor piece (matches vanilla per-piece values). */
    public static int defense(ItemStack item) {
        if (item == null) {
            return 0;
        }
        Material m = item.getType();
        String n = m.name();
        if (n.startsWith("LEATHER_")) {
            return leather(n);
        }
        if (n.startsWith("GOLDEN_") || n.startsWith("CHAINMAIL_")) {
            return chainOrGold(n);
        }
        if (n.startsWith("IRON_")) {
            return iron(n);
        }
        if (n.startsWith("DIAMOND_") || n.startsWith("NETHERITE_")) {
            return diamondOrNetherite(n);
        }
        return 0;
    }

    private static int leather(String n) {
        if (n.endsWith("CHESTPLATE")) {
            return 3;
        }
        if (n.endsWith("LEGGINGS")) {
            return 2;
        }
        return 1; // helmet / boots
    }

    private static int chainOrGold(String n) {
        if (n.endsWith("CHESTPLATE")) {
            return 5;
        }
        if (n.endsWith("LEGGINGS")) {
            return 4;
        }
        if (n.endsWith("HELMET")) {
            return 2;
        }
        return 1; // boots
    }

    private static int iron(String n) {
        if (n.endsWith("CHESTPLATE")) {
            return 6;
        }
        if (n.endsWith("LEGGINGS")) {
            return 5;
        }
        if (n.endsWith("HELMET")) {
            return 2;
        }
        return 2; // boots
    }

    private static int diamondOrNetherite(String n) {
        if (n.endsWith("CHESTPLATE")) {
            return 8;
        }
        if (n.endsWith("LEGGINGS")) {
            return 6;
        }
        if (n.endsWith("HELMET")) {
            return 3;
        }
        return 3; // boots
    }
}
