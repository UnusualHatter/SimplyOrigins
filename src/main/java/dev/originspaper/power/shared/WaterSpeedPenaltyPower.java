package dev.originspaper.power.shared;

import dev.originspaper.util.AttributeUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

/** Poor swimmer: a movement-speed penalty that only applies while in water. */
public class WaterSpeedPenaltyPower extends AbstractPower {

    private final double scalar; // negative, e.g. -0.15 = 15% slower

    public WaterSpeedPenaltyPower(String id, double scalar) {
        super(id);
        this.scalar = scalar;
    }

    @Override
    public void onTick(Player player) {
        AttributeUtil.sync(player, Attribute.MOVEMENT_SPEED, id,
                player.isInWater() ? scalar : 0.0, AttributeModifier.Operation.ADD_SCALAR);
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id);
    }
}
