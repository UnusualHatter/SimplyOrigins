package dev.originspaper.power.origins.bat;

import dev.originspaper.listener.PlacedBlockTracker;
import dev.originspaper.power.shared.NightTimeEffectPower;
import dev.originspaper.power.shared.ProgressionPower;
import dev.originspaper.util.GroundUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Bat progression. XP: glide through the air, mine in the dark, hunt at night.
 * Milestones: Nv3 recover a heart, Nv6 faster in deep caves, Nv10 faster in the dark of night.
 */
public class BatProgressionPower extends ProgressionPower {

    private static final String HP_KEY = "bat:prog_hp";
    private static final String CAVE_KEY = "bat:prog_cave";
    private static final String NIGHT_KEY = "bat:prog_night";

    public BatProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        if (!GroundUtil.isOnGround(e.getPlayer())) {
            accrueDistance(e.getPlayer(), e, 30.0, 5); // "Planar pelas cavernas"
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getLightFromSky() == 0 && !PlacedBlockTracker.isPlayerPlaced(e.getBlock())) {
            award(e.getPlayer(), 6); // "Minerar no escuro"
        }
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer != null && e.getEntity() instanceof Monster && NightTimeEffectPower.isNight(killer)) {
            award(killer, 10); // "Caçar à noite"
        }
    }

    @Override
    public void onTick(Player player) {
        int level = level(player);
        attr(player, Attribute.MAX_HEALTH, HP_KEY,
                level >= 3 ? 2.0 : 0.0, AttributeModifier.Operation.ADD_NUMBER);

        boolean deepCave = player.getLocation().getY() < 60
                && player.getLocation().getBlock().getLightFromSky() == 0;
        attr(player, Attribute.MOVEMENT_SPEED, CAVE_KEY,
                level >= 6 && deepCave ? 0.05 : 0.0, AttributeModifier.Operation.ADD_SCALAR);

        attr(player, Attribute.MOVEMENT_SPEED, NIGHT_KEY,
                level >= 10 && NightTimeEffectPower.isNight(player) ? 0.07 : 0.0,
                AttributeModifier.Operation.ADD_SCALAR);
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.MAX_HEALTH, HP_KEY);
        clearAttr(player, Attribute.MOVEMENT_SPEED, CAVE_KEY);
        clearAttr(player, Attribute.MOVEMENT_SPEED, NIGHT_KEY);
        super.onRemove(player);
    }
}
