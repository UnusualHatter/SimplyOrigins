package dev.originspaper.power.shared;

import dev.originspaper.util.EffectUtil;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Applies a hidden effect only at night, clearing it during the day. */
public class NightTimeEffectPower extends AbstractPower {

    private final PotionEffectType type;
    private final int amplifier;

    public NightTimeEffectPower(String id, PotionEffectType type, int amplifier) {
        super(id);
        this.type = type;
        this.amplifier = amplifier;
    }

    public static boolean isNight(Player player) {
        long time = player.getWorld().getTime();
        return time >= 13000L && time <= 23000L;
    }

    @Override
    public void onTick(Player player) {
        if (isNight(player)) {
            EffectUtil.ensure(player, type, amplifier);
        } else {
            EffectUtil.clear(player, type);
        }
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, type);
    }
}
