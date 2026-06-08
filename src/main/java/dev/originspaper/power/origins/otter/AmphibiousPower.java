package dev.originspaper.power.origins.otter;

import dev.originspaper.power.shared.AbstractPower;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

/** Breathes underwater and on land perfectly. */
public class AmphibiousPower extends AbstractPower {

    public AmphibiousPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (player.isInWater()) {
            player.setRemainingAir(player.getMaximumAir());
        }
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.DROWNING
                && e.getEntity() instanceof Player p && p.isInWater()) {
            e.setCancelled(true);
        }
    }
}
