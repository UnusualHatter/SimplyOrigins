package dev.originspaper.power.origins.shark;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.PerfStats;
import dev.originspaper.util.SafeTargetUtil;
import dev.originspaper.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Active skill: a short underwater lunge that bites the first prey in its path. Water-gated via
 * {@link #canActivate} so a dry-land press never burns the cooldown. Modelled on the goat charge's
 * segment sweep so a fast dash can't tunnel through the target.
 */
public class FeedingRushPower extends AbstractPower implements ActivePowerType {

    private static final double DASH_SPEED = 1.8;
    private static final double HIT_RADIUS = 1.3;
    private static final long MAX_RUSH_MS = 1200L;

    private final long cooldownTicks;
    private final double damage;

    /** Players currently mid-rush (activation timestamp). */
    private final Map<UUID, Long> rushing = new ConcurrentHashMap<>();

    public FeedingRushPower(String id, long cooldownTicks, double damage) {
        super(id);
        this.cooldownTicks = cooldownTicks;
        this.damage = damage;
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public boolean canActivate(Player player) {
        if (player.isInWater()) {
            return true;
        }
        player.sendActionBar(TextUtil.msg("§cO Avanço Voraz só funciona dentro da água."));
        return false;
    }

    @Override
    public void onActivate(Player player) {
        player.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(DASH_SPEED));
        rushing.put(player.getUniqueId(), System.currentTimeMillis());

        Location c = player.getLocation();
        player.getWorld().playSound(c, Sound.ENTITY_DOLPHIN_ATTACK, 1.0f, 0.6f);
        ParticleUtil.spawn(Particle.BUBBLE_COLUMN_UP, c.clone().add(0, 0.5, 0), 20, 0.5, 0.5, 0.5, 0.02);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Long start = rushing.get(player.getUniqueId());
        if (start == null) {
            return;
        }
        if (System.currentTimeMillis() - start > MAX_RUSH_MS || !player.isInWater()) {
            rushing.remove(player.getUniqueId());
            return;
        }
        LivingEntity prey = findPreyAlong(e.getFrom(), e.getTo(), player);
        if (prey != null) {
            rushing.remove(player.getUniqueId());
            bite(player, prey);
            return;
        }
        ParticleUtil.spawnTrail(Particle.BUBBLE, player.getLocation().add(0, 0.4, 0), 4, 0.25);
    }

    /** Closest valid target within {@link #HIT_RADIUS} of the path travelled this tick, if any. */
    private LivingEntity findPreyAlong(Location from, Location to, Player player) {
        Vector a = from.toVector();
        Vector seg = to.toVector().subtract(a);
        double segLen2 = seg.lengthSquared();
        double search = HIT_RADIUS + Math.sqrt(segLen2) + 1.0;
        LivingEntity best = null;
        double bestDist2 = Double.MAX_VALUE;
        PerfStats.entityScan();
        for (Entity entity : to.getWorld().getNearbyEntities(to, search, search, search)) {
            if (!(entity instanceof LivingEntity living) || entity.equals(player)
                    || SafeTargetUtil.isProtected(living)
                    || !claims().canDamage(player, living)) {
                continue; // the rush glides past pets/named/claim-protected targets
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

    private void bite(Player player, LivingEntity prey) {
        // Nv10 "Terror dos Mares": the lunge bites harder.
        double total = damage + (levelOf(player) >= 10 ? 2.0 : 0.0);
        prey.damage(total, player);

        Vector away = prey.getLocation().toVector().subtract(player.getLocation().toVector());
        if (away.lengthSquared() < 1.0e-4) {
            away = player.getLocation().getDirection();
        }
        Vector knockback = away.normalize().multiply(1.1).setY(0.3);
        UUID sourceId = player.getUniqueId();
        UUID preyId = prey.getUniqueId();
        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            Player source = plugin().getServer().getPlayer(sourceId);
            var data = plugin().data().get(sourceId);
            if (source == null || data == null || data.getPowers().stream()
                    .noneMatch(power -> power.getId().equals(id))) {
                return;
            }
            Entity target = plugin().getServer().getEntity(preyId);
            if (target != null && target.isValid()) {
                target.setVelocity(knockback);
            }
        }, 1L);

        Location loc = prey.getLocation().add(0, prey.getHeight() * 0.6, 0);
        player.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EAT, 1.0f, 0.7f);
        ParticleUtil.spawnTrail(Particle.CRIT, loc, 8, 0.4);
        ParticleUtil.spawnTrail(Particle.BUBBLE_POP, loc, 12, 0.4);
        ParticleUtil.spawnTrail(Particle.DUST, loc, 8, 0.4,
                ParticleUtil.dust(org.bukkit.Color.fromRGB(160, 10, 10), 1.3f));
        plugin().progression().awardXp(player, 6); // "Atacar presas na água"
    }

    @Override
    public long getCooldownTicks(Player player) {
        // Nv6 "Frenesi": faster recharge.
        if (levelOf(player) >= 6) {
            return (long) (cooldownTicks * 0.7);
        }
        return cooldownTicks;
    }

    @Override
    public void onRemove(Player player) {
        rushing.remove(player.getUniqueId());
    }
}
