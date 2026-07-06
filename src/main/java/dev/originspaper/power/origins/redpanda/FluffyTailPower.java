package dev.originspaper.power.origins.redpanda;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.PerfStats;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * Active skill: a soothing tail flourish — Regeneration I for the red panda and every nearby
 * player. Support tool, zero damage; allies inside claims the panda isn't trusted in are skipped
 * (same rule as any other player-affecting power).
 */
public class FluffyTailPower extends AbstractPower implements ActivePowerType {

    private static final double REGEN_RADIUS = 6.0;

    private final long cooldownTicks;
    private final int regenTicks;

    public FluffyTailPower(String id, long cooldownTicks, int regenTicks) {
        super(id);
        this.cooldownTicks = cooldownTicks;
        this.regenTicks = regenTicks;
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onActivate(Player player) {
        EffectUtil.apply(player, PotionEffectType.REGENERATION, regenTicks, 0);

        PerfStats.entityScan();
        for (Player ally : player.getWorld().getNearbyPlayers(player.getLocation(), REGEN_RADIUS)) {
            if (ally.equals(player) || !claims().canAffectPlayer(player, ally)) {
                continue;
            }
            EffectUtil.apply(ally, PotionEffectType.REGENERATION, regenTicks, 0);
            ParticleUtil.spawnTrail(Particle.HEART, ally.getLocation().add(0, 1.2, 0), 2, 0.3);
        }

        Location c = player.getLocation();
        player.getWorld().playSound(c, Sound.ENTITY_PANDA_SNEEZE, 0.8f, 1.4f);
        ParticleUtil.spawnGroundBurst(Particle.HEART, c, 0.8, 6, 0.1);
        ParticleUtil.spawnTrail(Particle.CHERRY_LEAVES, c.clone().add(0, 1.0, 0), 8, 0.5);
        ParticleUtil.spawnRing(Particle.HAPPY_VILLAGER, c.clone().add(0, 0.3, 0), 1.5, 14, 0.0);
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
        EffectUtil.clearTemporary(player, PotionEffectType.REGENERATION, regenTicks, 0);
    }
}
