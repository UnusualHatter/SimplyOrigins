package dev.originspaper.power.origins.redpanda;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ArmorUtil;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

/**
 * Speed while on forest/tree blocks — the red panda's home turf. Deliberately excludes plain grass
 * so the deer keeps the general-travel niche: this bonus is about trees, canopies and forest floor,
 * not open fields. A diamond/netherite chestplate ("Corpo Leve") suppresses the bonus.
 */
public class TreeAcrobatPower extends AbstractPower {

    private static final Set<Material> FOREST_FLOOR = Set.of(
            Material.MOSS_BLOCK, Material.MOSS_CARPET, Material.PODZOL, Material.ROOTED_DIRT,
            Material.DIRT_PATH, Material.VINE, Material.BAMBOO, Material.BAMBOO_SAPLING,
            Material.BAMBOO_BLOCK, Material.STRIPPED_BAMBOO_BLOCK, Material.AZALEA,
            Material.FLOWERING_AZALEA);

    public TreeAcrobatPower(String id) {
        super(id);
    }

    /** Logs, stripped logs, wood, leaves, bamboo, vines, moss and forest-floor blocks. */
    static boolean isForestBlock(Material type) {
        return type.name().endsWith("_LOG") || type.name().endsWith("_WOOD")
                || type.name().endsWith("_LEAVES") || FOREST_FLOOR.contains(type);
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onTick(Player player) {
        // "Corpo Leve": heavy chestplates suppress the red panda's acrobatics.
        if (ArmorUtil.isWearingHeavyChest(player)) {
            return;
        }
        Material below = player.getLocation().clone().subtract(0, 0.1, 0).getBlock().getType();
        Material feet = player.getLocation().getBlock().getType();
        if (!isForestBlock(below) && !isForestBlock(feet)) {
            return;
        }
        // Nv10 "Acrobata Mestre": Speed II on forest blocks.
        int amplifier = levelOf(player) >= 10 ? 1 : 0;
        EffectUtil.apply(player, PotionEffectType.SPEED, 40, amplifier);
        if (plugin().tick() % 2 == 0 && player.getVelocity().lengthSquared() > 0.01) {
            ParticleUtil.spawnTrail(Particle.COMPOSTER, player.getLocation().add(0, 0.3, 0), 2, 0.2);
            ParticleUtil.spawnTrail(Particle.CHERRY_LEAVES, player.getLocation().add(0, 1.0, 0), 1, 0.3);
        }
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.SPEED);
    }
}
