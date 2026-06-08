package dev.originspaper.power.origins.rat;

import dev.originspaper.power.shared.AbstractPower;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.concurrent.ThreadLocalRandom;

/** 30% chance to dodge projectiles. */
public class EvasionPower extends AbstractPower {

    public EvasionPower(String id) {
        super(id);
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (ThreadLocalRandom.current().nextDouble() < 0.3) {
                e.setCancelled(true);
            }
        }
    }
}
