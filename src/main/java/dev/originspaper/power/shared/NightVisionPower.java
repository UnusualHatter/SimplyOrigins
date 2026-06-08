package dev.originspaper.power.shared;

import org.bukkit.potion.PotionEffectType;

/** Permanent night vision. */
public class NightVisionPower extends PermanentEffectPower {

    public NightVisionPower(String id) {
        super(id, PotionEffectType.NIGHT_VISION, 0);
    }
}
