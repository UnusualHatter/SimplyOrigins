package dev.originspaper.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

/** Converts legacy {@code §}-coded strings into Adventure components for items and messages. */
public final class TextUtil {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    private TextUtil() {}

    /** Parses a legacy string, disabling the default italic that item display names get. */
    public static Component item(String legacy) {
        return LEGACY.deserialize(legacy).decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> itemLore(List<String> lines) {
        return lines.stream().map(TextUtil::item).toList();
    }

    /** Parses a legacy string for chat messages (keeps default formatting). */
    public static Component msg(String legacy) {
        return LEGACY.deserialize(legacy);
    }
}
