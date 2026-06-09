package dev.originspaper.power.origins.moth;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.power.shared.NightTimeEffectPower;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.concurrent.ThreadLocalRandom;

/** Cosmetic moth flair: wing-scale dust while walking, on hit, and a rare moonlit glow. */
public class MothVisualsPower extends AbstractPower {

    public MothVisualsPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        // Wing-scale dust while walking on the ground.
        if (GroundUtil.isOnGround(player) && player.getVelocity().lengthSquared() > 0.005
                && plugin().tick() % 2 == 0) {
            ParticleUtil.spawnTrail(Particle.WHITE_ASH, loc, 2, 0.3);
            ParticleUtil.spawnTrail(Particle.CHERRY_LEAVES, loc, 1, 0.3);
        }
        // Rare moonlit shimmer at night.
        if (NightTimeEffectPower.isNight(player) && ThreadLocalRandom.current().nextDouble() < 0.05) {
            ParticleUtil.spawnTrail(Particle.END_ROD, loc, 1, 0.3);
        }
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        Location loc = e.getEntity().getLocation().add(0, 1.0, 0);
        ParticleUtil.spawnTrail(Particle.WHITE_ASH, loc, 6, 0.4);
        ParticleUtil.spawnTrail(Particle.END_ROD, loc, 4, 0.3);
    }
}
