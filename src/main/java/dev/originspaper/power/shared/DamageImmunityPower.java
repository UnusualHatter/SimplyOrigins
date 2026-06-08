package dev.originspaper.power.shared;

import org.bukkit.event.entity.EntityDamageEvent;

import java.util.EnumSet;
import java.util.Set;

/** Cancels damage from any of the configured causes (victim-side). */
public class DamageImmunityPower extends AbstractPower {

    private final Set<EntityDamageEvent.DamageCause> causes;

    public DamageImmunityPower(String id, EntityDamageEvent.DamageCause... causes) {
        super(id);
        this.causes = EnumSet.of(causes[0], causes);
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (causes.contains(e.getCause())) {
            e.setCancelled(true);
        }
    }
}
