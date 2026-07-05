package dev.originspaper.integration;

/**
 * A soft integration with a third-party plugin. Hook classes must never reference the third-party
 * plugin's types directly — anything that imports them lives in a separate class that is only
 * instantiated from {@link #onEnable()}, which the manager calls strictly after confirming the
 * plugin is present (otherwise merely loading the class would throw {@code NoClassDefFoundError}).
 */
public interface IntegrationHook {

    /** Display name used in logs and {@code /origin debug integrations}. */
    String name();

    /** Exact plugin name as registered with the {@code PluginManager}. */
    String pluginName();

    /** Key under {@code integrations.<key>} in config.yml. */
    String configKey();

    /** Whether the third-party plugin is installed (set by the manager during detection). */
    boolean isPresent();

    /** Whether the hook is currently enabled and running. */
    boolean isActive();

    /** Human-readable state for status displays. */
    String status();
}
