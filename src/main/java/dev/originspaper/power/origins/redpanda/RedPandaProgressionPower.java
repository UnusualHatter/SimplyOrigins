package dev.originspaper.power.origins.redpanda;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Red panda progression. XP: roam forest blocks, climb (granted by {@link ClimberPower}), eat
 * berries. Milestones: Nv3 +1 heart, Nv6 faster Fluffy Tail (in {@link FluffyTailPower}),
 * Nv10 Speed II on forest blocks (in {@link TreeAcrobatPower}).
 */
public class RedPandaProgressionPower extends ProgressionPower {

    private static final String HP_KEY = "red_panda:prog_hp";

    public RedPandaProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Material below = e.getPlayer().getLocation().clone().subtract(0, 0.1, 0).getBlock().getType();
        if (TreeAcrobatPower.isForestBlock(below)) {
            accrueDistance(e.getPlayer(), e, 30.0, 5); // "Percorrer florestas e copas"
        }
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        Material m = e.getItem().getType();
        if (m == Material.SWEET_BERRIES || m == Material.GLOW_BERRIES) {
            award(e.getPlayer(), 6); // "Comer frutinhas"
        }
    }

    @Override
    public void onTick(Player player) {
        int level = level(player);
        attr(player, Attribute.MAX_HEALTH, HP_KEY,
                level >= 3 ? 2.0 : 0.0, AttributeModifier.Operation.ADD_NUMBER);
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.MAX_HEALTH, HP_KEY);
        super.onRemove(player);
    }
}
