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

    private SitStackUtil() {}

    /** Other players physically stacked with {@code player}, in either direction. */
    public static List<Player> linkedPlayers(Player player) {
        Entity bottom = player;
        while (bottom.getVehicle() != null) {
            bottom = bottom.getVehicle();
        }
        List<Player> result = new ArrayList<>();
        collect(bottom, player, result);
        return result;
    }

    private static void collect(Entity entity, Player exclude, List<Player> out) {
        if (entity instanceof Player p && !p.equals(exclude)) {
            out.add(p);
        }
        for (Entity passenger : entity.getPassengers()) {
            collect(passenger, exclude, out);
        }
    }
}
