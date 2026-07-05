package dev.originspaper.power.origins.demon;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Active skill: Strength I for 10s, then Hunger and Weakness for 5s. */
public class HellPactPower extends AbstractPower implements ActivePowerType {

    private static final long STRENGTH_SECONDS = 10L;
    private static final long DRAWBACK_END_SECONDS = 15L; // 10s buff + 5s drawback

    /**
     * Per-player pact state, driven entirely by the 1/s global tick — no scheduler callback that
     * could outlive the power (quit, origin switch, reload) or hold a stale Player reference.
     */
    private static final class Pact {
        final long startedAt; // OriginsPaper#tick seconds
        boolean drawbackApplied;

        Pact(long startedAt) {
            this.startedAt = startedAt;
        }
    }

    private final Map<UUID, Pact> pacts = new ConcurrentHashMap<>();

    public HellPactPower(String id) {
        super(id);
    }

    @Override
    public void onActivate(Player player) {
        EffectUtil.apply(player, PotionEffectType.STRENGTH, 200, 0); // Strength I for 10s
        pacts.put(player.getUniqueId(), new Pact(plugin().tick()));

        Location c = player.getLocation();
        player.getWorld().playSound(c, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.6f);
        for (double y = 0.0; y <= 2.2; y += 0.3) { // vertical soul-fire pillar
            ParticleUtil.spawnTrail(Particle.SOUL_FIRE_FLAME, c.clone().add(0, y, 0), 4, 0.15);
        }
        ParticleUtil.spawnGroundBurst(Particle.LAVA, c, 0.6, 6, 0.1);
        ParticleUtil.spawnGroundBurst(Particle.SMOKE, c, 0.5, 4, 0.02);
    }

    @Override
    public void onTick(Player player) {
        Pact pact = pacts.get(player.getUniqueId());
        if (pact == null) {
            return;
        }
        long elapsed = plugin().tick() - pact.startedAt;
        Location c = player.getLocation().add(0, 1.0, 0);
        if (elapsed < STRENGTH_SECONDS) {
            // Orbiting infernal aura; the start angle rotates each second for a sense of spin.
            double offset = elapsed * 0.7;
            for (int i = 0; i < 6; i++) {
                double a = offset + i * Math.PI / 3;
                ParticleUtil.spawnTrail(Particle.SOUL_FIRE_FLAME,
                        c.clone().add(Math.cos(a) * 0.8, 0, Math.sin(a) * 0.8), 1, 0.0);
            }
        } else if (elapsed < DRAWBACK_END_SECONDS) {
            // The pact comes due: one-shot Weakness + Hunger, ">= " so a lag-skipped second
            // can't silently skip the drawback.
            if (!pact.drawbackApplied) {
                pact.drawbackApplied = true;
                EffectUtil.apply(player, PotionEffectType.WEAKNESS, 100, 0);
                EffectUtil.apply(player, PotionEffectType.HUNGER, 100, 0);
            }
            ParticleUtil.spawnTrail(Particle.ASH, c, 6, 0.4);
            ParticleUtil.spawnTrail(Particle.LARGE_SMOKE, c, 2, 0.3);
            if (elapsed % 2 == 0) {
                ParticleUtil.spawnTrail(Particle.DRIPPING_LAVA, c, 2, 0.3);
            }
        } else {
            pacts.remove(player.getUniqueId());
        }
    }

    @Override
    public long getCooldownTicks(Player player) {
        return 600L; // 30 seconds cooldown
    }

    @Override
    public void onRemove(Player player) {
        pacts.remove(player.getUniqueId());
        EffectUtil.clear(player, PotionEffectType.STRENGTH);
        EffectUtil.clear(player, PotionEffectType.WEAKNESS);
        EffectUtil.clear(player, PotionEffectType.HUNGER);
    }
}
