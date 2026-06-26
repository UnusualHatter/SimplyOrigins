package dev.originspaper.power.origins.rabbit;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Active skill: a quick forward hop-dash for getting around — modelled on the goat's headbutt
 * charge but purely mobility, dealing no damage and triggering no blast. 7s cooldown.
 */
public class RabbitDashPower extends AbstractPower implements ActivePowerType {

    private static final long COOLDOWN_TICKS = 140L; // 7 seconds
    private static final double DASH_SPEED = 1.8;

    /** Players currently mid-dash, for the cosmetic trail. */
    private final Map<UUID, Long> dashing = new ConcurrentHashMap<>();

    public RabbitDashPower(String id) {
        super(id);
    }

    @Override
    public void onActivate(Player player) {
        // Dash along the look direction on the horizontal plane, with a small hop so it clears
        // edges and reads like a rabbit's leap — never a vertical launch.
        Vector look = player.getEyeLocation().getDirection();
        Vector flat = new Vector(look.getX(), 0, look.getZ());
        if (flat.lengthSquared() < 1.0e-6) {
            flat = new Vector(0, 0, 1); // looking straight up/down → default forward
        }
        flat.normalize().multiply(DASH_SPEED);
        player.setVelocity(new Vector(flat.getX(), 0.4, flat.getZ()));
        dashing.put(player.getUniqueId(), System.currentTimeMillis());

        Location feet = player.getLocation();
        player.getWorld().playSound(feet, Sound.ENTITY_RABBIT_JUMP, 1.0f, 1.2f);
        ParticleUtil.spawnGroundBurst(Particle.CLOUD, feet, 0.5, 8, 0.05);
        ParticleUtil.spawnGroundBurst(Particle.POOF, feet, 0.4, 6, 0.05);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Long time = dashing.get(player.getUniqueId());
        if (time == null) {
            return;
        }
        // End the trail once the hop settles back on the ground (or after a short safety window).
        if (System.currentTimeMillis() - time > 200L && GroundUtil.isOnGround(player)) {
            dashing.remove(player.getUniqueId());
        } else {
            ParticleUtil.spawnTrail(Particle.CLOUD, player.getLocation().add(0, 0.2, 0), 1, 0.15);
        }
    }

    /**
     * Coelho balance: while mid-dash a spear hit can't exceed 8 (4 hearts), so the
     * dash + spear charge-attack combo can't burst players down.
     */
    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        if (dashing.containsKey(player.getUniqueId()) && isHoldingSpear(player) && e.getDamage() > 8.0) {
            e.setDamage(8.0);
        }
    }

    @Override
    public void onRemove(Player player) {
        dashing.remove(player.getUniqueId());
    }

    @Override
    public long getCooldownTicks(Player player) {
        return COOLDOWN_TICKS;
    }
}
