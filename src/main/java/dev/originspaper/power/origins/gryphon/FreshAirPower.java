package dev.originspaper.power.origins.gryphon;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;

/** Needs fresh, high air: cannot sleep below a minimum altitude; trails wind while soaring. */
public class FreshAirPower extends AbstractPower {

    private static final int MIN_Y = 86;
    private static final int HIGH_ALTITUDE_Y = 160;

    public FreshAirPower(String id) {
        super(id);
    }

    @Override
    public void onBedEnter(PlayerBedEnterEvent e) {
        if (e.getBed().getY() < MIN_Y) {
            e.setCancelled(true);
            e.getPlayer().sendActionBar(TextUtil.msg("§cO ar aqui é denso demais. Durma a 86 blocos ou mais."));
        }
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
