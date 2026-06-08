package dev.originspaper.power.shared;

import dev.originspaper.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

/** Prevents using a shield (blocks the raise-shield interaction). */
public class NoShieldPower extends AbstractPower {

    public NoShieldPower(String id) {
        super(id);
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() != null && e.getItem().getType() == Material.SHIELD) {
            e.setCancelled(true);
            p.sendActionBar(TextUtil.msg("§cVocê não consegue usar escudos."));
        }
    }
}
