package dev.originspaper.power.origins.gryphon;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.potion.PotionEffectType;

/** Needs fresh, high air: sleeps badly near the ground; trails wind while soaring. */
public class FreshAirPower extends AbstractPower {

    private static final int MIN_Y = 86;
    private static final int HIGH_ALTITUDE_Y = 160;
    private static final int RESTLESS_TICKS = 600; // ~30s of torpor after a low, dense-air sleep

    public FreshAirPower(String id) {
        super(id);
    }

    // Intentionally uses the deprecated BedEnterResult API — it's the reliable way to tell whether
    // the player will actually sleep before applying the low-altitude torpor.
    @SuppressWarnings("deprecation")
    @Override
    public void onBedEnter(PlayerBedEnterEvent e) {
        // Only react to a sleep that will actually happen, and only near the ground. High up, the
        // gryphon rests fine. Low down it can still sleep (so it can set spawn anywhere), but wakes
        // groggy — a real, unavoidable cost instead of a hard block that a tower trivially dodges.
        if (e.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK || e.getBed().getY() >= MIN_Y) {
            return;
        }
        Player p = e.getPlayer();
        EffectUtil.apply(p, PotionEffectType.WEAKNESS, RESTLESS_TICKS, 0);
        EffectUtil.apply(p, PotionEffectType.SLOWNESS, RESTLESS_TICKS, 0);
        EffectUtil.apply(p, PotionEffectType.MINING_FATIGUE, RESTLESS_TICKS, 0);
        p.sendActionBar(TextUtil.msg("§cO ar denso aqui embaixo te deixa grogue. Descanse no alto para evitar o torpor."));
    }

    @Override
    public void onTick(Player player) {
        if (player.isGliding()) {
            Location loc = player.getLocation();
            ParticleUtil.spawnTrail(Particle.WHITE_ASH, loc, 2, 0.3);
            ParticleUtil.spawnTrail(Particle.CLOUD, loc, 1, 0.2);
        }
        if (player.getLocation().getY() >= HIGH_ALTITUDE_Y && plugin().tick() % 2 == 0) {
            ParticleUtil.spawnTrail(Particle.END_ROD, player.getLocation().add(0, 0.5, 0), 2, 0.2);
        }
    }
}
