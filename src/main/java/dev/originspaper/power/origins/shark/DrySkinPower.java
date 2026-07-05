package dev.originspaper.power.origins.shark;

import dev.originspaper.api.TickCadence;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dry skin: minutes-long grace before penalties, so land trips are inconvenient — not miserable.
 * Weakness after the warning window, Slowness on top after the penalty window; any water or rain
 * contact resets the clock. Checked every 5s ({@code OriginsPaper#tick} advances 1/s).
 */
public class DrySkinPower extends AbstractPower {

    /** Effect duration comfortably covering the 5s cadence gap (100 ticks) plus margin. */
    private static final int EFFECT_TICKS = 140;

    private final long warnSeconds;
    private final long penaltySeconds;

    /** Last tick (1/s) the shark was wet, per player. */
    private final Map<UUID, Long> lastWet = new ConcurrentHashMap<>();

    public DrySkinPower(String id, long warnSeconds, long penaltySeconds) {
        super(id);
        this.warnSeconds = warnSeconds;
        this.penaltySeconds = penaltySeconds;
    }

    @Override
    public TickCadence getTickCadence() {
        return TickCadence.EVERY_5_SECONDS;
    }

    @Override
    public void onTick(Player player) {
        long now = plugin().tick();
        if (BloodSensePower.isWet(player)) {
            lastWet.put(player.getUniqueId(), now);
            return;
        }
        long dry = now - lastWet.computeIfAbsent(player.getUniqueId(), k -> now);
        Location loc = player.getLocation().add(0, 1.0, 0);
        if (dry > penaltySeconds) {
            EffectUtil.apply(player, PotionEffectType.WEAKNESS, EFFECT_TICKS, 0);
            EffectUtil.apply(player, PotionEffectType.SLOWNESS, EFFECT_TICKS, 0);
            ParticleUtil.spawnTrail(Particle.ASH, loc, 5, 0.4); // parched
        } else if (dry > warnSeconds) {
            EffectUtil.apply(player, PotionEffectType.WEAKNESS, EFFECT_TICKS, 0);
            ParticleUtil.spawnTrail(Particle.FALLING_DUST, loc, 3, 0.3,
                    org.bukkit.Material.SAND.createBlockData());
        } else if (dry > warnSeconds / 2) {
            ParticleUtil.spawnTrail(Particle.DRIPPING_WATER, loc, 3, 0.3); // starting to dry out
        }
    }

    @Override
    public void onRemove(Player player) {
        lastWet.remove(player.getUniqueId());
        EffectUtil.clear(player, PotionEffectType.WEAKNESS);
        EffectUtil.clear(player, PotionEffectType.SLOWNESS);
    }
}
