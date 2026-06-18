package dev.originspaper.util;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;

/** Decides which entities untargeted area damage (dash blasts, etc.) must spare. */
public final class SafeTargetUtil {

    private SafeTargetUtil() {}

    /**
     * True for entities that should never be hit by accidental area damage: tamed pets (dogs, cats,
     * horses, llamas, parrots…) and any name-tagged mob the player deliberately kept.
     */
    public static boolean isProtected(LivingEntity entity) {
        if (entity instanceof Tameable tameable && tameable.isTamed()) {
            return true;
        }
        return entity.customName() != null;
    }
}
