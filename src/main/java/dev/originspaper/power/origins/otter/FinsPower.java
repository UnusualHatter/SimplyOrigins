package dev.originspaper.power.origins.otter;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.AttributeUtil;
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
        } else {
            AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id);
        }
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id);
    }
}
