package dev.originspaper.power.origins.deer;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Deer progression. XP: sprint on the move, survive falls, fend off hostiles.
 * Milestones: Nv3 higher jump, Nv6 extra speed, Nv10 fall damage reduced to almost nothing.
 */
public class DeerProgressionPower extends ProgressionPower {

    private static final String JUMP_KEY = "deer:prog_jump";
    private static final String SPEED_KEY = "deer:prog_speed";

    public DeerProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().isSprinting()) {
            accrueDistance(e.getPlayer(), e, 30.0, 6); // "Correr longas distâncias"
        }
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)
                || e.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        // Nv10 "Instinto de Fuga": cut fall damage to a fifth (stacks with the base Soft Landing).
        if (level(player) >= 10) {
            e.setDamage(e.getDamage() * 0.2);
        }
        // XP only if the deer actually walks away from the fall.
        if (player.getHealth() - e.getFinalDamage() > 0) {
            award(player, 18); // "Sobreviver a grandes quedas"
        }
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer != null && e.getEntity() instanceof Monster) {
            award(killer, 8); // "Matar monstros"
        }
    }

    @Override
    public void onTick(Player player) {
        int level = level(player);
        attr(player, Attribute.JUMP_STRENGTH, JUMP_KEY,
                level >= 3 ? 0.1 : 0.0, AttributeModifier.Operation.ADD_NUMBER);
        attr(player, Attribute.MOVEMENT_SPEED, SPEED_KEY,
                level >= 6 ? 0.05 : 0.0, AttributeModifier.Operation.ADD_SCALAR);
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.JUMP_STRENGTH, JUMP_KEY);
        clearAttr(player, Attribute.MOVEMENT_SPEED, SPEED_KEY);
        super.onRemove(player);
    }
}
