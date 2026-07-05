package dev.originspaper.integration;

import dev.originspaper.OriginsPaper;

/** Common lifecycle/state for integration hooks; subclasses implement {@link #onEnable()}/{@link #onDisable()}. */
abstract class AbstractIntegrationHook implements IntegrationHook {

    protected final OriginsPaper plugin;
    private final String name;
    private final String pluginName;
    private final String configKey;

    private boolean present;
    private boolean configEnabled = true;
    private boolean active;

    protected AbstractIntegrationHook(OriginsPaper plugin, String name, String pluginName, String configKey) {
        this.plugin = plugin;
        this.name = name;
        this.pluginName = pluginName;
        this.configKey = configKey;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String pluginName() {
        return pluginName;
    }

    @Override
    public String configKey() {
        return configKey;
    }

    @Override
    public boolean isPresent() {
        return present;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public String status() {
        if (active) {
            return "enabled";
        }
        if (!present) {
            return "skipped (plugin not installed or failed to load)";
        }
        if (!configEnabled) {
            return "disabled (config)";
        }
        return "failed (see startup log)";
    }

    /** Manager-driven lifecycle: detect, then enable when present + config-enabled. */
    final void tryEnable(boolean present, boolean configEnabled) {
        this.present = present;
        this.configEnabled = configEnabled;
        if (!present) {
            plugin.log().info("Integration " + name + ": skipped — " + pluginName + " not installed (or failed to load).");
            return;
        }
        if (!configEnabled) {
            plugin.log().info("Integration " + name + ": disabled in config.yml.");
            return;
        }
        try {
            onEnable();
            active = true;
            plugin.log().info("Integration " + name + ": enabled.");
        } catch (Throwable t) {
            // Throwable on purpose: a broken/renamed third-party API surfaces as NoClassDefFoundError
            // or NoSuchMethodError, and one bad integration must never take the plugin down.
            active = false;
            plugin.log().warn("Integration " + name + " failed to enable — running without it.", t);
        }
    }

    final void tryDisable() {
        if (!active) {
            return;
        }
        try {
            onDisable();
        } catch (Throwable t) {
            plugin.log().warn("Integration " + name + " failed to disable cleanly.", t);
        }
        active = false;
    }

    /** Called only when the third-party plugin is present; may reference it via a delegate class. */
    protected abstract void onEnable();

    protected void onDisable() {}
}
