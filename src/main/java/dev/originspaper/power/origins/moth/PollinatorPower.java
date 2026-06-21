package dev.originspaper.power.origins.moth;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/** While sneaking, nearby crops, saplings and stalks grow as if bone-mealed; pollen blinds foes. */
public class PollinatorPower extends AbstractPower {

    // Base pollination is just the block the moth stands on (1x1); the Nv3 milestone widens it to
    // a 3x3 patch. Kept small and centred on the player so the effect is clear and predictable.
    private static final int RADIUS = 0;
    private static final double CHANCE_PER_BLOCK = 0.40;
    private static final double BLIND_RADIUS = 4.0;
    private static final int POLLINATE_XP = 4;

    /** Supported non-sapling plants. Saplings/propagules/azaleas are matched by name. */
    private static final Set<Material> PLANTS = Set.of(
            Material.WHEAT, Material.CARROTS, Material.POTATOES, Material.BEETROOTS,
            Material.NETHER_WART, Material.COCOA, Material.SWEET_BERRY_BUSH,
            Material.CAVE_VINES, Material.CAVE_VINES_PLANT,
            Material.SUGAR_CANE, Material.BAMBOO, Material.BAMBOO_SAPLING);

    public PollinatorPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (!player.isSneaking()) {
            return;
        }
        // Continuous pollen aura — visible feedback that the effect is active while crouched.
        ParticleUtil.spawnTrail(Particle.HAPPY_VILLAGER, player.getLocation().add(0, 0.6, 0), 1, 0.4);

        // The moth's one bit of teeth: pollen blinds hostiles that crowd it while it pollinates.
        blindNearbyFoes(player);

        // Growth runs on a steady cadence off the global tick (every ~2s). Because it is driven by
        // the tick — not by the crouch action — holding crouch keeps pollinating on its own, and
        // tapping crouch repeatedly cannot speed it up.
        if (plugin().tick() % 2 != 0) {
            return;
        }
        int level = levelOf(player);
        int radius = RADIUS + (level >= 3 ? 1 : 0); // Nv3 "Pólen Fértil": 1x1 → 3x3 pollination
        // Centred on the block the moth is standing on, so the patch always follows the player.
        Location base = player.getLocation();
        boolean grewAny = false;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (ThreadLocalRandom.current().nextDouble() >= CHANCE_PER_BLOCK) {
                        continue;
                    }
                    if (grow(base.clone().add(dx, dy, dz).getBlock())) {
                        grewAny = true;
                    }
                }
            }
        }
        if (grewAny) {
            plugin().progression().awardXp(player, POLLINATE_XP); // "Polinizar plantações"
            if (level >= 10) { // Nv10 "Aura de Vida": saturate nearby players
                grantLifeAura(player);
            }
        }
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    /** Feeds the moth and nearby players a touch of Saturation while it pollinates. */
    private void grantLifeAura(Player player) {
        for (Player nearby : player.getWorld().getNearbyPlayers(player.getLocation(), 6.0)) {
            nearby.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 40, 0, true, false, false));
            ParticleUtil.spawnTrail(Particle.HEART, nearby.getLocation().add(0, 1.0, 0), 1, 0.3);
        }
    }

    /** Blinds hostile mobs lingering next to the pollinating moth, with a small spore puff. */
    private void blindNearbyFoes(Player player) {
        for (Monster monster : player.getWorld()
                .getNearbyEntitiesByType(Monster.class, player.getLocation(), BLIND_RADIUS)) {
            monster.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0, true, false, false));
            monster.setTarget(null);
            ParticleUtil.spawnTrail(Particle.SPORE_BLOSSOM_AIR, monster.getEyeLocation(), 4, 0.3);
        }
    }

    /** Returns true if the block actually grew this call (used to award pollination XP). */
    private boolean grow(Block block) {
        Material type = block.getType();
        if (!isSupported(type)) {
            return false;
        }
        boolean sapling = isSapling(type);

        // Vanilla bone meal handles most cases (crops, saplings/trees, bamboo, berries, cocoa).
        if (block.applyBoneMeal(BlockFace.UP)) {
            if (sapling) {
                saplingParticles(block);
            } else {
                cropParticles(block);
            }
            return true;
        }
        // Bone meal didn't apply: age-based crops bone meal ignores (e.g. nether wart).
        if (block.getBlockData() instanceof Ageable ageable && ageable.getAge() < ageable.getMaximumAge()) {
            ageable.setAge(ageable.getAge() + 1);
            block.setBlockData(ageable);
            cropParticles(block);
            return true;
        }
        // Sugar cane: grow one segment upward if there is room (capped at the natural height).
        if (type == Material.SUGAR_CANE) {
            Block above = block.getRelative(BlockFace.UP);
            if (above.getType().isAir() && columnHeight(block, type) < 3) {
                above.setType(type);
                cropParticles(above);
                return true;
            }
        }
        return false;
    }

    private int columnHeight(Block top, Material type) {
        int height = 1;
        Block below = top.getRelative(BlockFace.DOWN);
        while (below.getType() == type && height < 16) {
            height++;
            below = below.getRelative(BlockFace.DOWN);
        }
        return height;
    }

    private boolean isSupported(Material t) {
        return PLANTS.contains(t) || isSapling(t);
    }

    private boolean isSapling(Material t) {
        return t.name().endsWith("_SAPLING") || t == Material.MANGROVE_PROPAGULE
                || t == Material.AZALEA || t == Material.FLOWERING_AZALEA;
    }

    private void cropParticles(Block block) {
        Location c = block.getLocation().add(0.5, 0.5, 0.5);
        ParticleUtil.spawnTrail(Particle.HAPPY_VILLAGER, c, 3, 0.25);
        ParticleUtil.spawnTrail(Particle.COMPOSTER, c, 3, 0.25);
    }

    private void saplingParticles(Block block) {
        Location c = block.getLocation().add(0.5, 0.5, 0.5);
        ParticleUtil.spawnTrail(Particle.CHERRY_LEAVES, c, 3, 0.3);
        ParticleUtil.spawnTrail(Particle.SPORE_BLOSSOM_AIR, c, 2, 0.3);
    }
}
