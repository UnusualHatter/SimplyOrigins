package dev.originspaper.power.origins.feline; // Fixed package syntax issue

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Gains speed when looking at / chasing a hostile mob. */
public class PredatorInstinctPower extends AbstractPower {

    public PredatorInstinctPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (plugin().tick() % 10 != 0) {
            return;
        }
        Entity target = player.getTargetEntity(30);
        if (target instanceof Monster) {
            EffectUtil.apply(player, PotionEffectType.SPEED, 60, 0); // Speed I for 3 seconds
        }
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.SPEED);
    }
}
