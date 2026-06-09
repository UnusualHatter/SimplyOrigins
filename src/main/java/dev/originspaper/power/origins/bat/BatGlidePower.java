package dev.originspaper.power.origins.bat;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

/** Natural glide: floats the instant the bat leaves the ground, even on a jump. */
public class BatGlidePower extends AbstractPower {

    public BatGlidePower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        // Apply the moment the bat is airborne (jump or fall). onMove fires every tick while
        // airborne, so the glide engages within a tick — no waiting on the 1s global tick, and
        // it is already active by the time the arc starts descending.
        if (!GroundUtil.isOnGround(player)) {
            EffectUtil.apply(player, PotionEffectType.SLOW_FALLING, 40, 0);
        }
    }

    @Override
    public void onTick(Player player) {
        // Cosmetic shimmer while drifting down.
        if (!GroundUtil.isOnGround(player) && player.getVelocity().getY() < 0) {
            Location loc = player.getLocation();
            ParticleUtil.spawnTrail(Particle.END_ROD, loc, 2, 0.2);
            ParticleUtil.spawnTrail(Particle.SMOKE, loc, 1, 0.2);
        }
    }
}
