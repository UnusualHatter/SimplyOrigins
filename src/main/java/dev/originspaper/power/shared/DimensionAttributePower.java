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
        if (player.getWorld().getEnvironment() == environment) {
            AttributeUtil.set(player, attribute, id, amount, operation);
        } else {
            AttributeUtil.clear(player, attribute, id);
        }
        // A shrinking MAX_HEALTH modifier doesn't auto-clamp current health (unlike the one-time
        // apply/remove path, which goes through PlayerDataManager#clampHealth). Clamp down here
        // too, e.g. so a full-health Dragon entering the Nether doesn't end up above their new max.
        if (attribute == Attribute.MAX_HEALTH) {
            AttributeInstance maxHpAttr = player.getAttribute(Attribute.MAX_HEALTH);
            if (maxHpAttr != null && player.getHealth() > maxHpAttr.getValue()) {
                player.setHealth(maxHpAttr.getValue());
            }
        }
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, attribute, id);
    }
}
