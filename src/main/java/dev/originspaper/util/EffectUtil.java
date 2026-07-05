package dev.originspaper.util;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Helpers for potion effects, including "permanent" effects re-applied from the tick loop.
 */
public final class EffectUtil {

    /** Long duration used for effects that should appear permanent. */
    public static final int PERMANENT = 999999;
    private static final int REFRESH_THRESHOLD = 40;

    private EffectUtil() {}

    /** Ensures a permanent, hidden effect of at least the given amplifier is present. */
    public static void ensure(Player player, PotionEffectType type, int amplifier) {
        PotionEffect current = player.getPotionEffect(type);
        if (current == null || current.getAmplifier() < amplifier
                || (isManaged(current) && current.getDuration() < REFRESH_THRESHOLD)) {
            player.addPotionEffect(new PotionEffect(type, PERMANENT, amplifier, true, false, false));
        }
    }

    public static void clear(Player player, PotionEffectType type) {
        PotionEffect current = player.getPotionEffect(type);
        if (current != null && isManaged(current) && current.getDuration() > 1000) {
            player.removePotionEffect(type);
        }
    }

    /**
     * Removes one of our short hidden effects without touching visible brewed/plugin effects.
     * The duration ceiling keeps a later, longer hidden effect from another source intact.
     */
    public static void clearTemporary(Player player, PotionEffectType type,
                                      int maxDurationTicks, int amplifier) {
        PotionEffect current = player.getPotionEffect(type);
        if (current != null && isManaged(current) && current.getAmplifier() == amplifier
                && current.getDuration() <= maxDurationTicks + 20) {
            player.removePotionEffect(type);
        }
    }

    /** Origin effects are consistently hidden and ambient; normal brewed effects are not. */
    public static boolean isManaged(PotionEffect effect) {
        return effect.isAmbient() && !effect.hasParticles() && !effect.hasIcon();
    }

    /** Applies a temporary, hidden effect (used for situational buffs/debuffs). */
    public static void apply(Player player, PotionEffectType type, int durationTicks, int amplifier) {
        player.addPotionEffect(new PotionEffect(type, durationTicks, amplifier, true, false, false));
    }
}
