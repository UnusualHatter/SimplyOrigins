package dev.originspaper.power.shared;

import org.bukkit.damage.DamageType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/** Immunity to fire, lava and magma block damage. */
public class FireImmunityPower extends DamageImmunityPower {

    public FireImmunityPower(String id) {
        super(id, DamageCause.FIRE, DamageCause.FIRE_TICK, DamageCause.LAVA);
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        // Since 26.2 magma damage arrives as CONTACT, not HOT_FLOOR.
        if (DamageType.HOT_FLOOR.equals(e.getDamageSource().getDamageType())) {
            e.setCancelled(true);
            return;
        }
        super.onDamage(e);
    }
}
