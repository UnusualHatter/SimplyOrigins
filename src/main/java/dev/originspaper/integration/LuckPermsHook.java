package dev.originspaper.integration;

import dev.originspaper.OriginsPaper;

/**
 * LuckPerms detection stub. Permission-driven features (per-group origin allowlists, admin bypass
 * metadata) are planned but not wired yet; detection now means servers see the correct status in
 * {@code /origin debug integrations}.
 */
public class LuckPermsHook extends AbstractIntegrationHook {

    LuckPermsHook(OriginsPaper plugin) {
        super(plugin, "LuckPerms", "LuckPerms", "luckperms");
    }

    @Override
    protected void onEnable() {
        plugin.log().info("LuckPerms detected — permission integration not implemented yet.");
    }
}
