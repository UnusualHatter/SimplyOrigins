package dev.originspaper.integration;

import dev.originspaper.OriginsPaper;
import dev.originspaper.util.PerfStats;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

/**
 * GriefPrevention-backed claim checks. References GP types directly, so it must only be
 * instantiated by {@link GriefPreventionHook} after the plugin is confirmed present.
 *
 * <p>Protection fails <b>closed</b>: if a claim lookup blows up, the action is denied rather than
 * letting an ability bypass land protection.
 */
class GriefPreventionClaimService implements ClaimProtectionService {

    private final OriginsPaper plugin;
    private final boolean protectAoe;
    private final boolean blockHostileEffects;
    private final boolean allowOwnClaims;
    private final boolean allowAdminClaims;

    GriefPreventionClaimService(OriginsPaper plugin, boolean protectAoe, boolean blockHostileEffects,
                                boolean allowOwnClaims, boolean allowAdminClaims) {
        this.plugin = plugin;
        this.protectAoe = protectAoe;
        this.blockHostileEffects = blockHostileEffects;
        this.allowOwnClaims = allowOwnClaims;
        this.allowAdminClaims = allowAdminClaims;
    }

    @Override
    public boolean canDamage(Player source, LivingEntity target) {
        if (!protectAoe) {
            return true;
        }
        // Hostile mobs are never claim-protected (mirrors GP, which guards animals/pets, not monsters).
        if (target instanceof Monster) {
            return true;
        }
        return allowedAt(source, target.getLocation());
    }

    @Override
    public boolean canAffectPlayer(Player source, Player target) {
        if (!blockHostileEffects) {
            return true;
        }
        return allowedAt(source, target.getLocation());
    }

    @Override
    public boolean canModifyBlock(Player source, Block block) {
        return allowedAt(source, block.getLocation());
    }

    /** Unclaimed → allowed; admin claim → config; claim where {@code source} may build → config. */
    private boolean allowedAt(Player source, Location where) {
        PerfStats.claimCheck();
        try {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(where, true, null);
            if (claim == null) {
                return true;
            }
            if (claim.isAdminClaim()) {
                if (!allowAdminClaims) {
                    PerfStats.claimDenial();
                }
                return allowAdminClaims;
            }
            if (claim.checkPermission(source, ClaimPermission.Build, null) == null) {
                if (!allowOwnClaims) {
                    PerfStats.claimDenial();
                }
                return allowOwnClaims;
            }
            PerfStats.claimDenial();
            return false;
        } catch (Throwable t) {
            plugin.log().warn("GriefPrevention claim lookup failed — denying origin ability (fail closed).", t);
            PerfStats.claimDenial();
            return false;
        }
    }
}
