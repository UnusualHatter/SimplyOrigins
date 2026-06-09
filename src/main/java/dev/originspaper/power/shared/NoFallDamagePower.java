package dev.originspaper.power.shared;

import dev.originspaper.util.ParticleUtil;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

/** Cancels all fall damage, with a soft dust puff on a meaningful landing. */
public class NoFallDamagePower extends DamageImmunityPower {

    public NoFallDamagePower(String id) {
        super(id, EntityDamageEvent.DamageCause.FALL);
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        super.onDamage(e); // cancels the fall damage
        // The cancel doesn't zero the reported amount, so we can still gate on the fall's size.
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL
                && e.getEntity() instanceof Player player && e.getDamage() > 4.0) {
            ParticleUtil.spawnGroundBurst(Particle.CLOUD, player.getLocation(), 0.5, 8, 0.05);
        }
    }
}
