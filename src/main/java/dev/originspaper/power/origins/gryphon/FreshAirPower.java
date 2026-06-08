package dev.originspaper.power.origins.gryphon;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.TextUtil;
import org.bukkit.event.player.PlayerBedEnterEvent;

/** Needs fresh, high air: cannot sleep below a minimum altitude. */
public class FreshAirPower extends AbstractPower {

    private static final int MIN_Y = 86;

    public FreshAirPower(String id) {
        super(id);
    }

    @Override
    public void onBedEnter(PlayerBedEnterEvent e) {
        if (e.getBed().getY() < MIN_Y) {
            e.setCancelled(true);
            e.getPlayer().sendActionBar(TextUtil.msg("§cO ar aqui é denso demais. Durma a 86 blocos ou mais."));
        }
    }
}
