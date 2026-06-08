package dev.originspaper.power.shared;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/** Immunity to fire, lava and magma block damage. */
public class FireImmunityPower extends DamageImmunityPower {

    public FireImmunityPower(String id) {
        super(id, DamageCause.FIRE, DamageCause.FIRE_TICK, DamageCause.LAVA, DamageCause.HOT_FLOOR);
    }
}
