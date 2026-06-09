package dev.originspaper.power.origins.otter;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Punished after spending out of water/rain for too long. The window is measured in seconds
 * because it is checked against {@link dev.originspaper.OriginsPaper#tick()}, which advances once
 * per 20-tick cycle (i.e. once per second). As the otter dries out it shows escalating warning
 * particles before the slowness/weakness penalty kicks in.
 */
public class WaterDependencyPower extends AbstractPower {

    private final ConcurrentHashMap<UUID, Long> lastWetTime = new ConcurrentHashMap<>();
    private final long maxDrySeconds;

    public WaterDependencyPower(String id, long maxDrySeconds) {
        super(id);
        this.maxDrySeconds = maxDrySeconds;
    }

    @Override
    public void onTick(Player player) {
        boolean isWet = player.isInWater()
                || (player.getWorld().hasStorm() && player.getLocation().getBlock().getLightFromSky() == 15);
        long now = plugin().tick();

        if (isWet) {
            lastWetTime.put(player.getUniqueId(), now);
            return;
        }

        long lastWet = lastWetTime.getOrDefault(player.getUniqueId(), now);
        long dry = now - lastWet;
        Location loc = player.getLocation().add(0, 1.0, 0);

        if (dry > maxDrySeconds) {
            EffectUtil.apply(player, PotionEffectType.SLOWNESS, 40, 0);
            EffectUtil.apply(player, PotionEffectType.WEAKNESS, 40, 0);
            ParticleUtil.spawnTrail(Particle.ASH, loc, 5, 0.4); // Stage 3: parched
        } else if (dry > maxDrySeconds * 3 / 4) {
            if (now % 2 == 0) { // Stage 2: drying — sand crumbling off
                ParticleUtil.spawnTrail(Particle.FALLING_DUST, loc, 3, 0.3, Material.SAND.createBlockData());
            }
        } else if (dry > maxDrySeconds / 2) {
            if (now % 3 == 0) { // Stage 1: thirst — losing moisture
                ParticleUtil.spawnTrail(Particle.DRIPPING_WATER, loc, 3, 0.3);
            }
        }
    }

    @Override
    public void onRemove(Player player) {
        lastWetTime.remove(player.getUniqueId());
        EffectUtil.clear(player, PotionEffectType.SLOWNESS);
        EffectUtil.clear(player, PotionEffectType.WEAKNESS);
    }
}
