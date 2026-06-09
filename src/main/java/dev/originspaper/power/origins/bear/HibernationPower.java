package dev.originspaper.power.origins.bear;

import dev.originspaper.power.shared.ActiveBuffPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

/** Hibernation: a primal red aura on activation, a slow frost-and-cloud haze while buffed. */
public class HibernationPower extends ActiveBuffPower {

    private static final Particle.DustOptions DARK_RED =
            ParticleUtil.dust(Color.fromRGB(120, 0, 0), 1.6f);

    public HibernationPower(String id, long cooldownTicks, Sound sound, PotionEffect... effects) {
        super(id, cooldownTicks, sound, effects);
    }

    @Override
    public void onActivate(Player player) {
        super.onActivate(player);
        Location c = player.getLocation().add(0, 1.0, 0);
        ParticleUtil.spawnRing(Particle.DUST, c, 1.2, 18, 0.0, DARK_RED);
        // ENTITY_EFFECT needs a Color on this build; pass a dark tint for the primal-bear look.
        ParticleUtil.spawnTrail(Particle.ENTITY_EFFECT, c, 12, 0.5, Color.fromRGB(80, 0, 0));
    }

    @Override
    public void onTick(Player player) {
        if (!isBuffActive(player)) {
            return;
        }
        Location c = player.getLocation().add(0, 1.0, 0);
        ParticleUtil.spawnTrail(Particle.SNOWFLAKE, c, 4, 0.5);
        ParticleUtil.spawnTrail(Particle.CLOUD, c, 2, 0.4);
    }
}
