package dev.originspaper.power.origins.wolf;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.power.shared.NightTimeEffectPower;
import dev.originspaper.util.AttributeUtil;
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
        double amount = NightTimeEffectPower.isNight(player) ? 0.20 : 0.15;
        AttributeUtil.set(player, Attribute.MOVEMENT_SPEED, id, amount,
                AttributeModifier.Operation.ADD_SCALAR);
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
