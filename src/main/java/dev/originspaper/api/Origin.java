package dev.originspaper.api;

import java.util.List;

/**
 * Immutable definition of an origin.
 *
 * @param id          unique lowercase id, e.g. {@code "merling"}
 * @param displayName human-readable name, e.g. {@code "Merling"}
 * @param skullTexture texture URL for the GUI head, or {@code null} to fall back to a material icon
 * @param fallbackIcon material used as the GUI icon when {@code skullTexture} is null
 * @param powers      functional powers applied to the player
 * @param infos       display metadata (name + lore) shown in the GUI, decoupled from the powers
 */
public record Origin(
        String id,
        String displayName,
        String skullTexture,
        org.bukkit.Material fallbackIcon,
        List<PowerType> powers,
        List<PowerInfo> infos) {

    public record PowerInfo(String name, List<String> lore) {
        public PowerInfo(String name, String... lore) {
            this(name, List.of(lore));
        }
    }
}
