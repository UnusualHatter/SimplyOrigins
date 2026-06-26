package dev.originspaper.power.origins.goat;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.GroundUtil;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.SafeTargetUtil;
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

/** Active skill: a flat, headbutt-like charge that detonates a small blast on impact or at the end. */
public class LeapPower extends AbstractPower implements ActivePowerType {

    private static final double BLAST_RADIUS = 1.5;
    private static final double BLAST_DAMAGE = 5.0;
    private static final double HIT_RADIUS = 1.2; // how close to the charge path counts as a headbutt

    /** Nv10 "Avalanche": the blast reaches a block farther. */
    private double blastRadius(Player player) {
        return BLAST_RADIUS + (levelOf(player) >= 10 ? 1.0 : 0.0);
    }

    /** Nv3 "Crânio Duro": each headbutt hits harder. */
    private double blastDamage(Player player) {
        return BLAST_DAMAGE + (levelOf(player) >= 3 ? 2.0 : 0.0);
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    private final Map<UUID, Long> leaping = new ConcurrentHashMap<>();

    public LeapPower(String id) {
        super(id);
    }

    @Override
    public void onActivate(Player player) {
        // Straight headbutt charge: follow the look direction on the horizontal plane, no lift.
        Vector look = player.getEyeLocation().getDirection();
        Vector flat = new Vector(look.getX(), 0, look.getZ());
        if (flat.lengthSquared() < 1.0e-6) {
            flat = new Vector(0, 0, 1); // looking straight up/down → default forward
        }
        flat.normalize().multiply(2.2);
        player.setVelocity(new Vector(flat.getX(), 0.1, flat.getZ())); // tiny Y so it skims, never lifts

        leaping.put(player.getUniqueId(), System.currentTimeMillis());
        Location feet = player.getLocation();
        player.getWorld().playSound(feet, Sound.ENTITY_GOAT_LONG_JUMP, 1.0f, 1.0f);
        ParticleUtil.spawnGroundBurst(Particle.POOF, feet, 0.5, 6, 0.05);
        ParticleUtil.spawnGroundBurst(Particle.CLOUD, feet, 0.5, 8, 0.05);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Long time = leaping.get(player.getUniqueId());
        if (time == null) {
            return;
        }
        // Headbutt: sweep the whole move segment so a fast charge can't tunnel past a target.
        LivingEntity hit = findTargetAlong(e.getFrom(), e.getTo(), player);
        if (hit != null) {
            leaping.remove(player.getUniqueId());
            player.setVelocity(new Vector(0, 0, 0)); // recoil to a halt on impact
            blastAt(hit.getLocation(), player); // detonate on the entity we ran into
            return;
        }
        // Otherwise detonate when the charge runs its course and the goat is back on the ground.
        if (System.currentTimeMillis() - time > 500L && GroundUtil.isOnGround(player)) {
            leaping.remove(player.getUniqueId());
            blastAt(player.getLocation(), player);
        } else {
            Object below = player.getLocation().clone().subtract(0, 0.2, 0).getBlock().getBlockData();
            ParticleUtil.spawnTrail(Particle.FALLING_DUST, player.getLocation().add(0, 0.2, 0), 2, 0.2, below);
        }
    }

    /** Closest living entity within {@link #HIT_RADIUS} of the path travelled this tick, if any. */
    private LivingEntity findTargetAlong(Location from, Location to, Player player) {
        Vector a = from.toVector();
        Vector seg = to.toVector().subtract(a);
        double segLen2 = seg.lengthSquared();
        double search = blastRadius(player) + Math.sqrt(segLen2) + 1.0;
        LivingEntity best = null;
        double bestDist2 = Double.MAX_VALUE;
        for (Entity entity : to.getWorld().getNearbyEntities(to, search, search, search)) {
            if (!(entity instanceof LivingEntity living) || entity.equals(player)
                    || SafeTargetUtil.isProtected(living)) {
                continue; // charge passes through pets/named mobs instead of stopping on them
            }
            Vector p = entity.getLocation().toVector();
            double t = segLen2 < 1.0e-6 ? 0.0 : p.clone().subtract(a).dot(seg) / segLen2;
            t = Math.max(0.0, Math.min(1.0, t));
            double d2 = a.clone().add(seg.clone().multiply(t)).distanceSquared(p);
            if (d2 <= HIT_RADIUS * HIT_RADIUS && d2 < bestDist2) {
                bestDist2 = d2;
                best = living;
            }
        }
        return best;
    }

    /** A small, block-safe explosion centred on {@code center}: damages nearby entities only. */
    private void blastAt(Location center, Player owner) {
        World world = center.getWorld();
        world.playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 0.7f, 1.2f);
        ParticleUtil.spawn(Particle.EXPLOSION, center.clone().add(0, 0.5, 0), 1, 0, 0, 0, 0.0);
        Object below = center.clone().subtract(0, 0.2, 0).getBlock().getBlockData();
        ParticleUtil.spawnGroundBurst(Particle.BLOCK, center, 0.6, 12, 0.0, below);
        ParticleUtil.spawnGroundBurst(Particle.DUST_PLUME, center, 0.5, 6, 0.05);
        double radius = blastRadius(owner);
        double damage = blastDamage(owner);
        boolean hit = false;
        for (Entity entity : world.getNearbyEntities(center, radius, radius, radius)) {
            if (entity instanceof LivingEntity target && !entity.equals(owner)
                    && !SafeTargetUtil.isProtected(target)) {
                target.damage(damage, owner); // knockback radiates from the blast centre
                hit = true;
            }
        }
        if (hit) {
            plugin().progression().awardXp(owner, 8); // "Acertar Cabeçadas"
        }
    }

    /**
     * Cabra balance: while mid-charge a spear hit can't exceed 8 (4 hearts). The charge already
     * detonates its own blast, so a spear charge-attack on top shouldn't one-combo players.
     */
    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        if (leaping.containsKey(player.getUniqueId()) && isHoldingSpear(player) && e.getDamage() > 8.0) {
            e.setDamage(8.0);
        }
    }

    @Override
    public void onRemove(Player player) {
        leaping.remove(player.getUniqueId());
    }

    @Override
    public long getCooldownTicks(Player player) {
        return 60L;
    }
}
