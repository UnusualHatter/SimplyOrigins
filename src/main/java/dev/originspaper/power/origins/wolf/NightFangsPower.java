package dev.originspaper.power.origins.wolf;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.power.shared.NightTimeEffectPower;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/** Bonus melee damage at night. */
public class NightFangsPower extends AbstractPower {

    public NightFangsPower(String id) {
        super(id);
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player player && NightTimeEffectPower.isNight(player)) {
            e.setDamage(e.getDamage() + 2.0);
        }
    }
}
