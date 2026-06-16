package dev.originspaper.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds other players riding in the same vehicle/passenger stack as a given player.
 * Covers plugins like GSit's player-on-player "sit", which chain players together
 * through invisible seat entities using normal vehicle/passenger relations.
 */
public final class SitStackUtil {

    /** Hard cap on stack traversal — guards against a malformed/cyclic passenger graph. */
    private static final int MAX_DEPTH = 32;

    private SitStackUtil() {}

    /** Other players physically stacked with {@code player}, in either direction. */
    public static List<Player> linkedPlayers(Player player) {
        // Fast path: not riding anything and nothing riding it → not stacked with anyone. Avoids
        // any allocation on the common case — this runs on every mob-target event for a player.
        if (player.getVehicle() == null && player.getPassengers().isEmpty()) {
            return List.of();
        }
        Entity bottom = player;
        int guard = 0;
        while (bottom.getVehicle() != null && guard++ < MAX_DEPTH) {
            bottom = bottom.getVehicle();
        }
        List<Player> result = new ArrayList<>();
        collect(bottom, player, result, 0);
        return result;
    }

    private static void collect(Entity entity, Player exclude, List<Player> out, int depth) {
        if (depth > MAX_DEPTH) {
            return;
        }
        if (entity instanceof Player p && !p.equals(exclude)) {
            out.add(p);
        }
        for (Entity passenger : entity.getPassengers()) {
            collect(passenger, exclude, out, depth + 1);
        }
    }
}
