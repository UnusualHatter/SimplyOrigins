package dev.originspaper.power.origins.goat;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Goat progression XP from hunting. The level milestones are skill-shaped, so they live in the
 * active powers: harder/bigger headbutts in {@link LeapPower} (Nv3 damage, Nv10 radius, plus the
 * "Acertar Cabeçadas" XP), and a stronger Ram knockback in {@link RamPower} (Nv6).
 */
public class GoatProgressionPower extends ProgressionPower {

    public GoatProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) {
            return;
        }
        if (e.getEntity() instanceof Monster) {
            award(killer, 10); // "Matar monstros"
        } else if (e.getEntity() instanceof Animals) {
            award(killer, 6); // "Caçar animais"
        }
    }
}
