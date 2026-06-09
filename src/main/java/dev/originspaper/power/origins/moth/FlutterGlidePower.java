package dev.originspaper.power.origins.moth;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

/** Asas Trêmulas + Voo Gracioso: glides the instant the moth leaves the ground, even on a jump. */
public class FlutterGlidePower extends AbstractPower {

    public FlutterGlidePower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        // Engage the instant the moth is airborne (jump or fall). onMove fires every tick while
        // airborne, so the glide is felt immediately — no waiting on the 1s global tick.
        if (!GroundUtil.isOnGround(player)) {
            EffectUtil.apply(player, PotionEffectType.SLOW_FALLING, 40, 0);
        }
    }

    @Override
    public void onTick(Player player) {
        // Cosmetic trail while drifting down.
        if (!GroundUtil.isOnGround(player) && player.getVelocity().getY() < 0) {
            Location loc = player.getLocation();
            ParticleUtil.spawnTrail(Particle.END_ROD, loc, 2, 0.2);
            ParticleUtil.spawnTrail(Particle.CHERRY_LEAVES, loc, 1, 0.2);
        }
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        // Delicate wings drift down softly — never splat.
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
        }
    }
}
