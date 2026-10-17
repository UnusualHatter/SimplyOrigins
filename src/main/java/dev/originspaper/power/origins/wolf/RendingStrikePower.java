package dev.originspaper.power.origins.wolf;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.CooldownUtil;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.SafeTargetUtil;
import dev.originspaper.util.TextUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Soco Dilacerante (Nv5): a melee hit landed while crouching opens a wound that bleeds for a few
 * seconds. The crouch is the trigger, the cooldown lives in the persisted cooldown map like any
 * active skill, and the bleed ticks on the wolf's own 1 s tick.
 *
 * <p>Bleed pulses are plain damage with no attacker, so they skip armour, never re-enter this or any
 * other on-hit power, and give no knockback. {@code setKiller} keeps the kill credited to the wolf,
 * and each pulse lands regardless of the target's hurt cooldown.
 */
public class RendingStrikePower extends AbstractPower {

    private static final int UNLOCK_LEVEL = 5;
    private static final long COOLDOWN_TICKS = 240L;
    private static final int BLEED_SECONDS = 5;
    private static final double BLEED_DAMAGE = 1.0;
    private static final double BLEED_MAX_DISTANCE_SQUARED = 48.0 * 48.0;
    private static final Color BLOOD = Color.fromRGB(150, 8, 8);

    private record Bleed(UUID targetId, int secondsLeft) {}

    private final Map<UUID, Bleed> bleeds = new ConcurrentHashMap<>();

    public RendingStrikePower(String id) {
        super(id);
    }

    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player) || e.getCause() != DamageCause.ENTITY_ATTACK
                || !player.isSneaking() || !(e.getEntity() instanceof LivingEntity target)
                || target instanceof ArmorStand || levelOf(player) < UNLOCK_LEVEL) {
            return;
        }
        var data = plugin().data().get(player.getUniqueId());
        if (data == null) {
            return;
        }
        if (!CooldownUtil.isReady(data.getCooldowns(), id, COOLDOWN_TICKS)) {
            long remaining = CooldownUtil.remainingSeconds(data.getCooldowns(), id, COOLDOWN_TICKS);
            player.sendActionBar(TextUtil.msg("§7Soco Dilacerante em recarga: §f" + remaining + "s"));
            return;
        }
        CooldownUtil.trigger(data.getCooldowns(), id);
        bleeds.put(player.getUniqueId(), new Bleed(target.getUniqueId(), BLEED_SECONDS));

        Location loc = target.getLocation().add(0, target.getHeight() * 0.6, 0);
        player.getWorld().playSound(loc, Sound.ENTITY_WOLF_GROWL, 0.8f, 1.3f);
        player.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 0.8f);
        ParticleUtil.spawnTrail(Particle.DUST, loc, 10, 0.35, ParticleUtil.dust(BLOOD, 1.4f));
        ParticleUtil.spawnTrail(Particle.BLOCK, loc, 8, 0.3, Material.REDSTONE_BLOCK.createBlockData());
        player.sendActionBar(TextUtil.msg("§cSoco Dilacerante! §7O alvo está sangrando."));
    }

    /** One pulse per second. Protection is re-checked every pulse, like the shark's bite. */
    @Override
    public void onTick(Player player) {
        Bleed bleed = bleeds.get(player.getUniqueId());
        if (bleed == null) {
            return;
        }
        var entity = plugin().getServer().getEntity(bleed.targetId());
        if (!(entity instanceof LivingEntity target) || !target.isValid() || target.isDead()
                || !target.getWorld().equals(player.getWorld())
                || target.getLocation().distanceSquared(player.getLocation()) > BLEED_MAX_DISTANCE_SQUARED
                || SafeTargetUtil.isProtectedFromHostile(player, target)
                || !claims().canDamage(player, target)) {
            bleeds.remove(player.getUniqueId());
            return;
        }
        // The wolf's own melee keeps the target in hurt cooldown, which would swallow a 1-damage
        // pulse. Step around it and put the cooldown back exactly as it was.
        int cooldown = target.getNoDamageTicks();
        double lastDamage = target.getLastDamage();
        target.setKiller(player);
        target.setNoDamageTicks(0);
        target.damage(BLEED_DAMAGE);
        if (target.isValid() && !target.isDead()) {
            target.setNoDamageTicks(cooldown);
            target.setLastDamage(lastDamage);
        }
        Location loc = target.getLocation().add(0, target.getHeight() * 0.6, 0);
        ParticleUtil.spawnTrail(Particle.DUST, loc, 5, 0.25, ParticleUtil.dust(BLOOD, 1.1f));
        if (bleed.secondsLeft() <= 1) {
            bleeds.remove(player.getUniqueId());
        } else {
            bleeds.put(player.getUniqueId(), new Bleed(bleed.targetId(), bleed.secondsLeft() - 1));
        }
    }

    @Override
    public void onRemove(Player player) {
        bleeds.remove(player.getUniqueId());
    }
}
