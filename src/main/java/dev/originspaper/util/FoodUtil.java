package dev.originspaper.util;

import org.bukkit.Material;

import java.util.Set;

/** Classification helpers for food/diet powers. */
public final class FoodUtil {

    private static final Set<Material> RAW_MEAT = Set.of(
            Material.BEEF, Material.PORKCHOP, Material.CHICKEN, Material.MUTTON,
            Material.RABBIT, Material.COD, Material.SALMON, Material.TROPICAL_FISH,
            Material.PUFFERFISH);

    private static final Set<Material> COOKED_MEAT = Set.of(
            Material.COOKED_BEEF, Material.COOKED_PORKCHOP, Material.COOKED_CHICKEN,
            Material.COOKED_MUTTON, Material.COOKED_RABBIT, Material.COOKED_COD,
            Material.COOKED_SALMON);

    private static final Set<Material> OTHER_MEAT = Set.of(
            Material.ROTTEN_FLESH, Material.RABBIT_STEW);

    private FoodUtil() {}

    public static boolean isRawMeat(Material m) {
        return RAW_MEAT.contains(m);
    }

    public static boolean isMeat(Material m) {
        return RAW_MEAT.contains(m) || COOKED_MEAT.contains(m) || OTHER_MEAT.contains(m);
    }
}
