package dev.originspaper.power.origins.owl;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.TextUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/** Reveals nearby living entities with Glowing effect. */
public class EcholocationPower extends AbstractPower implements ActivePowerType {

    public EcholocationPower(String id) {
        super(id);
    }

    @Override
    public void onActivate(Player player) {
        int count = 0;
        for (LivingEntity entity : player.getWorld().getLivingEntities()) {
            if (entity.equals(player)) continue;
            
            if (entity.getLocation().getWorld().equals(player.getWorld()) && entity.getLocation().distanceSquared(player.getLocation()) <= 40 * 40) {
                entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0, false, false, false));
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
