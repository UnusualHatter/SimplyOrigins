package dev.originspaper.power.origins.wolf;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.FoodUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/** Eating meat restores an extra half-heart. */
public class CarnivoresBitePower extends AbstractPower {

    public CarnivoresBitePower(String id) {
        super(id);
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (!FoodUtil.isMeat(e.getItem().getType())) {
            return;
        }
        Player player = e.getPlayer();
        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            double max = player.getAttribute(Attribute.MAX_HEALTH).getValue();
            player.setHealth(Math.min(max, player.getHealth() + 2.0));
        }, 1L);
    }
}
