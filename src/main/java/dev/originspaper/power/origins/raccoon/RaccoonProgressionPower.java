package dev.originspaper.power.origins.raccoon;

import dev.originspaper.power.shared.NightTimeEffectPower;
import dev.originspaper.power.shared.ProgressionPower;
import dev.originspaper.power.shared.TrashGourmetPower;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 * Raccoon progression. XP: scavenger finds (granted by {@link ScavengersLuckPower}), trash meals,
 * night kills. Milestones live in the kit: Nv3 better find odds ({@link ScavengersLuckPower}),
 * Nv6 scurry cooldown and Nv10 longer invisibility ({@link RaccoonScurryPower}).
 */
public class RaccoonProgressionPower extends ProgressionPower {

    public RaccoonProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (TrashGourmetPower.isTrashFood(e.getItem().getType())
                && readyForXp(e.getPlayer(), 5)) {
            award(e.getPlayer(), 6); // "Comer comidas duvidosas"
        }
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer != null && e.getEntity() instanceof Monster
                && NightTimeEffectPower.isNight(killer)) {
            award(killer, 10); // "Matar monstros à noite"
        }
    }
}
