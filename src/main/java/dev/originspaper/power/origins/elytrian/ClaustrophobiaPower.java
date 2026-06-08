package dev.originspaper.power.origins.elytrian;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Weakened when trapped under a low ceiling (claustrophobia). */
public class ClaustrophobiaPower extends AbstractPower {

    public ClaustrophobiaPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (plugin().tick() % 2 != 0) {
            return; // roughly every 40 ticks
        }
        Location loc = player.getLocation();
        for (int dy = 1; dy <= 3; dy++) {
            if (loc.clone().add(0, dy, 0).getBlock().getType().isSolid()) {
                EffectUtil.apply(player, PotionEffectType.WEAKNESS, 100, 0);
                EffectUtil.apply(player, PotionEffectType.SLOWNESS, 100, 0);
                return;
            }
        }
    }
}
