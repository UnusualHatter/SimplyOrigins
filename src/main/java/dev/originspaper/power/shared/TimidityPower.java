package dev.originspaper.power.shared;

import dev.originspaper.util.AttributeUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

/** Timid: weakened when low on health. */
public class TimidityPower extends AbstractPower {

    public TimidityPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        var maxHealth = player.getAttribute(Attribute.MAX_HEALTH);
        double max = maxHealth == null ? 20.0 : maxHealth.getValue();
        AttributeUtil.sync(player, Attribute.ATTACK_DAMAGE, id,
                max > 0 && player.getHealth() / max <= 0.30 ? -4.0 : 0.0,
                AttributeModifier.Operation.ADD_NUMBER);
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, Attribute.ATTACK_DAMAGE, id);
    }
}
