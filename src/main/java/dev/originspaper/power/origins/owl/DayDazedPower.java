package dev.originspaper.power.origins.owl;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Dazzled by daylight: weakened when standing in bright sky light. */
public class DayDazedPower extends AbstractPower {

    public DayDazedPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (player.getLocation().getBlock().getLightFromSky() > 11
                && !dev.originspaper.power.shared.NightTimeEffectPower.isNight(player)) {
            EffectUtil.apply(player, PotionEffectType.WEAKNESS, 60, 0);
            EffectUtil.apply(player, PotionEffectType.MINING_FATIGUE, 60, 0);
            if (plugin().tick() % 2 == 0) { // smouldering under the sun
                Location loc = player.getLocation().add(0, 1.0, 0);
                ParticleUtil.spawnTrail(Particle.ASH, loc, 4, 0.4);
                ParticleUtil.spawnTrail(Particle.LARGE_SMOKE, loc, 1, 0.3);
            }
        }
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.WEAKNESS);
        EffectUtil.clear(player, PotionEffectType.MINING_FATIGUE);
    }
}
