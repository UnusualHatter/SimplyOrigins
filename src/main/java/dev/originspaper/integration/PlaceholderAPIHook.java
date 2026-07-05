package dev.originspaper.integration;

import dev.originspaper.OriginsPaper;

/**
 * PlaceholderAPI detection stub. Registering an expansion (e.g. {@code %originspaper_origin%},
 * {@code %originspaper_level%}) needs the PAPI API as a compile dependency — planned, not wired
 * yet. Detection now means servers see the correct status in {@code /origin debug integrations}.
 */
public class PlaceholderAPIHook extends AbstractIntegrationHook {

    PlaceholderAPIHook(OriginsPaper plugin) {
        super(plugin, "PlaceholderAPI", "PlaceholderAPI", "placeholderapi");
    }

    @Override
    protected void onEnable() {
        plugin.log().info("PlaceholderAPI detected — placeholder expansion not implemented yet.");
    }
}
