package dev.originspaper.power.origins.rabbit;

import dev.originspaper.power.shared.AbstractPower;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

/** Crops near the rabbit grow faster. */
public class ReplenishPower extends AbstractPower {

    private static final int RADIUS = 4;

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
                    }
                }
            }
        }
    }
}
