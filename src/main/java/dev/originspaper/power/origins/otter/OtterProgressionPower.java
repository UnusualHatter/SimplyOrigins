package dev.originspaper.power.origins.otter;

import dev.originspaper.listener.PlacedBlockTracker;
import dev.originspaper.power.shared.ProgressionPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Otter progression. XP: swim distance, mine submerged, hunt in water.
 * Milestones: Nv3 faster submerged mining, Nv6 conduit power while submerged, Nv10 faster swimming.
 */
public class OtterProgressionPower extends ProgressionPower {

    private static final String DIVE_KEY = "otter:prog_dive";
    private static final String CURRENT_KEY = "otter:prog_current";

    public OtterProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().isInWater()) {
            accrueDistance(e.getPlayer(), e, 25.0, 5); // "Nadar longas distâncias"
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().isInWater() && !PlacedBlockTracker.isPlayerPlaced(e.getBlock())) {
            award(e.getPlayer(), 8); // "Minerar submerso"
        }
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer != null && e.getEntity() instanceof Monster && killer.isInWater()) {
            award(killer, 15); // "Caçar na água"
        }
    }

    @Override
    public void onTick(Player player) {
        int level = level(player);
        // Nv3: faster mining underwater (stacks on the base Aqua Affinity).
        attr(player, Attribute.SUBMERGED_MINING_SPEED, DIVE_KEY,
                level >= 3 ? 0.5 : 0.0, AttributeModifier.Operation.ADD_SCALAR);

        // Nv6: conduit power while submerged (underwater vision + faster mining).
        if (level >= 6 && player.isUnderWater()) {
            EffectUtil.ensure(player, PotionEffectType.CONDUIT_POWER, 0);
        } else {
            EffectUtil.clear(player, PotionEffectType.CONDUIT_POWER);
        }

        // Nv10: swim faster in any water.
        attr(player, Attribute.MOVEMENT_SPEED, CURRENT_KEY,
                level >= 10 && player.isInWater() ? 0.25 : 0.0, AttributeModifier.Operation.ADD_SCALAR);
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.SUBMERGED_MINING_SPEED, DIVE_KEY);
        clearAttr(player, Attribute.MOVEMENT_SPEED, CURRENT_KEY);
        EffectUtil.clear(player, PotionEffectType.CONDUIT_POWER);
        super.onRemove(player);
    }
}
