package dev.originspaper.api;

import org.bukkit.entity.Player;

/**
 * A power that can be triggered manually with the active-skill input (sneak + swap hands / F).
 * Each origin has at most one {@code ActivePowerType}.
 */
public interface ActivePowerType extends PowerType {

    void onActivate(Player player);

    /** Cooldown in ticks. Return 0 for no cooldown. */
    long getCooldownTicks(Player player);
}
