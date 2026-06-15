package dev.originspaper.power.origins.owl;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/** Reveals nearby living entities with the Glowing effect — a magical sonar pulse. */
public class EcholocationPower extends AbstractPower implements ActivePowerType {

    private static final double RANGE = 12.0;

    public EcholocationPower(String id) {
        super(id);
    }

    @Override
    public void onActivate(Player player) {
        Location origin = player.getLocation().add(0, 1.0, 0);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 0.5f, 1.6f);
        // Three expanding sonar rings.
        for (double r = 2.0; r <= 6.0; r += 2.0) {
            ParticleUtil.spawnRing(Particle.SCULK_SOUL, origin, r, 24, 0.0);
            ParticleUtil.spawnRing(Particle.END_ROD, origin, r, 16, 0.0);
        }

        int count = 0;
        // getLivingEntities() is already scoped to the player's world, so no world check is needed.
        for (LivingEntity entity : player.getWorld().getLivingEntities()) {
            if (entity.equals(player)) {
                continue;
            }
            if (entity.getLocation().distanceSquared(player.getLocation()) <= RANGE * RANGE) {
                entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0, false, false, false));
                Location head = entity.getLocation().add(0, entity.getHeight() + 0.3, 0);
                ParticleUtil.spawnTrail(Particle.ENCHANT, head, 6, 0.2);
                ParticleUtil.spawn(Particle.END_ROD, head, 3, 0.1, 0.1, 0.1, 0.0);
                count++;
            }
        }
        player.sendActionBar(TextUtil.msg("§bEcolocalização revelou " + count + " entidade(s)!"));
    }

    @Override
    public long getCooldownTicks() {
        return 600L; // 30 seconds
    }
}
