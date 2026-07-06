package dev.originspaper.power.origins.redpanda;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ArmorUtil;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spider-style climb with a leash: holding sneak against any solid wall lifts the red panda with
 * short Levitation pulses, but only while it is at most 4 blocks above solid ground (5 at Nv10 —
 * "Acrobata Mestre"). Past the limit the claws give out and it eases down on Slow Falling, so this
 * can never turn into wall-hugging flight. Suppressed by heavy chestplates ("Corpo Leve").
 */
public class ClimberPower extends AbstractPower {

    private static final BlockFace[] SIDES =
            {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
    private static final int BASE_MAX_HEIGHT = 4;
    private static final int MASTER_MAX_HEIGHT = 5; // Nv10 "Acrobata Mestre"

    /** Last tick (1/s) climb XP was granted, per player — stops AFK hold-sneak farms. */
    private final Map<UUID, Long> lastClimbXp = new ConcurrentHashMap<>();
    /** Last global tick at which this power refreshed its temporary climb effects. */
    private final Map<UUID, Long> lastClimbEffect = new ConcurrentHashMap<>();

    public ClimberPower(String id) {
        super(id);
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onTick(Player player) {
        if (!player.isSneaking() || ArmorUtil.isWearingHeavyChest(player)) {
            return;
        }
        if (!touchingWall(player)) {
            return;
        }
        int maxHeight = levelOf(player) >= 10 ? MASTER_MAX_HEIGHT : BASE_MAX_HEIGHT;
        if (heightAboveGround(player, maxHeight) >= maxHeight) {
            // Claws give out past the limit — Slow Falling stays so letting go never hurts.
            EffectUtil.apply(player, PotionEffectType.SLOW_FALLING, 60, 0);
            return;
        }
        // Short pulses renewed every cycle: rises ~0.9 blocks/s while held, stops soon after release.
        EffectUtil.apply(player, PotionEffectType.LEVITATION, 25, 0);
        EffectUtil.apply(player, PotionEffectType.SLOW_FALLING, 60, 0);
        lastClimbEffect.put(player.getUniqueId(), plugin().tick());
        ParticleUtil.spawnTrail(Particle.COMPOSTER, player.getLocation().add(0, 0.8, 0), 2, 0.25);

        // "Escalar troncos e bambus": XP only while actually rising on natural climbables, at most
        // once every 3s — hovering pinned against a ceiling or scaling walls earns nothing.
        if (player.getVelocity().getY() > 0.05 && touchingNaturalClimbable(player)) {
            long now = plugin().tick();
            Long last = lastClimbXp.get(player.getUniqueId());
            if (last == null || now - last >= 3) {
                lastClimbXp.put(player.getUniqueId(), now);
                plugin().progression().awardXp(player, 2);
            }
        }
    }

    /** Blocks of air (or other non-solid) between the player's feet and the ground below. */
    private int heightAboveGround(Player player, int maxScan) {
        Block below = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        int height = 0;
        while (height < maxScan && !below.getType().isSolid()) {
            height++;
            below = below.getRelative(BlockFace.DOWN);
        }
        return height;
    }

    /** Any solid block beside the player's feet or head — a spider climbs whatever wall it touches. */
    private boolean touchingWall(Player player) {
        Block feet = player.getLocation().getBlock();
        for (BlockFace side : SIDES) {
            if (feet.getRelative(side).getType().isSolid()
                    || feet.getRelative(side).getRelative(BlockFace.UP).getType().isSolid()) {
                return true;
            }
        }
        // Standing inside vines/bamboo also counts (they occupy the player's block).
        return isNaturalClimbable(feet.getType());
    }

    private boolean touchingNaturalClimbable(Player player) {
        Block feet = player.getLocation().getBlock();
        for (BlockFace side : SIDES) {
            if (isNaturalClimbable(feet.getRelative(side).getType())
                    || isNaturalClimbable(feet.getRelative(side).getRelative(BlockFace.UP).getType())) {
                return true;
            }
        }
        return isNaturalClimbable(feet.getType());
    }

    private boolean isNaturalClimbable(Material type) {
        return type.name().endsWith("_LOG") || type.name().endsWith("_WOOD")
                || type.name().endsWith("_LEAVES")
                || type == Material.BAMBOO || type == Material.BAMBOO_BLOCK
                || type == Material.STRIPPED_BAMBOO_BLOCK || type == Material.VINE;
    }

    @Override
    public void onRemove(Player player) {
        lastClimbXp.remove(player.getUniqueId());
        Long last = lastClimbEffect.remove(player.getUniqueId());
        if (last != null && plugin().tick() - last <= 3) {
            EffectUtil.clearTemporary(player, PotionEffectType.LEVITATION, 25, 0);
            EffectUtil.clearTemporary(player, PotionEffectType.SLOW_FALLING, 60, 0);
        }
    }
}
