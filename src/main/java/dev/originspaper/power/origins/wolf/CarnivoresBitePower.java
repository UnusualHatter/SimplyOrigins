package dev.originspaper.power.origins.wolf;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.FoodUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.UUID;

/** Eating meat restores an extra half-heart. */
public class CarnivoresBitePower extends AbstractPower {

    public CarnivoresBitePower(String id) {
        super(id);
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (!FoodUtil.isMeat(e.getItem().getType())) {
            return;
        }
        Player player = e.getPlayer();
        ParticleUtil.spawnTrail(Particle.HEART, player.getLocation().add(0, 1.0, 0), 3, 0.3);
        ParticleUtil.spawnTrail(Particle.HAPPY_VILLAGER, player.getLocation().add(0, 1.0, 0), 2, 0.3);
        UUID playerId = player.getUniqueId();
        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            Player resolved = resolveActivePlayer(playerId);
            if (resolved == null || resolved.isDead()) {
                return;
            }
            var maxAttr = resolved.getAttribute(Attribute.MAX_HEALTH);
            double max = maxAttr == null ? 20.0 : maxAttr.getValue();
            resolved.setHealth(Math.min(max, resolved.getHealth() + 1.0)); // half a heart
        }, 1L);
    }
}
