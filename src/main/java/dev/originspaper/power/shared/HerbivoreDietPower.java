package dev.originspaper.power.shared;

import dev.originspaper.util.FoodUtil;
import dev.originspaper.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/** Cannot eat meat or fish — the mirror of {@link CarnivoreDietPower}. */
public class HerbivoreDietPower extends AbstractPower {

    private final String denyMessage;

    public HerbivoreDietPower(String id, String denyMessage) {
        super(id);
        this.denyMessage = denyMessage;
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        Material m = e.getItem().getType();
        if (!m.isEdible() || !FoodUtil.isMeat(m)) {
            return;
        }
        e.setCancelled(true);
        if (denyMessage != null) {
            e.getPlayer().sendActionBar(TextUtil.msg(denyMessage));
        }
    }
}
