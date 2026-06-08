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

    private EffectUtil() {}

    /** Ensures a permanent, hidden effect of at least the given amplifier is present. */
    public static void ensure(Player player, PotionEffectType type, int amplifier) {
        PotionEffect current = player.getPotionEffect(type);
        if (current == null || current.getAmplifier() < amplifier || current.getDuration() < 40) {
            player.addPotionEffect(new PotionEffect(type, PERMANENT, amplifier, true, false, false));
        }
    }

    public static void clear(Player player, PotionEffectType type) {
        // Only remove if it looks like one of ours (permanent), to avoid stealing brewed effects.
        PotionEffect current = player.getPotionEffect(type);
        if (current != null && current.getDuration() > 1000) {
            player.removePotionEffect(type);
        }
    }

    /** Applies a temporary, hidden effect (used for situational buffs/debuffs). */
    public static void apply(Player player, PotionEffectType type, int durationTicks, int amplifier) {
        player.addPotionEffect(new PotionEffect(type, durationTicks, amplifier, true, false, false));
    }
}
