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

    /**
     * Precondition gate checked before {@link #onActivate}: return false when the skill can't fire
     * right now (e.g. the shark's dash out of water). A failed gate consumes the input but does
     * <b>not</b> start the cooldown — the implementation should tell the player why (action bar).
     */
    default boolean canActivate(Player player) {
        return true;
    }
}
