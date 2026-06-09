package dev.originspaper.power.origins.wolf;

import dev.originspaper.power.shared.ActiveBuffPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

/** Alpha's Howl: a predatory shockwave on activation, a menacing aura while the buff lasts. */
public class AlphaHowlPower extends ActiveBuffPower {

    public AlphaHowlPower(String id, long cooldownTicks, Sound sound, PotionEffect... effects) {
        super(id, cooldownTicks, sound, effects);
    }

    @Override
    public void onActivate(Player player) {
        super.onActivate(player); // applies the buffs + plays the howl sound
        Location c = player.getLocation();
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
