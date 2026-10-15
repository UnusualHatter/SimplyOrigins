package dev.originspaper.listener;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Remembers blocks players placed, so "mining/breaking" progression XP can ignore them — otherwise
 * placing and breaking the same block on a loop would be a trivial XP farm. Memory is bounded per
 * world (oldest entries evicted past the cap); a restart clears it, which at worst lets a pre-placed
 * block count once — not a viable farm.
 *
 * <p>Break detection runs at {@link EventPriority#MONITOR} so the placed flag is still readable by
 * the normal-priority progression handlers that fire earlier in the same event.
 */
public class PlacedBlockTracker implements Listener {

    private static final int MAX_PER_WORLD = 60_000;
    private static final Map<UUID, Set<Long>> PLACED = new ConcurrentHashMap<>();

    /** Packs block coords into a long (26 bits x, 26 bits z, 12 bits y) — collision-free in range. */
    private static long key(Block b) {
        return ((long) (b.getX() & 0x3FFFFFF))
                | ((long) (b.getZ() & 0x3FFFFFF) << 26)
                | ((long) (b.getY() & 0xFFF) << 52);
    }

    /** True if this exact block was placed by a player and not yet broken. */
    public static boolean isPlayerPlaced(Block block) {
        Set<Long> set = PLACED.get(block.getWorld().getUID());
        return set != null && set.contains(key(block));
    }

    /**
     * Drops all tracked placements (plugin disable). The map is static, so without this a
     * {@code /reload} would keep the old classloader's data alive through the new one's lifetime.
     */
    public static void clearAll() {
        PLACED.clear();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e) {
        track(e.getBlock());
    }

    private static void track(Block block) {
        Set<Long> set = PLACED.computeIfAbsent(block.getWorld().getUID(),
                k -> Collections.synchronizedSet(new LinkedHashSet<>()));
        synchronized (set) {
            if (set.size() >= MAX_PER_WORLD) {
                var it = set.iterator();
                if (it.hasNext()) {
                    it.next();
                    it.remove(); // evict oldest
                }
            }
            set.add(key(block));
        }
    }

    // The placed flag follows a block a piston moves; otherwise a piston loop would turn placed
    // blocks back into "natural" ones and reopen the farm.
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent e) {
        movePlaced(e.getBlock(), e.getBlocks(), true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent e) {
        movePlaced(e.getBlock(), e.getBlocks(), false);
    }

    private static void movePlaced(Block piston, List<Block> moved, boolean extending) {
        if (moved.isEmpty() || !(piston.getBlockData() instanceof Directional directional)) {
            return;
        }
        Set<Long> set = PLACED.get(piston.getWorld().getUID());
        if (set == null) {
            return;
        }
        BlockFace facing = directional.getFacing();
        BlockFace motion = extending ? facing : facing.getOppositeFace();
        List<Block> destinations = new ArrayList<>();
        synchronized (set) {
            // Remove every source before adding destinations: in a pushed row one block's
            // destination is the next block's source.
            for (Block block : moved) {
                if (set.remove(key(block))) {
                    destinations.add(block.getRelative(motion));
                }
            }
        }
        destinations.forEach(PlacedBlockTracker::track);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        Set<Long> set = PLACED.get(e.getBlock().getWorld().getUID());
        if (set != null) {
            set.remove(key(e.getBlock()));
        }
    }
}
