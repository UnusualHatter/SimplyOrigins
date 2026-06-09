package dev.originspaper.power.origins.wolf;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.power.shared.NightTimeEffectPower;
import dev.originspaper.util.AttributeUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

/** Faster at night, still quick by day. */
public class DayNightSpeedPower extends AbstractPower {

    public DayNightSpeedPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        boolean night = NightTimeEffectPower.isNight(player);
        double amount = night ? 0.20 : 0.15;
        AttributeUtil.set(player, Attribute.MOVEMENT_SPEED, id, amount,
                AttributeModifier.Operation.ADD_SCALAR);
        if (night && plugin().tick() % 2 == 0
                && (player.isSprinting() || player.getVelocity().lengthSquared() > 0.01)) {
            Location loc = player.getLocation().add(0, 0.2, 0);
            ParticleUtil.spawnTrail(Particle.END_ROD, loc, 2, 0.2);
            ParticleUtil.spawnTrail(Particle.CRIT, loc, 2, 0.2);
        }
    }

    @Override
    public void onApply(Player player) {
        AttributeUtil.set(player, Attribute.MOVEMENT_SPEED, id, 0.15,
                AttributeModifier.Operation.ADD_SCALAR);
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id);
    }
}
