package dev.originspaper.integration;

import dev.originspaper.OriginsPaper;

import java.util.List;

/**
 * Detects optional third-party plugins on enable/reload and drives the lifecycle of every
 * {@link IntegrationHook}. Protection integrations fail closed (a lookup error denies the action);
 * cosmetic/display integrations fail open (a broken hook is simply skipped).
 */
public class IntegrationManager {

    private final OriginsPaper plugin;
    private final AxGravesHook axGraves;
    private final GriefPreventionHook griefPrevention;
    private final SimpleVoiceChatHook simpleVoiceChat;
    private final List<AbstractIntegrationHook> hooks;

    public IntegrationManager(OriginsPaper plugin) {
        this.plugin = plugin;
        this.axGraves = new AxGravesHook(plugin);
        this.griefPrevention = new GriefPreventionHook(plugin);
        this.simpleVoiceChat = new SimpleVoiceChatHook(plugin);
        this.hooks = List.of(
                axGraves,
                griefPrevention,
                simpleVoiceChat,
                new PlaceholderAPIHook(plugin),
                new WorldGuardHook(plugin),
                new LuckPermsHook(plugin));
    }

    /** Detects installed plugins and enables every hook that is present and config-enabled. */
    public void enableAll() {
        for (AbstractIntegrationHook hook : hooks) {
            // isPluginEnabled (not getPlugin != null): a plugin that failed its own enable — e.g.
            // an incompatible server version — is still registered but must be treated as absent,
            // or touching its classes fails against a closed classloader.
            boolean present = plugin.getServer().getPluginManager().isPluginEnabled(hook.pluginName());
            boolean enabled = plugin.getConfig().getBoolean("integrations." + hook.configKey() + ".enabled", true);
            hook.tryEnable(present, enabled);
        }
    }

    public void disableAll() {
        for (AbstractIntegrationHook hook : hooks) {
            hook.tryDisable();
        }
    }

    /** Re-detects plugins and re-reads the integration config (used by {@code /origin reload}). */
    public void reload() {
        disableAll();
        enableAll();
    }

    /** Claim checks for area/hostile abilities — {@code ALLOW_ALL} unless GriefPrevention is active. */
    public ClaimProtectionService claims() {
        return griefPrevention.service();
    }

    /** Voice-chat hook (safe no-op methods when Simple Voice Chat is absent). */
    public SimpleVoiceChatHook voiceChat() {
        return simpleVoiceChat;
    }

    /** All hooks, for status displays. */
    public List<IntegrationHook> hooks() {
        return List.copyOf(hooks);
    }
}
