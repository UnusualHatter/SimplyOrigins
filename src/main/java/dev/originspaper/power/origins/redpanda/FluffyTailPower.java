package dev.originspaper.power.origins.redpanda;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.AttributeUtil;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Active skill: a fluffy burst of momentum — Speed II plus Slow Falling, made for tree-to-tree
 * hops, escapes and fall recovery. Pure mobility: no damage, no claim concerns.
 */
public class FluffyTailPower extends AbstractPower implements ActivePowerType {

    private final long cooldownTicks;
    private final int speedTicks;
    private final int slowFallTicks;
    private final Map<UUID, Long> speedActivations = new ConcurrentHashMap<>();
    private final Map<UUID, Long> slowFallActivations = new ConcurrentHashMap<>();

    public FluffyTailPower(String id, long cooldownTicks, int speedTicks, int slowFallTicks) {
        super(id);
        this.cooldownTicks = cooldownTicks;
        this.speedTicks = speedTicks;
        this.slowFallTicks = slowFallTicks;
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onActivate(Player player) {
        UUID playerId = player.getUniqueId();
        long activation = System.nanoTime();
        speedActivations.put(playerId, activation);
        slowFallActivations.put(playerId, activation);
        AttributeUtil.set(player, Attribute.MOVEMENT_SPEED, id + ".speed", 0.40,
                AttributeModifier.Operation.ADD_SCALAR); // Speed II equivalent
        EffectUtil.apply(player, PotionEffectType.SLOW_FALLING, slowFallTicks, 0);
        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            if (speedActivations.remove(playerId, activation)) {
                Player resolved = resolveActivePlayer(playerId);
                if (resolved != null) {
                    AttributeUtil.clear(resolved, Attribute.MOVEMENT_SPEED, id + ".speed");
                }
            }
        }, Math.max(1L, speedTicks));
        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            if (slowFallActivations.remove(playerId, activation)) {
                Player resolved = resolveActivePlayer(playerId);
                if (resolved != null) {
                    EffectUtil.clearTemporary(resolved, PotionEffectType.SLOW_FALLING,
                            slowFallTicks, 0);
                }
            }
        }, Math.max(1L, slowFallTicks));

        Location c = player.getLocation();
        player.getWorld().playSound(c, Sound.ENTITY_PANDA_SNEEZE, 0.8f, 1.4f);
        ParticleUtil.spawnGroundBurst(Particle.CLOUD, c, 0.6, 10, 0.05);
        ParticleUtil.spawnTrail(Particle.CHERRY_LEAVES, c.clone().add(0, 1.0, 0), 6, 0.4);
        ParticleUtil.spawn(Particle.SWEEP_ATTACK, c.clone().add(0, 0.6, 0), 1, 0, 0, 0, 0.0);
    }

    @Override
    public long getCooldownTicks(Player player) {
        // Nv6 "Cauda Ágil": faster recharge.
        if (levelOf(player) >= 6) {
            return (long) (cooldownTicks * 0.7);
        }
        return cooldownTicks;
    }

    @Override
    public void onRemove(Player player) {
        UUID playerId = player.getUniqueId();
        if (speedActivations.remove(playerId) != null) {
            AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id + ".speed");
        }
        if (slowFallActivations.remove(playerId) != null) {
            EffectUtil.clearTemporary(player, PotionEffectType.SLOW_FALLING, slowFallTicks, 0);
        }
    }
}
