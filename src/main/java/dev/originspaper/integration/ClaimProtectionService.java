package dev.originspaper.integration;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Land-claim aware checks that area/hostile abilities run per target <b>before</b> acting, so an
 * ability degrades gracefully (skips protected targets) instead of being cancelled outright.
 *
 * <p>Backed by GriefPrevention when that plugin is present and the integration is enabled;
 * otherwise {@link #ALLOW_ALL} preserves the plugin's standalone behavior. These checks are
 * claim-only — {@code SafeTargetUtil} (pets/named mobs) applies regardless and stays separate.
 */
public interface ClaimProtectionService {

    /** May {@code source}'s area ability damage {@code target} where it stands? */
    boolean canDamage(Player source, LivingEntity target);

    /** May {@code source} apply a non-damage effect (glowing, buffs, debuffs) to {@code target}? */
    boolean canAffectPlayer(Player source, Player target);

    /** May {@code source}'s ability modify {@code block} (ignite, grow, break, place)? */
    boolean canModifyBlock(Player source, Block block);

    /** No protection plugin present: everything allowed (the plugin's standalone behavior). */
    ClaimProtectionService ALLOW_ALL = new ClaimProtectionService() {
        @Override
        public boolean canDamage(Player source, LivingEntity target) {
            return true;
        }

        @Override
        public boolean canAffectPlayer(Player source, Player target) {
            return true;
        }

        @Override
        public boolean canModifyBlock(Player source, Block block) {
            return true;
        }
    };
}
