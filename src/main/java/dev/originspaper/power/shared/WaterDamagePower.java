package dev.originspaper.power.shared;

import org.bukkit.entity.Player;

/** Hydrophobia: takes damage while in water or rain (checked from the 20-tick loop). */
public class WaterDamagePower extends AbstractPower {

    private final double damage;

    public WaterDamagePower(String id) {
        this(id, 1.0);
    }

    public WaterDamagePower(String id, double damage) {
        super(id);
        this.damage = damage;
    }

    @Override
    public void onTick(Player player) {
        if (player.isInWater() || player.isInRain()) {
            player.damage(damage);
        }
    }
}
