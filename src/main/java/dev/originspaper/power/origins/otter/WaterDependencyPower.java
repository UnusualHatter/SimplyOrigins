package dev.originspaper.power.origins.otter;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Punished after spending out of water/rain for too long. */
public class WaterDependencyPower extends AbstractPower {

    private final ConcurrentHashMap<UUID, Long> lastWetTime = new ConcurrentHashMap<>();
    private final long maxDryTicks;

    public WaterDependencyPower(String id, long maxDryTicks) {
        super(id);
        this.maxDryTicks = maxDryTicks;
    }

    @Override
    public void onTick(Player player) {
        boolean isWet = player.isInWater() || (player.getWorld().hasStorm() && player.getLocation().getBlock().getLightFromSky() == 15);
        long currentTick = plugin().tick();
        
        if (isWet) {
            lastWetTime.put(player.getUniqueId(), currentTick);
            return;
        }
        
        long lastWet = lastWetTime.getOrDefault(player.getUniqueId(), currentTick);
        if (currentTick - lastWet > maxDryTicks) {
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
