package dev.originspaper.power.origins.bear;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/** Clumsy paws cannot wield swords or axes effectively. */
public class CumbersomeClawsPower extends AbstractPower {

    public CumbersomeClawsPower(String id) {
        super(id);
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        ItemStack hand = player.getInventory().getItemInMainHand();
        String name = hand.getType().name();
        if (name.endsWith("_SWORD") || name.endsWith("_AXE")) {
            e.setCancelled(true);
            player.sendActionBar(TextUtil.msg("§cSuas garras não conseguem manejar esta arma."));
        }
    }
}
