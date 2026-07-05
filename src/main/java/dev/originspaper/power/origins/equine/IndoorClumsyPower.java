package dev.originspaper.power.origins.equine;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.AttributeUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

/** Mild cramped-space penalty; tall caves and ordinary rooms remain comfortable. */
public class IndoorClumsyPower extends AbstractPower {

    public IndoorClumsyPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        boolean cramped = !GallopPower.hasHeadroom(player);
        AttributeUtil.sync(player, Attribute.MOVEMENT_SPEED, id, cramped ? -0.08 : 0.0,
                AttributeModifier.Operation.ADD_SCALAR);
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id);
    }
}
