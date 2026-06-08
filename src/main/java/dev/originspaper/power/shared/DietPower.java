package dev.originspaper.power.shared;

import dev.originspaper.util.FoodUtil;
import dev.originspaper.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Set;

/** Restricts what the player may eat. Optionally allows all meat plus a set of extra materials. */
public class DietPower extends AbstractPower {

    private final boolean allowMeat;
    private final Set<Material> extra;
    private final String denyMessage;

    public DietPower(String id, boolean allowMeat, Set<Material> extra, String denyMessage) {
        super(id);
        this.allowMeat = allowMeat;
        this.extra = extra;
        this.denyMessage = denyMessage;
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        Material m = e.getItem().getType();
        if (!m.isEdible()) {
            return;
        }
        if (allowMeat && FoodUtil.isMeat(m)) {
            return;
        }
        if (extra.contains(m)) {
            return;
        }
        e.setCancelled(true);
        Player p = e.getPlayer();
        if (denyMessage != null) {
            p.sendActionBar(TextUtil.msg(denyMessage));
        }
    }
}
