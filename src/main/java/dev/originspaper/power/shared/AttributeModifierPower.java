package dev.originspaper.power.shared;

import dev.originspaper.util.AttributeUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

/** Applies a single, constant attribute modifier for as long as the player has the origin. */
public class AttributeModifierPower extends AbstractPower {

    private final Attribute attribute;
    private final double amount;
    private final AttributeModifier.Operation operation;

    public AttributeModifierPower(String id, Attribute attribute, double amount) {
        this(id, attribute, amount, AttributeModifier.Operation.ADD_NUMBER);
    }

    public AttributeModifierPower(String id, Attribute attribute, double amount,
                                  AttributeModifier.Operation operation) {
        super(id);
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
    }

    @Override
    public void onApply(Player player) {
        AttributeUtil.set(player, attribute, id, amount, operation);
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, attribute, id);
    }
}
