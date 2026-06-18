package dev.originspaper.power.origins.dragon;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.World;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Dragon progression. XP: fly far, hunt in The End, and scorch foes with the breath (awarded in
 * {@link DragonBreathPower}). The level milestones are all about the breath, so they live in
 * {@link DragonBreathPower}: Nv3 longer range, Nv6 shorter cooldown, Nv10 a longer-burning blast.
 */
public class DragonProgressionPower extends ProgressionPower {

    public DragonProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().isGliding()) {
            accrueDistance(e.getPlayer(), e, 30.0, 4); // "Voar longas distâncias"
        }
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer != null && e.getEntity() instanceof Monster
                && killer.getWorld().getEnvironment() == World.Environment.THE_END) {
            award(killer, 25); // "Caçar no The End"
        }
    }
}
