package dev.originspaper.power.origins.otter;

import dev.originspaper.power.shared.AbstractPower;
import org.bukkit.entity.Player;

/** Buoyancy: stops sinking while submerged unless the player is sneaking to descend. */
public class LikeWaterPower extends AbstractPower {

    public LikeWaterPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (player.isUnderWater() && !player.isSneaking() && player.getVelocity().getY() < -0.05) {
            player.setVelocity(player.getVelocity().setY(player.getVelocity().getY() * 0.6));
        }
    }
}
