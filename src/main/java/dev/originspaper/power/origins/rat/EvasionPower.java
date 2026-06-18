package dev.originspaper.power.origins.rat;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/** 30% chance to dodge projectiles, leaving a quick puff; light skittering dust while moving. */
public class EvasionPower extends AbstractPower {

    /** Last second a dodge granted XP, per player — caps dispenser-arrow XP farms. */
    private final Map<UUID, Long> lastDodgeXp = new ConcurrentHashMap<>();

    public EvasionPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        // Tiny ground-tinted skitter dust while scurrying about.
        if (plugin().tick() % 2 == 0
                && (player.isSprinting() || player.getVelocity().lengthSquared() > 0.01)) {
            Block below = player.getLocation().clone().subtract(0, 0.1, 0).getBlock();
            ParticleUtil.spawnTrail(Particle.BLOCK, player.getLocation().add(0, 0.05, 0), 2, 0.15,
                    below.getBlockData());
        }
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player
                && e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (ThreadLocalRandom.current().nextDouble() < dodgeChance(player)) {
                e.setCancelled(true);
                if (dodgeXpReady(player)) {
                    plugin().progression().awardXp(player, 8); // "Desviar de projéteis"
                }
                Location loc = player.getLocation().add(0, 1.0, 0);
                ParticleUtil.spawnGroundBurst(Particle.CLOUD, loc, 0.4, 6, 0.05);
                ParticleUtil.spawnGroundBurst(Particle.POOF, loc, 0.3, 3, 0.02);
            }
        }
    }

    /** At most one dodge XP award every 3 seconds, to cap arrow-dispenser farms. */
    private boolean dodgeXpReady(Player player) {
        long now = plugin().tick();
        UUID id = player.getUniqueId();
        Long last = lastDodgeXp.get(id);
        if (last != null && now - last < 3) {
            return false;
        }
        lastDodgeXp.put(id, now);
        return true;
    }

    @Override
    public void onRemove(Player player) {
        lastDodgeXp.remove(player.getUniqueId());
    }

    /** Base 30% dodge, rising with progression: Nv3 → 38%, Nv10 → 45%. */
    private double dodgeChance(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        int level = data == null ? 1 : data.level();
        if (level >= 10) {
            return 0.45;
        }
        if (level >= 3) {
            return 0.38;
        }
        return 0.30;
    }
}
