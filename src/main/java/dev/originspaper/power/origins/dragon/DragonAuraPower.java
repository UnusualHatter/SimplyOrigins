package dev.originspaper.power.origins.dragon;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.FoodUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/** Cosmetic-only dragon flair: melee wisps, a glide breath trail, and embers when eating meat. */
public class DragonAuraPower extends AbstractPower {

    public DragonAuraPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (player.isGliding()) {
            Location loc = player.getLocation().add(0, 0.3, 0);
            ParticleUtil.spawnTrail(Particle.DRAGON_BREATH, loc, 3, 0.3);
            ParticleUtil.spawnTrail(Particle.END_ROD, loc, 2, 0.2);
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        Location loc = e.getEntity().getLocation().add(0, 1.0, 0);
        ParticleUtil.spawnTrail(Particle.CRIT, loc, 6, 0.4);
        ParticleUtil.spawnTrail(Particle.DRAGON_BREATH, loc, 4, 0.3);
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (!FoodUtil.isMeat(e.getItem().getType())) {
            return;
        }
        Location loc = e.getPlayer().getLocation().add(0, 1.0, 0);
        ParticleUtil.spawnTrail(Particle.LAVA, loc, 4, 0.3);
        ParticleUtil.spawnTrail(Particle.SMOKE, loc, 4, 0.3);
    }
}
