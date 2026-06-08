package dev.originspaper.power.origins.bat;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.GroundUtil;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Natural glide (slow falling) when falling. */
public class BatGlidePower extends AbstractPower {

    public BatGlidePower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (!GroundUtil.isOnGround(player) && player.getVelocity().getY() < -0.1) {
            EffectUtil.apply(player, PotionEffectType.SLOW_FALLING, 20, 0);
        }
    }
}
