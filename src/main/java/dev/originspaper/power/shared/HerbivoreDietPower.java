package dev.originspaper.power.shared;

import dev.originspaper.util.FoodUtil;
import dev.originspaper.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/** Cannot eat meat. */
public class HerbivoreDietPower extends AbstractPower {

    private final String denyMessage;

    public HerbivoreDietPower(String id, String denyMessage) {
        super(id);
        this.denyMessage = denyMessage;
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        Material m = e.getItem().getType();
        if (!FoodUtil.isMeat(m)) {
            return;
        }
        e.setCancelled(true);
        Player p = e.getPlayer();
        if (denyMessage != null) {
            p.sendActionBar(TextUtil.msg(denyMessage));
        }
    }
}
