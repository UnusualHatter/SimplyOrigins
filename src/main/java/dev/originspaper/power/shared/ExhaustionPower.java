package dev.originspaper.power.shared;

import org.bukkit.entity.Player;

/** Adds extra exhaustion each tick cycle (large appetite / metabolism). */
public class ExhaustionPower extends AbstractPower {

    private final float amountPerCycle;
    private final boolean nightOnly;

    public ExhaustionPower(String id, float amountPerCycle, boolean nightOnly) {
        super(id);
        this.amountPerCycle = amountPerCycle;
        this.nightOnly = nightOnly;
    }

    @Override
    public void onTick(Player player) {
        if (nightOnly && !NightTimeEffectPower.isNight(player)) {
            return;
        }
        player.setExhaustion(player.getExhaustion() + amountPerCycle);
    }
}
