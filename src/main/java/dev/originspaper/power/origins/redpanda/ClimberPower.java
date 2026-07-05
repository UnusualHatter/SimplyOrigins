package dev.originspaper.power.origins.redpanda;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Slow, controlled climb: holding sneak against a trunk/leaves/bamboo lifts the red panda with a
 * short Levitation pulse renewed each second, plus Slow Falling so letting go never hurts. It only
 * works while touching a climbable block, so it is tree climbing — not free flight. Suppressed by
 * heavy chestplates ("Corpo Leve").
 */
public class ClimberPower extends AbstractPower {

    private static final BlockFace[] SIDES =
            {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

    /** Last tick (1/s) climb XP was granted, per player — stops AFK hold-sneak farms at treetops. */
    private final Map<UUID, Long> lastClimbXp = new ConcurrentHashMap<>();

    public ClimberPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (!player.isSneaking() || TreeAcrobatPower.isWearingHeavyChest(player)) {
            return;
        }
        if (!touchingClimbable(player)) {
            return;
        }
        // Short pulses renewed every cycle: rises ~0.9 blocks/s while held, stops soon after release.
        EffectUtil.apply(player, PotionEffectType.LEVITATION, 25, 0);
        EffectUtil.apply(player, PotionEffectType.SLOW_FALLING, 60, 0);
        ParticleUtil.spawnTrail(Particle.COMPOSTER, player.getLocation().add(0, 0.8, 0), 2, 0.25);

        // "Escalar troncos e bambus": XP only while actually rising, at most once every 3s —
        // hovering pinned against a ceiling earns nothing.
        if (player.getVelocity().getY() > 0.05) {
            long now = plugin().tick();
            Long last = lastClimbXp.get(player.getUniqueId());
            if (last == null || now - last >= 3) {
                lastClimbXp.put(player.getUniqueId(), now);
                plugin().progression().awardXp(player, 2);
            }
        }
    }

    private boolean touchingClimbable(Player player) {
        Location loc = player.getLocation();
        for (BlockFace side : SIDES) {
            if (isClimbable(loc.getBlock().getRelative(side).getType())
                    || isClimbable(loc.getBlock().getRelative(side).getRelative(BlockFace.UP).getType())) {
                return true;
            }
        }
        // Standing inside vines/bamboo also counts (they occupy the player's block).
        return isClimbable(loc.getBlock().getType());
    }

    private boolean isClimbable(Material type) {
        return type.name().endsWith("_LOG") || type.name().endsWith("_WOOD")
                || type.name().endsWith("_LEAVES")
                || type == Material.BAMBOO || type == Material.BAMBOO_BLOCK
                || type == Material.STRIPPED_BAMBOO_BLOCK || type == Material.VINE;
    }

    @Override
    public void onRemove(Player player) {
        lastClimbXp.remove(player.getUniqueId());
        // Short effects, but strip any climb residue immediately on origin switch.
        player.removePotionEffect(PotionEffectType.LEVITATION);
        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
    }
}
