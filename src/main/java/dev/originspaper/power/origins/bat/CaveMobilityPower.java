package dev.originspaper.power.origins.bat;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Faster movement and haste in caves (Y < 64 and low sky light). */
public class CaveMobilityPower extends AbstractPower {

    public CaveMobilityPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (plugin().tick() % 20 != 0) return;
        
        if (player.getLocation().getY() < 64 && player.getLocation().getBlock().getLightFromSky() == 0) {
            EffectUtil.apply(player, PotionEffectType.SPEED, 60, 0);
            EffectUtil.apply(player, PotionEffectType.HASTE, 60, 0);
        }
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.SPEED);
        EffectUtil.clear(player, PotionEffectType.HASTE);
    }
}
