package dev.originspaper.power.origins.skunk;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.power.shared.NightTimeEffectPower;
import dev.originspaper.util.ArmorUtil;
import dev.originspaper.util.AttributeUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

/** Mild night/low-light movement identity, disabled by a noisy heavy chestplate. */
public class NightRoamerPower extends AbstractPower {

    private static final int LOW_LIGHT = 8;

    public NightRoamerPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        boolean dark = NightTimeEffectPower.isNight(player)
                || player.getLocation().getBlock().getLightLevel() < LOW_LIGHT;
        boolean active = dark && !player.isInWater() && !ArmorUtil.isWearingHeavyChest(player);
        AttributeUtil.sync(player, Attribute.MOVEMENT_SPEED, id, active ? 0.08 : 0.0,
                AttributeModifier.Operation.ADD_SCALAR);
        if (active && player.isSprinting()) {
            ParticleUtil.spawnTrail(Particle.ASH, player.getLocation().add(0, 0.2, 0), 2, 0.2);
        }
    }

    @Override
    public void onRemove(Player player) {
        AttributeUtil.clear(player, Attribute.MOVEMENT_SPEED, id);
    }
}
