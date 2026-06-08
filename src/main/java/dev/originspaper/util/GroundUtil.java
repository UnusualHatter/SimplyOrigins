package dev.originspaper.util;

import org.bukkit.entity.Player;

/** Non-deprecated ground check based on the block just below the player's feet. */
public final class GroundUtil {

    private GroundUtil() {}

    public static boolean isOnGround(Player player) {
        return player.getLocation().clone().subtract(0, 0.05, 0).getBlock().getType().isSolid();
    }
}
