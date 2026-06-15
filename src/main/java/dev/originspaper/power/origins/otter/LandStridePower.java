package dev.originspaper.power.origins.otter;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * Base land mobility so the otter isn't dead weight away from water: a steady Speed buff whenever
 * it is out of the water (in water, Fins already handles swimming). Re-applied each tick as a short,
 * hidden effect so it fades on its own if the power is removed.
 */
public class LandStridePower extends AbstractPower {

    private final int amplifier;

    public LandStridePower(String id, int amplifier) {
        super(id);
        this.amplifier = amplifier;
    }

    @Override
    public void onTick(Player player) {
        if (player.isInWater()) {
            return;
        }
        EffectUtil.apply(player, PotionEffectType.SPEED, 40, amplifier);
    }
}
