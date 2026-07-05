package dev.originspaper.integration;

import dev.originspaper.OriginsPaper;

/**
 * WorldGuard detection stub. Region-flag checks (mirroring the GriefPrevention claim checks) are
 * planned but not wired yet; detection now means servers see the correct status in
 * {@code /origin debug integrations}.
 */
public class WorldGuardHook extends AbstractIntegrationHook {

    WorldGuardHook(OriginsPaper plugin) {
        super(plugin, "WorldGuard", "WorldGuard", "worldguard");
    }

    @Override
    protected void onEnable() {
        plugin.log().info("WorldGuard detected — region checks not implemented yet.");
    }
}
