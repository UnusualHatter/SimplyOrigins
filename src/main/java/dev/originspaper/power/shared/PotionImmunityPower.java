package dev.originspaper.power.shared;

import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

/** Cancels application of the configured potion effect types. */
public class PotionImmunityPower extends AbstractPower {

    private final Set<PotionEffectType> immune;

    public PotionImmunityPower(String id, PotionEffectType... types) {
        super(id);
        this.immune = Set.of(types);
    }

    @Override
    public void onPotionEffect(EntityPotionEffectEvent e) {
        EntityPotionEffectEvent.Action action = e.getAction();
        if (action != EntityPotionEffectEvent.Action.ADDED
                && action != EntityPotionEffectEvent.Action.CHANGED) {
            return;
        }
        if (e.getModifiedType() != null && immune.contains(e.getModifiedType())) {
            e.setCancelled(true);
        }
    }
}
