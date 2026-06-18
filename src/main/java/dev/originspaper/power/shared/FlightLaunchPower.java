package dev.originspaper.power.shared;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Active skill: launches the player upward and starts gliding shortly after. */
public class FlightLaunchPower extends AbstractPower implements ActivePowerType {

    private final long cooldownTicks;
    private final double upwardVelocity;
    private final long glideDelay;

    /** Activation second per player → drives the short ascent trail. */
    private final Map<UUID, Long> launchedAt = new ConcurrentHashMap<>();

    public FlightLaunchPower(String id, long cooldownTicks, double upwardVelocity, long glideDelay) {
        super(id);
        this.cooldownTicks = cooldownTicks;
        this.upwardVelocity = upwardVelocity;
        this.glideDelay = glideDelay;
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onActivate(Player player) {
        // Nv3 "Decolagem Forte" (gryphon): a stronger launch upward.
        double velocity = upwardVelocity * (levelOf(player) >= 3 ? 1.25 : 1.0);
        player.setVelocity(player.getVelocity().setY(velocity));
        Location c = player.getLocation();
        player.getWorld().playSound(c, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);
        ParticleUtil.spawnGroundBurst(Particle.CLOUD, c, 0.8, 12, 0.05);
        ParticleUtil.spawn(Particle.SWEEP_ATTACK, c.clone().add(0, 0.5, 0), 2, 0.4, 0.2, 0.4, 0.0);
        ParticleUtil.spawn(Particle.GUST, c.clone().add(0, 0.5, 0), 1, 0, 0, 0, 0.0);
        launchedAt.put(player.getUniqueId(), plugin().tick());

        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            if (!GroundUtil.isOnGround(player) && player.getInventory().getChestplate() != null) {
                player.setGliding(true);
            }
        }, glideDelay);
    }

    @Override
    public void onTick(Player player) {
        Long t = launchedAt.get(player.getUniqueId());
        if (t == null) {
            return;
        }
        if (plugin().tick() - t > 3 || GroundUtil.isOnGround(player)) {
            launchedAt.remove(player.getUniqueId());
            return;
        }
        if (player.getVelocity().getY() > 0.1) {
            ParticleUtil.spawnTrail(Particle.END_ROD, player.getLocation().add(0, 0.2, 0), 3, 0.2);
        }
    }

    @Override
    public long getCooldownTicks(Player player) {
        // Nv10 "Senhor dos Céus" (gryphon): shorter cooldown.
        if (levelOf(player) >= 10) {
            return (long) (cooldownTicks * 0.6);
        }
        return cooldownTicks;
    }

    @Override
    public void onRemove(Player player) {
        launchedAt.remove(player.getUniqueId());
    }
}
