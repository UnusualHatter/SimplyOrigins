package dev.originspaper.power.origins.bat;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/** Active skill: gives a strong vertical boost to prolong glide. */
public class ProlongedGlidePower extends AbstractPower implements ActivePowerType {

    public ProlongedGlidePower(String id) {
        super(id);
    }

    @Override
    public void onActivate(Player player) {
        Vector dir = player.getLocation().getDirection().normalize().multiply(1.5);
        if (dir.getY() < 0.2) {
            dir.setY(0.2); // slight minimum upward lift
        }
        player.setVelocity(dir);
    }

    @Override
    public long getCooldownTicks() {
        return 200L; // 10 seconds
    }
}
