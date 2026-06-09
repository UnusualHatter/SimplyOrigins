package dev.originspaper.power.origins.bat;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Faster movement and haste in caves (Y < 64 and low sky light). */
public class CaveMobilityPower extends AbstractPower {

    public CaveMobilityPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        // onTick runs once per second; the 60-tick (3s) effects overlap to stay continuous in caves.
        if (player.getLocation().getY() < 64 && player.getLocation().getBlock().getLightFromSky() == 0) {
            EffectUtil.apply(player, PotionEffectType.SPEED, 60, 0);
            EffectUtil.apply(player, PotionEffectType.HASTE, 60, 0);
            if (plugin().tick() % 2 == 0) { // subtle subterranean aura
                Location loc = player.getLocation().add(0, 1.0, 0);
                ParticleUtil.spawnTrail(Particle.SCULK_SOUL, loc, 2, 0.4);
                ParticleUtil.spawnTrail(Particle.PORTAL, loc, 3, 0.4);
            }
        }
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.SPEED);
        EffectUtil.clear(player, PotionEffectType.HASTE);
    }
}
