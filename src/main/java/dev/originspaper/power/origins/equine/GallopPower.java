package dev.originspaper.power.origins.equine;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ArmorUtil;
import dev.originspaper.util.AttributeUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Builds travel speed while the equine keeps sprinting. Stage one works in ordinary traversable
 * space; stage two needs open sky and headroom, keeping tunnels from becoming permanent highways.
 */
public class GallopPower extends AbstractPower {

    private static final double MIN_DISTANCE_PER_SECOND_SQUARED = 0.6 * 0.6;

    private final int stageOneSeconds;
    private final int stageTwoSeconds;
    private final Map<UUID, GallopState> states = new ConcurrentHashMap<>();

    public GallopPower(String id, int stageOneSeconds, int stageTwoSeconds) {
        super(id);
        this.stageOneSeconds = stageOneSeconds;
        this.stageTwoSeconds = stageTwoSeconds;
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onTick(Player player) {
        UUID playerId = player.getUniqueId();
        Location here = player.getLocation();
        GallopState previous = states.get(playerId);

        boolean canGallop = player.isSprinting() && !player.isSneaking() && !player.isInWater()
                && !player.isGliding() && !player.isInsideVehicle()
                && !ArmorUtil.isWearingHeavyChest(player) && hasHeadroom(player);
        if (!canGallop) {
            reset(player);
            return;
        }

        if (previous == null) {
            states.put(playerId, new GallopState(1, here.getX(), here.getZ(), 0));
            return;
        }

        double dx = here.getX() - previous.x();
        double dz = here.getZ() - previous.z();
        if (dx * dx + dz * dz < MIN_DISTANCE_PER_SECOND_SQUARED) {
            reset(player); // stopped against a wall or is sprinting in place
            return;
        }

        int seconds = previous.seconds() + 1;
        int firstAt = Math.max(1, stageOneSeconds - (levelOf(player) >= 3 ? 1 : 0));
        int secondAt = Math.max(firstAt + 1, stageTwoSeconds - (levelOf(player) >= 3 ? 1 : 0));
        int stage = seconds >= secondAt && isOpenTerrain(player) ? 2 : seconds >= firstAt ? 1 : 0;
        double scalar = stage == 0 ? 0.0 : stage == 1 ? 0.20 : levelOf(player) >= 10 ? 0.60 : 0.40;
        AttributeUtil.sync(player, Attribute.MOVEMENT_SPEED, id, scalar,
                AttributeModifier.Operation.ADD_SCALAR);

        if (stage > previous.stage()) {
            player.getWorld().playSound(here, Sound.ENTITY_HORSE_GALLOP, 0.8f,
                    stage == 1 ? 1.1f : 1.35f);
            ParticleUtil.spawnGroundBurst(Particle.CLOUD, here, 0.45, stage == 1 ? 5 : 9, 0.03);
        } else if (stage >= 2) {
            Object below = here.clone().subtract(0, 0.15, 0).getBlock().getBlockData();
            ParticleUtil.spawnTrail(Particle.BLOCK, here.clone().add(0, 0.1, 0), 2, 0.2, below);
        }
        states.put(playerId, new GallopState(seconds, here.getX(), here.getZ(), stage));
    }

    static boolean hasHeadroom(Player player) {
        return player.getLocation().clone().add(0, 2.1, 0).getBlock().isPassable();
    }

    private static boolean isOpenTerrain(Player player) {
        return hasHeadroom(player) && player.getEyeLocation().getBlock().getLightFromSky() > 0;
    }

    private void reset(Player player) {
        states.remove(player.getUniqueId());
        AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id);
    }

    @Override
    public void onRemove(Player player) {
        reset(player);
    }

    private record GallopState(int seconds, double x, double z, int stage) {}
}
