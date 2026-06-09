package dev.originspaper.power.origins.rabbit;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/** Crops near the rabbit grow faster, with happy flair on growth, jumps, and carrot snacks. */
public class ReplenishPower extends AbstractPower {

    private static final int RADIUS = 4;

    private final Map<UUID, Boolean> wasOnGround = new ConcurrentHashMap<>();

    public ReplenishPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (plugin().tick() % 5 != 0) {
            return; // roughly every 100 ticks
        }
        Location base = player.getLocation();
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                    Block block = base.clone().add(dx, dy, dz).getBlock();
                    if (block.getBlockData() instanceof Ageable ageable
                            && ageable.getAge() < ageable.getMaximumAge()
                            && ThreadLocalRandom.current().nextDouble() < 0.30) {
                        ageable.setAge(ageable.getAge() + 1);
                        block.setBlockData(ageable);
                        Location center = block.getLocation().add(0.5, 0.5, 0.5);
                        ParticleUtil.spawnTrail(Particle.COMPOSTER, center, 3, 0.2);
                        ParticleUtil.spawnTrail(Particle.HAPPY_VILLAGER, center, 2, 0.2);
                    }
                }
            }
        }
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        boolean onGround = GroundUtil.isOnGround(player);
        Boolean prev = wasOnGround.put(player.getUniqueId(), onGround);
        if (Boolean.TRUE.equals(prev) && !onGround && player.getVelocity().getY() > 0.2) {
            Location loc = player.getLocation();
            ParticleUtil.spawnGroundBurst(Particle.CLOUD, loc, 0.3, 4, 0.02);
            ParticleUtil.spawnGroundBurst(Particle.HAPPY_VILLAGER, loc, 0.3, 3, 0.02);
        }
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        Material eaten = e.getItem().getType();
        if (eaten == Material.CARROT || eaten == Material.GOLDEN_CARROT) {
            ParticleUtil.spawnTrail(Particle.HAPPY_VILLAGER, e.getPlayer().getLocation().add(0, 1.0, 0), 6, 0.3);
        }
    }

    @Override
    public void onRemove(Player player) {
        wasOnGround.remove(player.getUniqueId());
    }
}
