package dev.originspaper.power.origins.skunk;

import dev.originspaper.power.shared.NightTimeEffectPower;
import dev.originspaper.power.shared.ProgressionPower;
import dev.originspaper.power.shared.TrashGourmetPower;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/** Skunk XP: night roaming, questionable meals and spray hits awarded by StinkSprayPower. */
public class SkunkProgressionPower extends ProgressionPower {

    private static final String HP_KEY = "skunk:prog_hp";

    public SkunkProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        if (NightTimeEffectPower.isNight(e.getPlayer())) {
            accrueDistance(e.getPlayer(), e, 35.0, 4);
        }
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (TrashGourmetPower.isTrashFood(e.getItem().getType())) {
            award(e.getPlayer(), 6);
        }
    }

    @Override
    public void onTick(Player player) {
        attr(player, Attribute.MAX_HEALTH, HP_KEY,
                level(player) >= 3 ? 2.0 : 0.0, AttributeModifier.Operation.ADD_NUMBER);
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.MAX_HEALTH, HP_KEY);
        super.onRemove(player);
    }
}
