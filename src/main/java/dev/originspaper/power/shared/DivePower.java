package dev.originspaper.power.shared;

import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
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
            Location hit = e.getEntity().getLocation().add(0, 1.0, 0);
            ParticleUtil.spawn(Particle.SWEEP_ATTACK, hit, 1, 0, 0, 0, 0.0);
            ParticleUtil.spawnTrail(Particle.CRIT, hit, 6, 0.3);
        }
    }
}
