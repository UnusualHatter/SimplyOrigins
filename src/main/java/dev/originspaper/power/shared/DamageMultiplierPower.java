package dev.originspaper.power.shared;

import org.bukkit.event.entity.EntityDamageEvent;

import java.util.EnumSet;
import java.util.Set;

/** Multiplies incoming damage of the configured causes (victim-side weakness/resistance). */
public class DamageMultiplierPower extends AbstractPower {

    private final double multiplier;
    private final Set<EntityDamageEvent.DamageCause> causes;

    public DamageMultiplierPower(String id, double multiplier, EntityDamageEvent.DamageCause... causes) {
        super(id);
        this.multiplier = multiplier;
        this.causes = EnumSet.of(causes[0], causes);
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (causes.contains(e.getCause())) {
            e.setDamage(e.getDamage() * multiplier);
        }
    }
}
