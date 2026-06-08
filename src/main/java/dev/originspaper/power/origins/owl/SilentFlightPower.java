package dev.originspaper.power.origins.owl;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.AttributeUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

/** Reduces mob detection range drastically while gliding. */
public class SilentFlightPower extends AbstractPower {

    public SilentFlightPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        // -75% detection range while gliding
        if (player.isGliding()) {
            AttributeUtil.set(player, Attribute.FOLLOW_RANGE, getId(), -0.75, AttributeModifier.Operation.ADD_SCALAR);
        } else {
            AttributeUtil.clear(player, Attribute.FOLLOW_RANGE, getId());
        }
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, Attribute.FOLLOW_RANGE, getId());
    }
}
