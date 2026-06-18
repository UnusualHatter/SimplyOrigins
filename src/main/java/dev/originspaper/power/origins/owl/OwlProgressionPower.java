package dev.originspaper.power.origins.owl;

import dev.originspaper.power.shared.NightTimeEffectPower;
import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Owl progression. XP: glide far, hunt at night, strike from a dive.
 * Milestones: Nv3 sharper night strikes, Nv10 a heavy diving blow. Nv6 ("Sonar Apurado") lives in
 * {@link EcholocationPower}, cutting its cooldown.
 */
public class OwlProgressionPower extends ProgressionPower {

    public OwlProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().isGliding()) {
            accrueDistance(e.getPlayer(), e, 30.0, 5); // "Planar longas distâncias"
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
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        int level = level(player);
        if (player.isGliding()) {
            if (readyForXp(player, 3)) {
                award(player, 8); // "Atacar mergulhando" (rate-limited vs. melee-spam farms)
            }
            if (level >= 10) { // Nv10 "Predador Aéreo"
                e.setDamage(e.getDamage() + 4.0);
            }
        }
        if (level >= 3 && NightTimeEffectPower.isNight(player)) { // Nv3 "Olhar Noturno"
            e.setDamage(e.getDamage() + 2.0);
        }
    }
}
