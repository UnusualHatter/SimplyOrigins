package dev.originspaper.power.origins.demon;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/** Active skill: Strength I for 10s, then Hunger and Weakness for 5s. */
public class HellPactPower extends AbstractPower implements ActivePowerType {

    public HellPactPower(String id) {
        super(id);
    }

    @Override
    public void onActivate(Player player) {
        EffectUtil.apply(player, PotionEffectType.STRENGTH, 200, 0); // Strength I for 10s
        
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline() && player.isValid()) {
                    EffectUtil.apply(player, PotionEffectType.WEAKNESS, 100, 0);
                    EffectUtil.apply(player, PotionEffectType.HUNGER, 100, 0);
                }
            }
        }.runTaskLater(plugin(), 200L);
    }

    @Override
    public long getCooldownTicks() {
        return 600L; // 30 seconds cooldown
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.STRENGTH);
        EffectUtil.clear(player, PotionEffectType.WEAKNESS);
        EffectUtil.clear(player, PotionEffectType.HUNGER);
    }
}
