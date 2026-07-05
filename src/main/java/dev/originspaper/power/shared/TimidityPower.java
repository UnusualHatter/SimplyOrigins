package dev.originspaper.power.shared;

import dev.originspaper.util.EffectUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Timid: weakened when low on health. */
public class TimidityPower extends AbstractPower {

    public TimidityPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        double max = player.getAttribute(Attribute.MAX_HEALTH).getValue();
        if (max > 0 && player.getHealth() / max <= 0.30) {
            EffectUtil.apply(player, PotionEffectType.WEAKNESS, 40, 0);
        }
    }

    @Override
    public void onRemove(Player player) {
        player.removePotionEffect(PotionEffectType.WEAKNESS);
    }
}
