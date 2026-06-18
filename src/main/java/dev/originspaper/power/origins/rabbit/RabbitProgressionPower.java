package dev.originspaper.power.origins.rabbit;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Rabbit progression. XP: harvest mature crops, eat carrots, hop around.
 * Milestones: Nv3 higher jump, Nv6 extra speed, Nv10 +1 heart.
 */
public class RabbitProgressionPower extends ProgressionPower {

    private static final String JUMP_KEY = "rabbit:prog_jump";
    private static final String SPEED_KEY = "rabbit:prog_speed";
    private static final String HP_KEY = "rabbit:prog_hp";

    public RabbitProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        // Only a fully-grown crop counts as a harvest.
        if (e.getBlock().getBlockData() instanceof Ageable age && age.getAge() == age.getMaximumAge()) {
            award(e.getPlayer(), 10); // "Colher plantações"
        }
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        Material type = e.getItem().getType();
        if (type == Material.CARROT || type == Material.GOLDEN_CARROT) {
            award(e.getPlayer(), 8); // "Comer cenouras"
        }
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        accrueDistance(e.getPlayer(), e, 30.0, 4); // "Saltitar pelo mundo"
    }

    @Override
    public void onTick(Player player) {
        int level = level(player);
        attr(player, Attribute.JUMP_STRENGTH, JUMP_KEY,
                level >= 3 ? 0.1 : 0.0, AttributeModifier.Operation.ADD_NUMBER);
        attr(player, Attribute.MOVEMENT_SPEED, SPEED_KEY,
                level >= 6 ? 0.05 : 0.0, AttributeModifier.Operation.ADD_SCALAR);
        attr(player, Attribute.MAX_HEALTH, HP_KEY,
                level >= 10 ? 2.0 : 0.0, AttributeModifier.Operation.ADD_NUMBER);
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.JUMP_STRENGTH, JUMP_KEY);
        clearAttr(player, Attribute.MOVEMENT_SPEED, SPEED_KEY);
        clearAttr(player, Attribute.MAX_HEALTH, HP_KEY);
        super.onRemove(player);
    }
}
