package dev.originspaper.power.shared;

import dev.originspaper.util.EffectUtil;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/** Keeps a permanent, hidden potion effect applied, refreshing it each tick. */
public class PermanentEffectPower extends AbstractPower {

    private final PotionEffectType type;
    private final int amplifier;

    public PermanentEffectPower(String id, PotionEffectType type, int amplifier) {
        super(id);
        this.type = type;
        this.amplifier = amplifier;
    }

    @Override
    public void onApply(Player player) {
        EffectUtil.ensure(player, type, amplifier);
    }

    @Override
    public void onTick(Player player) {
        EffectUtil.ensure(player, type, amplifier);
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, type);
    }
}
