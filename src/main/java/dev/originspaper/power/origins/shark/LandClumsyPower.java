package dev.originspaper.power.origins.shark;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.AttributeUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

/**
 * Slightly slower out of water — a constant, mild inconvenience (not Slowness), so building and
 * mining stay playable while travel clearly favours the water.
 */
public class LandClumsyPower extends AbstractPower {

    private final double scalar; // negative, e.g. -0.08 = 8% slower on land

    public LandClumsyPower(String id, double scalar) {
        super(id);
        this.scalar = scalar;
    }

    @Override
    public void onTick(Player player) {
        AttributeUtil.sync(player, Attribute.MOVEMENT_SPEED, id,
                player.isInWater() ? 0.0 : scalar, AttributeModifier.Operation.ADD_SCALAR);
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id);
    }
}
