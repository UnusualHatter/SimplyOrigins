package dev.originspaper.power.origins.deer;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Speed boost when sprinting on natural terrain. */
public class NaturalRunnerPower extends AbstractPower {

    public NaturalRunnerPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (!player.isSprinting()) {
            return;
        }
        Block below = player.getLocation().subtract(0, 0.1, 0).getBlock();
        Material type = below.getType();
        if (isNatural(type)) {
            EffectUtil.apply(player, PotionEffectType.SPEED, 40, 0);
        }
    }

    private boolean isNatural(Material type) {
        return type == Material.GRASS_BLOCK || type == Material.DIRT || type == Material.COARSE_DIRT 
            || type == Material.PODZOL || type == Material.MYCELIUM || type == Material.MOSS_BLOCK
            || type == Material.STONE || type == Material.SAND || type == Material.RED_SAND
            || type == Material.SNOW_BLOCK || type == Material.SNOW || type.name().endsWith("_TERRACOTTA")
            || type == Material.TERRACOTTA || type.name().endsWith("_LOG") || type.name().endsWith("_LEAVES");
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.SPEED);
    }
}
