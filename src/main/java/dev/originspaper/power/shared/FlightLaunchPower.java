package dev.originspaper.power.shared;

import dev.originspaper.api.ActivePowerType;
import dev.originspaper.util.GroundUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/** Active skill: launches the player upward and starts gliding shortly after. */
public class FlightLaunchPower extends AbstractPower implements ActivePowerType {

    private final long cooldownTicks;
    private final double upwardVelocity;
    private final long glideDelay;

    public FlightLaunchPower(String id, long cooldownTicks, double upwardVelocity, long glideDelay) {
        super(id);
        this.cooldownTicks = cooldownTicks;
        this.upwardVelocity = upwardVelocity;
        this.glideDelay = glideDelay;
    }

    @Override
    public void onActivate(Player player) {
        player.setVelocity(player.getVelocity().setY(upwardVelocity));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);
        plugin().getServer().getScheduler().runTaskLater(plugin(), () -> {
            if (!GroundUtil.isOnGround(player) && player.getInventory().getChestplate() != null) {
                player.setGliding(true);
            }
        }, glideDelay);
    }

    @Override
    public long getCooldownTicks() {
        return cooldownTicks;
    }
}
