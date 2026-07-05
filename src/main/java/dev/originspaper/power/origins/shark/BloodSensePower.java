package dev.originspaper.power.origins.shark;

import dev.originspaper.api.TickCadence;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.PerfStats;
import dev.originspaper.util.SafeTargetUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Senses blood in the water: while the shark is wet, injured creatures nearby glow and drip a red
 * scent trail. Injured players inside claims the shark isn't trusted in are never revealed
 * (hostile-effect claim rule).
 */
public class BloodSensePower extends AbstractPower {

    private static final Particle.DustOptions BLOOD =
            ParticleUtil.dust(Color.fromRGB(160, 10, 10), 1.2f);

    private final double radius;
    private final double healthFraction; // e.g. 0.6 = below 60% max health

    public BloodSensePower(String id, double radius, double healthFraction) {
        super(id);
        this.radius = radius;
        this.healthFraction = healthFraction;
    }

    @Override
    public TickCadence getTickCadence() {
        return TickCadence.EVERY_2_SECONDS;
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onTick(Player player) {
        if (!isWet(player)) {
            return;
        }
        // Nv10 "Terror dos Mares": the scent carries farther.
        double range = radius + (levelOf(player) >= 10 ? 8.0 : 0.0);
        PerfStats.entityScan();
        for (LivingEntity entity : player.getWorld()
                .getNearbyEntitiesByType(LivingEntity.class, player.getLocation(), range)) {
            if (entity.equals(player) || SafeTargetUtil.isProtectedFromHostile(player, entity)
                    || !isInjured(entity)) {
                continue;
            }
            if (entity instanceof Player target && !claims().canAffectPlayer(player, target)) {
                continue; // never reveal players protected by their claims
            }
            entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 50, 0, true, false, false));
            Location loc = entity.getLocation().add(0, entity.getHeight() * 0.6, 0);
            ParticleUtil.spawnTrail(Particle.DUST, loc, 4, 0.25, BLOOD);
        }
    }

    private boolean isInjured(LivingEntity entity) {
        AttributeInstance max = entity.getAttribute(Attribute.MAX_HEALTH);
        if (max == null || max.getValue() <= 0) {
            return false;
        }
        return entity.getHealth() / max.getValue() < healthFraction;
    }

    static boolean isWet(Player player) {
        var block = player.getLocation().getBlock();
        return player.isInWater()
                || (player.getWorld().hasStorm() && block.getLightFromSky() == 15
                && block.getHumidity() > 0.0);
    }
}
