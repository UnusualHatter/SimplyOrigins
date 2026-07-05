package dev.originspaper.power.origins.shark;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.AttributeUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

/**
 * Apex swim speed: the shark outpaces everything in the water — including the otter, whose niche
 * stays utility (mining, conduit, land stride), not raw pursuit. No land bonus whatsoever.
 */
public class ApexSwimmerPower extends AbstractPower {

    private static final double SWIM_SPEED_SCALAR = 0.5;

    public ApexSwimmerPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        boolean inWater = player.isInWater();
        AttributeUtil.sync(player, Attribute.MOVEMENT_SPEED, id,
                inWater ? SWIM_SPEED_SCALAR : 0.0, AttributeModifier.Operation.ADD_SCALAR);
        if (inWater && player.getVelocity().lengthSquared() > 0.02) {
            ParticleUtil.spawnTrail(Particle.BUBBLE, player.getLocation().add(0, 0.4, 0), 3, 0.3);
            if (plugin().tick() % 2 == 0) {
                ParticleUtil.spawnTrail(Particle.NAUTILUS, player.getLocation().add(0, 0.4, 0), 2, 0.3);
            }
        }
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id);
    }
}
