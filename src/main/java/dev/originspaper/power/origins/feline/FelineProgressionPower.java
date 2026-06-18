package dev.originspaper.power.origins.feline;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Feline progression. XP: hunt hostiles, hunt animals, chase prey on the run.
 * Milestones: Nv3 faster while sprinting, Nv6 quicker attacks, Nv10 chance of a critical pounce.
 */
public class FelineProgressionPower extends ProgressionPower {

    private static final String CHASE_KEY = "feline:prog_chase";
    private static final String ATK_KEY = "feline:prog_atk";

    public FelineProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) {
            return;
        }
        if (e.getEntity() instanceof Monster) {
            award(killer, 12); // "Caçar criaturas hostis"
        } else if (e.getEntity() instanceof Animals) {
            award(killer, 6); // "Caçar animais"
        }
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().isSprinting()) {
            accrueDistance(e.getPlayer(), e, 30.0, 4); // "Perseguir presas"
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        // Nv10 "Bote Felino": a chance to land a critical blow.
        if (e.getDamager() instanceof Player player && level(player) >= 10
                && ThreadLocalRandom.current().nextDouble() < 0.25) {
            e.setDamage(e.getDamage() + 3.0);
        }
    }

    @Override
    public void onTick(Player player) {
        int level = level(player);
        attr(player, Attribute.MOVEMENT_SPEED, CHASE_KEY,
                level >= 3 && player.isSprinting() ? 0.04 : 0.0, AttributeModifier.Operation.ADD_SCALAR);
        attr(player, Attribute.ATTACK_SPEED, ATK_KEY,
                level >= 6 ? 0.5 : 0.0, AttributeModifier.Operation.ADD_NUMBER);
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.MOVEMENT_SPEED, CHASE_KEY);
        clearAttr(player, Attribute.ATTACK_SPEED, ATK_KEY);
        super.onRemove(player);
    }
}
