package dev.originspaper.power.origins.rat;

import dev.originspaper.power.shared.ActiveBuffPower;
import dev.originspaper.registry.PlayerOriginData;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

/** Rat's Scurry dash. Nv6 ("Fôlego de Rato") shortens its cooldown by 30%. */
public class ScurryPower extends ActiveBuffPower {

    public ScurryPower(String id, long cooldownTicks, Sound sound, PotionEffect... effects) {
        super(id, cooldownTicks, sound, effects);
    }

    @Override
    public long getCooldownTicks(Player player) {
        long base = super.getCooldownTicks(player);
        PlayerOriginData data = plugin().data().get(player.getUniqueId());
        if (data != null && data.level() >= 6) {
            return (long) (base * 0.7);
        }
        return base;
    }
}
