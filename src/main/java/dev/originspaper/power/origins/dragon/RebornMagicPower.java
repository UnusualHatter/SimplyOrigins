package dev.originspaper.power.origins.dragon;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Empowered while in The End: grants Regeneration. */
public class RebornMagicPower extends AbstractPower {

    public RebornMagicPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (player.getWorld().getEnvironment() == Environment.THE_END) {
            EffectUtil.ensure(player, PotionEffectType.REGENERATION, 0);
            if (plugin().tick() % 2 == 0) { // empowered End aura
                Location loc = player.getLocation().add(0, 1.0, 0);
                ParticleUtil.spawnTrail(Particle.DRAGON_BREATH, loc, 2, 0.4);
                ParticleUtil.spawnTrail(Particle.PORTAL, loc, 3, 0.4);
            }
        } else {
            EffectUtil.clear(player, PotionEffectType.REGENERATION);
        }
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.REGENERATION);
    }
}
