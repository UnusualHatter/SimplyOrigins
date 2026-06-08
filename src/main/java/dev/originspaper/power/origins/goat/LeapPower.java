package dev.originspaper.power.origins.goat;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.power.shared.AbstractPower;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/** Active skill: a strong forward leap. */
public class LeapPower extends AbstractPower implements ActivePowerType {

    public LeapPower(String id) {
        super(id);
    }

    @Override
    public void onActivate(Player player) {
        Vector dir = player.getLocation().getDirection().multiply(1.8).setY(0.5);
        player.setVelocity(dir);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GOAT_LONG_JUMP, 1.0f, 1.0f);
    }

    @Override
    public long getCooldownTicks() {
        return 60L;
    }
}
