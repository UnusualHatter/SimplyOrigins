package dev.originspaper.power.origins.otter;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.AttributeUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

/** Faster swimming: a movement-speed boost that is only present while submerged. */
public class FinsPower extends AbstractPower {

    public FinsPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (player.isUnderWater()) {
            AttributeUtil.set(player, Attribute.MOVEMENT_SPEED, id, 0.4,
                    AttributeModifier.Operation.ADD_SCALAR);
            Location loc = player.getLocation().add(0, 0.5, 0);
            ParticleUtil.spawnTrail(Particle.BUBBLE, loc, 4, 0.3);
            ParticleUtil.spawnTrail(Particle.BUBBLE_COLUMN_UP, loc, 2, 0.2);
            if (plugin().tick() % 3 == 0) { // occasional dolphin streak while swimming
                ParticleUtil.spawnTrail(Particle.DOLPHIN, loc, 2, 0.2);
                ParticleUtil.spawnTrail(Particle.SPLASH, player.getLocation(), 3, 0.2);
            }
        } else {
            AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id);
        }
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id);
    }
}
