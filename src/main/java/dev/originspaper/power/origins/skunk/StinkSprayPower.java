package dev.originspaper.power.origins.skunk;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.PerfStats;
import dev.originspaper.util.SafeTargetUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/** Active skill: a visible, non-damaging debuff cone used to disengage from nearby threats. */
public class StinkSprayPower extends AbstractPower implements ActivePowerType {

    private static final double CONE_DOT = 0.45;

    private final long cooldownTicks;
    private final double baseRadius;
    private final int playerSlownessTicks;
    private final int playerWeaknessTicks;
    private final int playerNauseaTicks;
    private final int mobSlownessTicks;
    private final int mobWeaknessTicks;
    private final int mobNauseaTicks;

    public StinkSprayPower(String id, long cooldownTicks, double baseRadius,
                           int playerSlownessTicks, int playerWeaknessTicks, int playerNauseaTicks,
                           int mobSlownessTicks, int mobWeaknessTicks, int mobNauseaTicks) {
        super(id);
        this.cooldownTicks = cooldownTicks;
        this.baseRadius = baseRadius;
        this.playerSlownessTicks = playerSlownessTicks;
        this.playerWeaknessTicks = playerWeaknessTicks;
        this.playerNauseaTicks = playerNauseaTicks;
        this.mobSlownessTicks = mobSlownessTicks;
        this.mobWeaknessTicks = mobWeaknessTicks;
        this.mobNauseaTicks = mobNauseaTicks;
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onActivate(Player player) {
        double radius = baseRadius + (levelOf(player) >= 10 ? 2.0 : 0.0);
        Location origin = player.getEyeLocation();
        Vector direction = origin.getDirection().normalize();
        boolean hit = false;

        PerfStats.entityScan();
        for (Entity entity : player.getWorld().getNearbyEntities(origin, radius, radius, radius)) {
            if (!(entity instanceof LivingEntity target) || entity.equals(player)
                    || SafeTargetUtil.isProtected(target) || !mayAffect(player, target)) {
                continue;
            }
            Vector offset = target.getEyeLocation().toVector().subtract(origin.toVector());
            double distance = offset.length();
            if (distance > radius || distance < 1.0e-6
                    || offset.multiply(1.0 / distance).dot(direction) < CONE_DOT) {
                continue;
            }
            applyDebuffs(target);
            hit = true;
        }

        player.getWorld().playSound(origin, Sound.ENTITY_LLAMA_SPIT, 1.0f, 0.55f);
        ParticleUtil.spawnCone(Particle.DUST, origin, direction, radius, radius * 0.45,
                0.45, 5, 0.01, ParticleUtil.dust(Color.fromRGB(150, 145, 45), 1.25f));
        ParticleUtil.spawnCone(Particle.ASH, origin, direction, radius, radius * 0.35,
                0.5, 3, 0.01);
        if (hit) {
            plugin().progression().awardXp(player, 5);
        }
    }

    private boolean mayAffect(Player source, LivingEntity target) {
        if (target instanceof Player targetPlayer) {
            return claims().canAffectPlayer(source, targetPlayer);
        }
        return claims().canDamage(source, target);
    }

    private void applyDebuffs(LivingEntity target) {
        boolean playerTarget = target instanceof Player;
        apply(target, PotionEffectType.SLOWNESS,
                playerTarget ? playerSlownessTicks : mobSlownessTicks);
        apply(target, PotionEffectType.WEAKNESS,
                playerTarget ? playerWeaknessTicks : mobWeaknessTicks);
        apply(target, PotionEffectType.NAUSEA,
                playerTarget ? playerNauseaTicks : mobNauseaTicks);
    }

    private static void apply(LivingEntity target, PotionEffectType type, int ticks) {
        if (ticks > 0) {
            target.addPotionEffect(new PotionEffect(type, ticks, 0, true, false, false));
        }
    }

    @Override
    public long getCooldownTicks(Player player) {
        return levelOf(player) >= 6 ? (long) (cooldownTicks * 0.7) : cooldownTicks;
    }
}
