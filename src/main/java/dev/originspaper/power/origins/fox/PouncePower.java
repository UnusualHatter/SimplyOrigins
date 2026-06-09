package dev.originspaper.power.origins.fox;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
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

        Location feet = player.getLocation();
        Object below = feet.clone().subtract(0, 0.2, 0).getBlock().getBlockData();
        ParticleUtil.spawnGroundBurst(Particle.BLOCK, feet, 0.5, 10, 0.0, below);
        ParticleUtil.spawnGroundBurst(Particle.CLOUD, feet, 0.5, 8, 0.05);
        ParticleUtil.spawn(Particle.SWEEP_ATTACK, feet.clone().add(0, 0.8, 0), 1, 0, 0, 0, 0.0);
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        // Bonus damage on any hit while the pounce window is active (player is airborne).
        if (pouncing.containsKey(player.getUniqueId())) {
            e.setDamage(e.getDamage() + 3.0);
            pouncing.remove(player.getUniqueId()); // consume — one bonus hit per pounce
        }
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Long time = pouncing.get(player.getUniqueId());
        if (time == null) {
            return;
        }
        if (System.currentTimeMillis() - time > 500L && GroundUtil.isOnGround(player)) {
            // Landing impact.
            Location feet = player.getLocation();
            Object below = feet.clone().subtract(0, 0.2, 0).getBlock().getBlockData();
            ParticleUtil.spawnGroundBurst(Particle.BLOCK, feet, 0.6, 12, 0.0, below);
            ParticleUtil.spawn(Particle.DAMAGE_INDICATOR, feet.clone().add(0, 0.5, 0), 4, 0.3, 0.2, 0.3, 0.0);
            ParticleUtil.spawnGroundBurst(Particle.POOF, feet, 0.4, 4, 0.02);
            pouncing.remove(player.getUniqueId());
        } else {
            // Airborne crit trail.
            ParticleUtil.spawnTrail(Particle.CRIT, player.getLocation().add(0, 0.5, 0), 2, 0.2);
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
