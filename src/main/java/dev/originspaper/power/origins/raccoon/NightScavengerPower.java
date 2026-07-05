package dev.originspaper.power.origins.raccoon;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.power.shared.NightTimeEffectPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * Scavenger eyes: night vision whenever it is actually dim — at night or in low light (caves,
 * dense forests, interiors). Bright daylight stays normal, so it reads as a nocturnal animal
 * rather than permanent night vision.
 */
public class NightScavengerPower extends AbstractPower {

    private static final int LOW_LIGHT = 8;

    public NightScavengerPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        boolean dim = NightTimeEffectPower.isNight(player)
                || player.getLocation().getBlock().getLightLevel() < LOW_LIGHT;
        if (dim) {
            EffectUtil.ensure(player, PotionEffectType.NIGHT_VISION, 0);
        } else {
            EffectUtil.clear(player, PotionEffectType.NIGHT_VISION);
        }
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.NIGHT_VISION);
    }
}
