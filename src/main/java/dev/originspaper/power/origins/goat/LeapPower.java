package dev.originspaper.power.origins.goat;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Active skill: a strong forward leap with an explosive takeoff and a dusty landing. */
public class LeapPower extends AbstractPower implements ActivePowerType {

    private final Map<UUID, Long> leaping = new ConcurrentHashMap<>();

    public LeapPower(String id) {
        super(id);
    }

    @Override
    public void onActivate(Player player) {
        Vector dir = player.getLocation().getDirection().multiply(1.8).setY(0.5);
        player.setVelocity(dir);
        leaping.put(player.getUniqueId(), System.currentTimeMillis());

        Location feet = player.getLocation();
        player.getWorld().playSound(feet, Sound.ENTITY_GOAT_LONG_JUMP, 1.0f, 1.0f);
        ParticleUtil.spawnGroundBurst(Particle.POOF, feet, 0.5, 6, 0.05);
        ParticleUtil.spawnGroundBurst(Particle.CLOUD, feet, 0.5, 8, 0.05);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Long time = leaping.get(player.getUniqueId());
        if (time == null) {
            return;
        }
        if (System.currentTimeMillis() - time > 500L && GroundUtil.isOnGround(player)) {
            Location feet = player.getLocation();
            Object below = feet.clone().subtract(0, 0.2, 0).getBlock().getBlockData();
            ParticleUtil.spawnGroundBurst(Particle.BLOCK, feet, 0.6, 12, 0.0, below);
            ParticleUtil.spawnGroundBurst(Particle.DUST_PLUME, feet, 0.5, 6, 0.05);
            leaping.remove(player.getUniqueId());
        } else {
            // Airborne falling-dust trail tinted by the block under the goat.
            Object below = player.getLocation().clone().subtract(0, 0.2, 0).getBlock().getBlockData();
            ParticleUtil.spawnTrail(Particle.FALLING_DUST, player.getLocation().add(0, 0.2, 0), 2, 0.2, below);
        }
    }

    @Override
    public void onRemove(Player player) {
        leaping.remove(player.getUniqueId());
    }

    @Override
    public long getCooldownTicks() {
        return 60L;
    }
}
