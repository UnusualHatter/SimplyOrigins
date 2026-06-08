package dev.originspaper.power.origins.otter;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Clear sight and dolphin's grace while fully submerged. */
public class WetEyesPower extends AbstractPower {

    public WetEyesPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (player.isUnderWater()) {
            EffectUtil.ensure(player, PotionEffectType.NIGHT_VISION, 0);
            EffectUtil.ensure(player, PotionEffectType.DOLPHINS_GRACE, 0);
        } else {
            EffectUtil.clear(player, PotionEffectType.NIGHT_VISION);
            EffectUtil.clear(player, PotionEffectType.DOLPHINS_GRACE);
        }
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.NIGHT_VISION);
        EffectUtil.clear(player, PotionEffectType.DOLPHINS_GRACE);
    }
}
