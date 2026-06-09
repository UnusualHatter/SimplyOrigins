package dev.originspaper.power.origins.moth;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.concurrent.ThreadLocalRandom;

/** Bees never turn hostile toward the moth, and greet it now and then. */
public class FriendOfBeesPower extends AbstractPower {

    public FriendOfBeesPower(String id) {
        super(id);
    }

    @Override
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getEntity() instanceof Bee bee && e.getTarget() instanceof Player) {
            e.setCancelled(true);
            bee.setAnger(0);
        }
    }

    @Override
    public void onTick(Player player) {
        if (plugin().tick() % 3 != 0) {
            return; // every ~3s
        }
        for (Bee bee : player.getWorld().getNearbyEntitiesByType(Bee.class, player.getLocation(), 6)) {
            bee.setAnger(0); // keep nearby bees calm even without a target event
            if (ThreadLocalRandom.current().nextDouble() < 0.25) {
                Location c = bee.getLocation().add(0, 0.4, 0);
                ParticleUtil.spawnTrail(Particle.HEART, c, 1, 0.2);
                ParticleUtil.spawnTrail(Particle.CHERRY_LEAVES, c, 1, 0.2);
            }
        }
    }
}
