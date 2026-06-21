package dev.originspaper.power.origins.wolf;

import dev.originspaper.power.shared.ActiveBuffPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/** Alpha's Howl: a predatory shockwave on activation, a menacing aura while the buff lasts. */
public class AlphaHowlPower extends ActiveBuffPower {

    public AlphaHowlPower(String id, long cooldownTicks, Sound sound, PotionEffect... effects) {
        super(id, cooldownTicks, sound, effects);
    }

    /** Current origin level, null-safe (1 if the player's data isn't loaded yet). */
    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public long getCooldownTicks(Player player) {
        long base = super.getCooldownTicks(player);
        if (levelOf(player) >= 6) { // Líder: recarga reduzida
            return (long) (base * 0.7);
        }
        return base;
    }

    @Override
    public void onActivate(Player player) {
        super.onActivate(player); // applies the buffs + plays the howl sound
        Location c = player.getLocation();

        if (levelOf(player) >= 10) { // Alcateia: fortalece aliados
            for (Entity entity : player.getNearbyEntities(8, 8, 8)) {
                if (entity instanceof Player ally && !ally.equals(player)) {
                    EffectUtil.apply(ally, PotionEffectType.STRENGTH, 200, 0);
                    ParticleUtil.spawnGroundBurst(Particle.HEART, ally.getLocation(), 0.5, 3, 0.1);
                } else if (entity instanceof Tameable pet && pet.isTamed() && player.equals(pet.getOwner())) {
                    ((LivingEntity) pet).addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 0, true, false, false));
                    ParticleUtil.spawnGroundBurst(Particle.HEART, pet.getLocation(), 0.5, 3, 0.1);
                }
            }
        }

        // Three concentric cloud rings read as a single expanding shockwave.
        for (double r = 1.5; r <= 3.5; r += 1.0) {
            ParticleUtil.spawnRing(Particle.CLOUD, c.clone().add(0, 0.2, 0), r, 20, 0.0);
        }
        ParticleUtil.spawn(Particle.SONIC_BOOM, c.clone().add(0, 1.0, 0), 1, 0, 0, 0, 0.0);
        ParticleUtil.spawnGroundBurst(Particle.CRIT, c.clone().add(0, 1.0, 0), 1.0, 25, 0.4);
    }

    @Override
    public void onTick(Player player) {
        if (!isBuffActive(player)) {
            return;
        }
        Location c = player.getLocation().add(0, 1.2, 0);
        ParticleUtil.spawnTrail(Particle.DAMAGE_INDICATOR, c, 3, 0.4);
        ParticleUtil.spawnTrail(Particle.ANGRY_VILLAGER, c, 2, 0.4);
    }
}
