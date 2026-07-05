package dev.originspaper.power.origins.equine;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.AttributeUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.Set;

/** A small, frequently useful travel bonus on paths, grass and other horse-friendly terrain. */
public class TrailRunnerPower extends AbstractPower {

    private static final Set<Material> TRAIL_BLOCKS = Set.of(
            Material.GRASS_BLOCK, Material.DIRT, Material.DIRT_PATH, Material.COARSE_DIRT,
            Material.ROOTED_DIRT, Material.PODZOL, Material.FARMLAND, Material.HAY_BLOCK);

    public TrailRunnerPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        Material below = player.getLocation().clone().subtract(0, 0.1, 0).getBlock().getType();
        boolean onTrail = TRAIL_BLOCKS.contains(below) && !player.isInWater();
        AttributeUtil.sync(player, Attribute.MOVEMENT_SPEED, id, onTrail ? 0.05 : 0.0,
                AttributeModifier.Operation.ADD_SCALAR);
        if (onTrail && player.isSprinting()) {
            ParticleUtil.spawnTrail(Particle.DUST_PLUME,
                    player.getLocation().add(0, 0.1, 0), 2, 0.2);
        }
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id);
    }
}
