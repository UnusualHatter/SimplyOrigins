package dev.originspaper.power.origins.otter;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Punished after spending out of water/rain for too long. The window is measured in seconds
 * because it is checked against {@link dev.originspaper.OriginsPaper#tick()}, which advances once
 * per 20-tick cycle (i.e. once per second).
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
        if (now - lastWet > maxDrySeconds) {
            EffectUtil.apply(player, PotionEffectType.SLOWNESS, 40, 0);
            EffectUtil.apply(player, PotionEffectType.WEAKNESS, 40, 0);
        }
    }

    @Override
    public void onRemove(Player player) {
        lastWetTime.remove(player.getUniqueId());
        EffectUtil.clear(player, PotionEffectType.SLOWNESS);
        EffectUtil.clear(player, PotionEffectType.WEAKNESS);
    }
}
