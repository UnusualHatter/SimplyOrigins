package dev.originspaper.power.origins.raccoon;

import dev.originspaper.listener.PlacedBlockTracker;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Rummaging luck: kills and mundane digging (leaves, gravel, dirt) sometimes turn up a small
 * trinket. Strictly low-value junk — never valuables, ores, player drops or container loot — and
 * economy-safe: low chance, a shared 5s per-player cooldown, no player-placed blocks (anti-farm),
 * and cancelled break events (claim protection) never reach this handler.
 */
public class ScavengersLuckPower extends AbstractPower {

    /** Deliberately junk-tier. Anything valuable (ores, gear, gems) is out of bounds. */
    private static final List<Material> TRINKETS = List.of(
            Material.STRING, Material.BONE, Material.STICK, Material.PAPER,
            Material.IRON_NUGGET, Material.BREAD, Material.COAL, Material.LEATHER,
            Material.GLASS_BOTTLE, Material.ROTTEN_FLESH, Material.SWEET_BERRIES);

    private static final Set<Material> DIGGABLES = Set.of(
            Material.GRAVEL, Material.DIRT, Material.COARSE_DIRT, Material.ROOTED_DIRT,
            Material.GRASS_BLOCK, Material.PODZOL, Material.MYCELIUM, Material.MUD);

    private static final long FIND_INTERVAL_SECONDS = 5;

    private final double chance;

    /** Last tick (1/s) a trinket dropped, per player — caps the find rate. */
    private final Map<UUID, Long> lastFind = new ConcurrentHashMap<>();

    public ScavengersLuckPower(String id, double chance) {
        super(id);
        this.chance = chance;
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null || e.getEntity() instanceof Player) {
            return; // never scavenge from player deaths
        }
        if (e.getEntity() instanceof Monster || e.getEntity() instanceof Animals) {
            tryFind(killer, e.getEntity().getLocation());
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        Material type = e.getBlock().getType();
        boolean diggable = DIGGABLES.contains(type) || type.name().endsWith("_LEAVES");
        if (!diggable || PlacedBlockTracker.isPlayerPlaced(e.getBlock())) {
            return;
        }
        tryFind(e.getPlayer(), e.getBlock().getLocation().add(0.5, 0.5, 0.5));
    }

    private void tryFind(Player player, Location where) {
        if (!claims().canModifyBlock(player, where.getBlock())) {
            return;
        }
        long now = plugin().tick();
        Long last = lastFind.get(player.getUniqueId());
        if (last != null && now - last < FIND_INTERVAL_SECONDS) {
            return;
        }
        // Nv3 "Mãos Habilidosas": better odds of turning something up.
        double effective = chance * (levelOf(player) >= 3 ? 1.5 : 1.0);
        if (ThreadLocalRandom.current().nextDouble() >= effective) {
            return;
        }
        lastFind.put(player.getUniqueId(), now);
        Material trinket = TRINKETS.get(ThreadLocalRandom.current().nextInt(TRINKETS.size()));
        where.getWorld().dropItemNaturally(where, new ItemStack(trinket));
        ParticleUtil.spawnTrail(Particle.HAPPY_VILLAGER, where.clone().add(0, 0.4, 0), 4, 0.25);
        plugin().progression().awardXp(player, 8); // "Vasculhar o mundo"
    }

    @Override
    public void onRemove(Player player) {
        lastFind.remove(player.getUniqueId());
    }
}
