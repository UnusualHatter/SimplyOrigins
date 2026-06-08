package dev.originspaper.power.origins.fox;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.GroundUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Active skill: a leaping pounce that deals bonus damage on the next hit while airborne. */
public class PouncePower extends AbstractPower implements ActivePowerType {

    private final Map<UUID, Long> pouncing = new ConcurrentHashMap<>();

    public PouncePower(String id) {
        super(id);
    }

    @Override
    public void onActivate(Player player) {
        Vector dir = player.getLocation().getDirection().multiply(1.5).setY(0.6);
        player.setVelocity(dir);
        pouncing.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        // Deal bonus damage on any hit while the pounce window is active (player is airborne).
        // We no longer check velocityY because the server may have already zeroed it before
        // the damage event fires; simply being in the pouncing set is sufficient.
        if (pouncing.containsKey(player.getUniqueId())) {
            e.setDamage(e.getDamage() + 3.0);
            pouncing.remove(player.getUniqueId()); // consume — one bonus hit per pounce
        }
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Long time = pouncing.get(e.getPlayer().getUniqueId());
        if (time != null && System.currentTimeMillis() - time > 500L) {
            if (GroundUtil.isOnGround(e.getPlayer())) {
                pouncing.remove(e.getPlayer().getUniqueId());
            }
        }
    }

    @Override
    public void onRemove(Player player) {
        pouncing.remove(player.getUniqueId());
    }

    @Override
    public long getCooldownTicks() {
        return 100L;
    }
}
