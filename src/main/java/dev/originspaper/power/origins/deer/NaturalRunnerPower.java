package dev.originspaper.power.origins.deer;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Speed boost when sprinting on natural terrain, with leafy/dusty flair and a jump puff. */
public class NaturalRunnerPower extends AbstractPower {

    private final Map<UUID, Boolean> wasOnGround = new ConcurrentHashMap<>();

    public NaturalRunnerPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (!player.isSprinting()) {
            return;
        }
        Block below = player.getLocation().clone().subtract(0, 0.1, 0).getBlock();
        Material type = below.getType();
        if (!isNatural(type)) {
            return;
        }
        EffectUtil.apply(player, PotionEffectType.SPEED, 40, 0);
        // Ground-tinted dust kicked up underfoot.
        ParticleUtil.spawnTrail(Particle.BLOCK, player.getLocation().add(0, 0.1, 0), 4, 0.2, below.getBlockData());
        // Forest-floor flair: drifting leaves.
        if (isForestFloor(type) && plugin().tick() % 2 == 0) {
            Location loc = player.getLocation().add(0, 1.2, 0);
            ParticleUtil.spawnTrail(Particle.CHERRY_LEAVES, loc, 2, 0.4);
            ParticleUtil.spawnTrail(Particle.FALLING_SPORE_BLOSSOM, loc, 1, 0.4);
        }
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        boolean onGround = GroundUtil.isOnGround(player);
        Boolean prev = wasOnGround.put(player.getUniqueId(), onGround);
        // Rising edge off the ground = a jump.
        if (Boolean.TRUE.equals(prev) && !onGround && player.getVelocity().getY() > 0.2) {
            ParticleUtil.spawnGroundBurst(Particle.CLOUD, player.getLocation(), 0.3, 4, 0.02);
        }
    }

    private boolean isForestFloor(Material type) {
        return type == Material.GRASS_BLOCK || type == Material.PODZOL || type == Material.MOSS_BLOCK
                || type.name().endsWith("_LEAVES");
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
        wasOnGround.remove(player.getUniqueId());
        EffectUtil.clear(player, PotionEffectType.SPEED);
    }
}
