package dev.originspaper.integration;

import dev.originspaper.OriginsPaper;

/**
 * GriefPrevention integration: claim-aware target filtering for area/hostile abilities. This class
 * holds no GriefPrevention imports; {@link GriefPreventionClaimService} (which does) is only
 * instantiated once the plugin is confirmed present.
 */
public class GriefPreventionHook extends AbstractIntegrationHook {

    private ClaimProtectionService service = ClaimProtectionService.ALLOW_ALL;

    GriefPreventionHook(OriginsPaper plugin) {
        super(plugin, "GriefPrevention", "GriefPrevention", "griefprevention");
    }

    @Override
    protected void onEnable() {
        var cfg = plugin.getConfig();
        String base = "integrations.griefprevention.";
        service = new GriefPreventionClaimService(plugin,
                cfg.getBoolean(base + "protect-claims-from-origin-aoe", true),
                cfg.getBoolean(base + "block-hostile-origin-effects-in-other-claims", true),
                cfg.getBoolean(base + "allow-effects-in-own-claims", true),
                cfg.getBoolean(base + "allow-effects-in-admin-claims", false));
        plugin.log().info("GriefPrevention detected — origin abilities will respect land claims.");
    }

    @Override
    protected void onDisable() {
        service = ClaimProtectionService.ALLOW_ALL;
    }

    /** The active claim checks, or {@code ALLOW_ALL} while the hook is inactive. */
    ClaimProtectionService service() {
        return isActive() ? service : ClaimProtectionService.ALLOW_ALL;
    }
}
