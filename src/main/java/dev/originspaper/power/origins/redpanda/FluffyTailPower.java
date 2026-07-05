package dev.originspaper.power.origins.redpanda;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * Active skill: a fluffy burst of momentum — Speed II plus Slow Falling, made for tree-to-tree
 * hops, escapes and fall recovery. Pure mobility: no damage, no claim concerns.
 */
public class FluffyTailPower extends AbstractPower implements ActivePowerType {

    private final long cooldownTicks;
    private final int speedTicks;
    private final int slowFallTicks;

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
        EffectUtil.apply(player, PotionEffectType.SPEED, speedTicks, 1);       // Speed II
        EffectUtil.apply(player, PotionEffectType.SLOW_FALLING, slowFallTicks, 0);

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
        EffectUtil.clear(player, PotionEffectType.SPEED);
        EffectUtil.clear(player, PotionEffectType.SLOW_FALLING);
    }
}
