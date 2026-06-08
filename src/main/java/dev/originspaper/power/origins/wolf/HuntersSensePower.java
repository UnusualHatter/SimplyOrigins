package dev.originspaper.power.origins.wolf;

import dev.originspaper.power.shared.AbstractPower;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/** Periodically highlights nearby living creatures with a glow. */
public class HuntersSensePower extends AbstractPower {

    public HuntersSensePower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (plugin().tick() % 2 != 0) {
            return; // every ~40 ticks
        }
        for (LivingEntity entity : player.getWorld().getNearbyEntitiesByType(LivingEntity.class, player.getLocation(), 16)) {
            if (entity.equals(player)) {
                continue;
            }
            entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0, true, false, false));
        }
    }
}
