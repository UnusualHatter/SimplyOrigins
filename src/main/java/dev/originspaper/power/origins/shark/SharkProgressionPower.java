package dev.originspaper.power.origins.shark;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Shark progression. XP: swim distance, land hits while in water, kill in water.
 * Milestones live in the kit: Nv3 bite damage ({@link PredatorBitePower}), Nv6 rush cooldown and
 * Nv10 rush damage ({@link FeedingRushPower}), Nv10 blood sense range ({@link BloodSensePower}).
 */
public class SharkProgressionPower extends ProgressionPower {

    public SharkProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().isInWater()) {
            accrueDistance(e.getPlayer(), e, 25.0, 5); // "Nadar longas distâncias"
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player player && player.isInWater() && readyForXp(player, 5)) {
            award(player, 6); // "Atacar presas na água"
        }
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null || !killer.isInWater()) {
            return;
        }
        if (e.getEntity() instanceof Monster) {
            award(killer, 15); // "Caçar na água"
        } else {
            award(killer, 6);
        }
    }
}
