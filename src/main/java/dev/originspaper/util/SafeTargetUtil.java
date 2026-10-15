package dev.originspaper.util;

import org.bukkit.GameRules;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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

    /**
     * Protection used by hostile/untargeted powers. In addition to pets and named mobs, players
     * are immune when vanilla PvP is disabled in their world.
     */
    public static boolean isProtectedFromHostile(Player source, LivingEntity target) {
        if (isProtected(target)) {
            return true;
        }
        return target instanceof Player
                && (!source.getWorld().equals(target.getWorld())
                        || !source.getWorld().getGameRuleValue(GameRules.PVP));
    }
}
