package dev.originspaper.power.origins.fox;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Focused hunt: repeatedly striking the same prey grants speed and strength. */
public class HuntPower extends AbstractPower {

    private record Track(UUID target, long time) {}

    private final Map<UUID, Track> lastHit = new ConcurrentHashMap<>();

    public HuntPower(String id) {
        super(id);
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        UUID targetId = e.getEntity().getUniqueId();
        long now = System.currentTimeMillis();
        Track prev = lastHit.get(player.getUniqueId());
        if (prev != null && prev.target().equals(targetId) && now - prev.time() < 3000L) {
            EffectUtil.apply(player, PotionEffectType.SPEED, 60, 0);
            EffectUtil.apply(player, PotionEffectType.STRENGTH, 60, 0);
        }
        lastHit.put(player.getUniqueId(), new Track(targetId, now));
    }

    @Override
    public void onRemove(Player player) {
        lastHit.remove(player.getUniqueId());
        EffectUtil.clear(player, PotionEffectType.SPEED);
        EffectUtil.clear(player, PotionEffectType.STRENGTH);
    }
}
