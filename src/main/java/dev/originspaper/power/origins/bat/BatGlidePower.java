package dev.originspaper.power.origins.bat;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Natural glide (slow falling) when falling, leaving a faint shimmer trail. */
public class BatGlidePower extends AbstractPower {

    public BatGlidePower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        // Trigger on any descent, not just fast falls: once slow falling is active it caps fall
        // speed near -0.07, which would fail a stricter threshold and make the glide stutter.
        // The 40-tick (2s) effect overlaps the 1s tick cadence so it stays continuous.
        if (!GroundUtil.isOnGround(player) && player.getVelocity().getY() < 0) {
            EffectUtil.apply(player, PotionEffectType.SLOW_FALLING, 40, 0);
            Location loc = player.getLocation();
            ParticleUtil.spawnTrail(Particle.END_ROD, loc, 2, 0.2);
            ParticleUtil.spawnTrail(Particle.SMOKE, loc, 1, 0.2);
        }
    }
}
