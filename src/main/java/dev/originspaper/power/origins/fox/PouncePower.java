package dev.originspaper.power.origins.fox;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Active skill: a leaping pounce — bonus damage on a mid-air hit, or a small blast on landing. */
public class PouncePower extends AbstractPower implements ActivePowerType {

    private static final double BLAST_RADIUS = 2.0;
    private static final double BLAST_DAMAGE = 3.0;

    private final Map<UUID, Long> pouncing = new ConcurrentHashMap<>();

    public PouncePower(String id) {
        super(id);
    }

    @Override
    public void onActivate(Player player) {
        Vector dir = player.getLocation().getDirection().multiply(1.5).setY(0.6);
        player.setVelocity(dir);
        pouncing.put(player.getUniqueId(), System.currentTimeMillis());

        Location feet = player.getLocation();
        Object below = feet.clone().subtract(0, 0.2, 0).getBlock().getBlockData();
        ParticleUtil.spawnGroundBurst(Particle.BLOCK, feet, 0.5, 10, 0.0, below);
        ParticleUtil.spawnGroundBurst(Particle.CLOUD, feet, 0.5, 8, 0.05);
        ParticleUtil.spawn(Particle.SWEEP_ATTACK, feet.clone().add(0, 0.8, 0), 1, 0, 0, 0, 0.0);
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        // Bonus damage on a hit landed while still airborne; consumes the pounce (no landing blast).
        if (pouncing.containsKey(player.getUniqueId())) {
            e.setDamage(e.getDamage() + 3.0);
            pouncing.remove(player.getUniqueId());
        }
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Long time = pouncing.get(player.getUniqueId());
        if (time == null) {
            return;
        }
        if (System.currentTimeMillis() - time > 500L && GroundUtil.isOnGround(player)) {
            pouncing.remove(player.getUniqueId());
            landingBlast(player);
        } else {
            // Airborne crit trail.
            ParticleUtil.spawnTrail(Particle.CRIT, player.getLocation().add(0, 0.5, 0), 2, 0.2);
        }
    }

    /** A small, block-safe explosion: damages nearby entities (no terrain damage). */
    private void landingBlast(Player player) {
        Location loc = player.getLocation();
        World world = player.getWorld();
        world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.6f, 1.4f);
        ParticleUtil.spawn(Particle.EXPLOSION, loc.clone().add(0, 0.5, 0), 1, 0, 0, 0, 0.0);
        Object below = loc.clone().subtract(0, 0.2, 0).getBlock().getBlockData();
        ParticleUtil.spawnGroundBurst(Particle.BLOCK, loc, 0.6, 12, 0.0, below);
        ParticleUtil.spawn(Particle.DAMAGE_INDICATOR, loc.clone().add(0, 0.5, 0), 4, 0.3, 0.2, 0.3, 0.0);
        for (Entity entity : player.getNearbyEntities(BLAST_RADIUS, BLAST_RADIUS, BLAST_RADIUS)) {
            if (entity instanceof LivingEntity target && !entity.equals(player)) {
                target.damage(BLAST_DAMAGE, player); // knockback radiates from the player (blast centre)
            }
        }
    }

    @Override
    public void onRemove(Player player) {
        pouncing.remove(player.getUniqueId());
    }

    @Override
    public long getCooldownTicks() {
        return 100L;
    }
}
