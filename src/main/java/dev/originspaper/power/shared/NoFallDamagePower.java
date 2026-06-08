package dev.originspaper.power.shared;

import org.bukkit.event.entity.EntityDamageEvent;

/** Cancels all fall damage. */
public class NoFallDamagePower extends DamageImmunityPower {

    public NoFallDamagePower(String id) {
        super(id, EntityDamageEvent.DamageCause.FALL);
    }
}
