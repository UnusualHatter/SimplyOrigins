package dev.originspaper.power.origins.demon;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/** Cosmetic-only: infernal flames when wading in lava and on the demon's melee strikes. */
public class InfernalVisualsPower extends AbstractPower {

    public InfernalVisualsPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (player.isInLava()) {
            ParticleUtil.spawnTrail(Particle.SOUL_FIRE_FLAME, player.getLocation().add(0, 0.5, 0), 6, 0.4);
            ParticleUtil.spawnTrail(Particle.LAVA, player.getLocation().add(0, 0.3, 0), 2, 0.3);
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        Location loc = e.getEntity().getLocation().add(0, 1.0, 0);
        ParticleUtil.spawnTrail(Particle.FLAME, loc, 8, 0.4);
        ParticleUtil.spawnTrail(Particle.CRIT, loc, 6, 0.4);
    }
}
