package dev.originspaper.integration;

import dev.originspaper.OriginsPaper;

/**
 * Simple Voice Chat detection + config surface. No SVC API dependency yet: this hook only detects
 * the plugin ("voicechat" is its registered name) and exposes the tuning values that future powers
 * (Alpha's Howl broadcast, bat echolocation ping, skunk spray muffle) will read. Every accessor is
 * safe to call whether or not SVC is installed — powers just check {@link #isActive()} first.
 */
public class SimpleVoiceChatHook extends AbstractIntegrationHook {

    private int wolfHowlRadius = 32;
    private int batEcholocationRadius = 24;
    private int skunkSprayMuffleSeconds = 5;
    private boolean stealthReducesVoiceRange = true;

    SimpleVoiceChatHook(OriginsPaper plugin) {
        super(plugin, "Simple Voice Chat", "voicechat", "simple-voice-chat");
    }

    @Override
    protected void onEnable() {
        var cfg = plugin.getConfig();
        String base = "integrations.simple-voice-chat.";
        wolfHowlRadius = cfg.getInt(base + "wolf-howl-radius", 32);
        batEcholocationRadius = cfg.getInt(base + "bat-echolocation-radius", 24);
        skunkSprayMuffleSeconds = cfg.getInt(base + "skunk-spray-muffle-seconds", 5);
        stealthReducesVoiceRange = cfg.getBoolean(base + "stealth-reduces-voice-range", true);
        plugin.log().info("Simple Voice Chat detected — voice hooks ready (no powers use them yet).");
    }

    /** Radius (blocks) the wolf's howl should be audible through voice chat. */
    public int wolfHowlRadius() {
        return wolfHowlRadius;
    }

    /** Radius (blocks) for future bat/skunk echolocation-style voice pings. */
    public int batEcholocationRadius() {
        return batEcholocationRadius;
    }

    /** How long (seconds) a future skunk spray muffles the victim's voice. */
    public int skunkSprayMuffleSeconds() {
        return skunkSprayMuffleSeconds;
    }

    /** Whether stealth powers should shrink the player's voice range. */
    public boolean stealthReducesVoiceRange() {
        return stealthReducesVoiceRange;
    }
}
