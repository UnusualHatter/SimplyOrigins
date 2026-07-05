package dev.originspaper.power.origins.shark;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/** Extra melee damage, but only while the shark itself is in the water. Nothing on dry land. */
public class PredatorBitePower extends AbstractPower {

    private final double extraDamage;

    public PredatorBitePower(String id, double extraDamage) {
        super(id);
        this.extraDamage = extraDamage;
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player) || !player.isInWater()) {
            return;
        }
        // Nv3 "Mandíbula Feroz": the bite digs half a heart deeper.
        double bonus = extraDamage + (levelOf(player) >= 3 ? 1.0 : 0.0);
        e.setDamage(e.getDamage() + bonus);

        Location loc = e.getEntity().getLocation().add(0, 0.8, 0);
        ParticleUtil.spawnTrail(Particle.CRIT, loc, 6, 0.3);
        ParticleUtil.spawnTrail(Particle.BUBBLE_POP, loc, 8, 0.3);
    }
}
