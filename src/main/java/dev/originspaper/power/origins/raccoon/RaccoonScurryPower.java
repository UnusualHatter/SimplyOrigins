package dev.originspaper.power.origins.raccoon;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ArmorUtil;
import dev.originspaper.util.AttributeUtil;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Active skill: a stealthy dart — Speed II plus a blink of Invisibility to break line of sight.
 * Attacking mid-scurry drops the disguise instantly, so it is an escape tool, not an ambush one.
 * Slower than the rat's Disparada, sneakier in exchange. Heavy chestplates spoil the stealth
 * (speed still applies).
 */
public class RaccoonScurryPower extends AbstractPower implements ActivePowerType {

    private final long cooldownTicks;
    private final int speedTicks;
    private final int invisibilityTicks;
    private final Map<UUID, Long> scurrying = new ConcurrentHashMap<>();

    public RaccoonScurryPower(String id, long cooldownTicks, int speedTicks, int invisibilityTicks) {
        super(id);
        this.cooldownTicks = cooldownTicks;
        this.speedTicks = speedTicks;
        this.invisibilityTicks = invisibilityTicks;
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onActivate(Player player) {
        UUID playerId = player.getUniqueId();
        long activation = System.nanoTime();
        scurrying.put(playerId, activation);
        AttributeUtil.set(player, Attribute.MOVEMENT_SPEED, id + ".speed", 0.40,
                AttributeModifier.Operation.ADD_SCALAR); // Speed II equivalent
        if (!ArmorUtil.isWearingHeavyChest(player)) {
            // Nv10 "Mestre da Malandragem": the vanishing act lasts three times longer.
            int invis = invisibilityTicks * (levelOf(player) >= 10 ? 3 : 1);
            EffectUtil.apply(player, PotionEffectType.INVISIBILITY, invis, 0);
        }
        long stateTicks = Math.max(speedTicks,
                ArmorUtil.isWearingHeavyChest(player) ? 0 : invisibilityTicks * (levelOf(player) >= 10 ? 3 : 1));
        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            if (scurrying.remove(playerId, activation)) {
                Player resolved = resolveActivePlayer(playerId);
                if (resolved != null) {
                    AttributeUtil.clear(resolved, Attribute.MOVEMENT_SPEED, id + ".speed");
                    EffectUtil.clearTemporary(resolved, PotionEffectType.INVISIBILITY,
                            Math.max(invisibilityTicks, (int) stateTicks), 0);
                }
            }
        }, Math.max(1L, stateTicks));
        Location c = player.getLocation();
        player.getWorld().playSound(c, Sound.ENTITY_CAT_HISS, 0.6f, 1.5f);
        ParticleUtil.spawnGroundBurst(Particle.SMOKE, c, 0.6, 12, 0.03);
        ParticleUtil.spawnGroundBurst(Particle.POOF, c, 0.4, 6, 0.03);
    }

    /** Attacking blows the cover: the invisibility ends on the spot (the dash speed runs out). */
    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        Player player = e.getDamager() instanceof Player direct ? direct
                : e.getDamager() instanceof Projectile projectile
                        && projectile.getShooter() instanceof Player shooter ? shooter : null;
        if (player != null && scurrying.containsKey(player.getUniqueId())) {
            EffectUtil.clearTemporary(player, PotionEffectType.INVISIBILITY,
                    invisibilityTicks * 3, 0);
        }
    }

    @Override
    public long getCooldownTicks(Player player) {
        // Nv6 "Pés Leves": faster recharge.
        if (levelOf(player) >= 6) {
            return (long) (cooldownTicks * 0.7);
        }
        return cooldownTicks;
    }

    @Override
    public void onRemove(Player player) {
        if (scurrying.remove(player.getUniqueId()) != null) {
            AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id + ".speed");
            EffectUtil.clearTemporary(player, PotionEffectType.INVISIBILITY,
                    Math.max(invisibilityTicks * 3, speedTicks), 0);
        }
    }
}
