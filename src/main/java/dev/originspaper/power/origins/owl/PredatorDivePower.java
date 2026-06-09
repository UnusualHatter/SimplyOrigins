package dev.originspaper.power.origins.owl;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

/** Massive damage on a diving strike, plus a short bleed. */
public class PredatorDivePower extends AbstractPower {

    private final double multiplier;

    public PredatorDivePower(String id, double multiplier) {
        super(id);
        this.multiplier = multiplier;
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        if (player.isGliding() && player.getVelocity().getY() < -0.3) {
            e.setDamage(e.getDamage() * multiplier);

            Location hit = e.getEntity().getLocation().add(0, 1.0, 0);
            ParticleUtil.spawn(Particle.SWEEP_ATTACK, hit, 1, 0, 0, 0, 0.0);
            ParticleUtil.spawnTrail(Particle.DAMAGE_INDICATOR, hit, 4, 0.3);
            ParticleUtil.spawnTrail(Particle.CRIT, hit, 6, 0.3);

            if (e.getEntity() instanceof LivingEntity target) {
                // Bleeding: 1 damage per second for 4 seconds, with a red drip.
                new BukkitRunnable() {
                    int ticks = 0;
                    @Override
                    public void run() {
                        if (ticks >= 4 || target.isDead() || !target.isValid()) {
                            cancel();
                            return;
                        }
                        target.damage(1.0);
                        Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 1.5f);
                        target.getWorld().spawnParticle(Particle.DUST, target.getLocation().add(0, 1, 0),
                                10, 0.3, 0.5, 0.3, 0, dust);
                        ticks++;
                    }
                }.runTaskTimer(plugin(), 20L, 20L); // every 1 second (20 ticks)
            }
        }
    }
}
