package dev.originspaper.power.shared;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/** Bonus melee damage when striking while diving in a glide (talons / dive strike). */
public class DivePower extends AbstractPower {

    private final double multiplier;
    private final double maxFallY;

    public DivePower(String id, double multiplier, double maxFallY) {
        super(id);
        this.multiplier = multiplier;
        this.maxFallY = maxFallY;
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        if (player.isGliding() && player.getVelocity().getY() < maxFallY) {
            e.setDamage(e.getDamage() * multiplier);
        }
    }
}
