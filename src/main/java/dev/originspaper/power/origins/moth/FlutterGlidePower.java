package dev.originspaper.power.origins.moth;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

/** Asas Trêmulas + Voo Gracioso: a permanent gentle, controllable descent with a magic trail. */
public class FlutterGlidePower extends AbstractPower {

    public FlutterGlidePower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        // Trigger on any descent. The 40-tick (2s) effect overlaps the 1s tick so it stays smooth.
        if (!GroundUtil.isOnGround(player) && player.getVelocity().getY() < 0) {
            EffectUtil.apply(player, PotionEffectType.SLOW_FALLING, 40, 0);
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
