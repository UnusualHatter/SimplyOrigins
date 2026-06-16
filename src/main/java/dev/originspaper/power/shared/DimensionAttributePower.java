package dev.originspaper.power.shared;

import dev.originspaper.util.AttributeUtil;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

/** Applies an attribute modifier only while the player is in a given dimension. */
public class DimensionAttributePower extends AbstractPower {

    private final Environment environment;
    private final Attribute attribute;
    private final double amount;
    private final AttributeModifier.Operation operation;

    public DimensionAttributePower(String id, Environment environment, Attribute attribute, double amount) {
        this(id, environment, attribute, amount, AttributeModifier.Operation.ADD_NUMBER);
    }

    public DimensionAttributePower(String id, Environment environment, Attribute attribute, double amount,
                                    AttributeModifier.Operation operation) {
        super(id);
        this.environment = environment;
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
    }

    @Override
    public void onTick(Player player) {
        AttributeInstance inst = player.getAttribute(attribute);
        if (inst == null) {
            return;
        }
        boolean inDimension = player.getWorld().getEnvironment() == environment;
        boolean applied = inst.getModifier(AttributeUtil.key(id)) != null;
        if (inDimension == applied) {
            return; // already in the correct state — act only on transitions, no per-tick churn
        }
        if (inDimension) {
            AttributeUtil.set(player, attribute, id, amount, operation);
            // Clamp current health down once, on entry, if the new max is lower (e.g. a full-health
            // Dragon stepping into the Nether shouldn't sit above its reduced max).
            if (attribute == Attribute.MAX_HEALTH && player.getHealth() > inst.getValue()) {
                player.setHealth(inst.getValue());
            }
        } else {
            AttributeUtil.clear(player, attribute, id);
        }
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, attribute, id);
    }
}
