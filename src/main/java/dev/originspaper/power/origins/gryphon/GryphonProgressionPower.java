package dev.originspaper.power.origins.gryphon;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Gryphon progression. XP: fly far, dive-attack, slay hostiles.
 * Milestones: Nv6 a stronger diving strike. Nv3 ("Decolagem Forte") and Nv10 ("Senhor dos Céus")
 * live in {@link dev.originspaper.power.shared.FlightLaunchPower}: a stronger launch and a shorter
 * cooldown.
 */
public class GryphonProgressionPower extends ProgressionPower {

    public GryphonProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().isGliding()) {
            accrueDistance(e.getPlayer(), e, 30.0, 5); // "Voar longas distâncias"
        }
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer != null && e.getEntity() instanceof Monster) {
            award(killer, 10); // "Matar monstros"
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player player && player.isGliding()) {
            if (readyForXp(player, 3)) {
                award(player, 8); // "Atacar mergulhando" (rate-limited vs. melee-spam farms)
            }
            if (level(player) >= 6) { // Nv6 "Caça-Picada"
                e.setDamage(e.getDamage() + 3.0);
            }
        }
    }
}
