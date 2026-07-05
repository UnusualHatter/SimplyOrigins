package dev.originspaper.util;

import dev.originspaper.OriginsPaper;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

/**
 * Deterministic attribute-modifier helpers. Every modifier is keyed by a {@link NamespacedKey}
 * derived from the power id, so add/remove is idempotent and survives restarts cleanly.
 */
public final class AttributeUtil {

    private AttributeUtil() {}

    public static NamespacedKey key(String powerId) {
        String k = powerId.toLowerCase().replace(':', '.').replaceAll("[^a-z0-9._-]", "_");
        return new NamespacedKey(OriginsPaper.instance(), k);
    }

    public static void set(Player player, Attribute attribute, String powerId,
                           double amount, AttributeModifier.Operation operation) {
        AttributeInstance inst = player.getAttribute(attribute);
        if (inst == null) {
            return;
        }
        NamespacedKey key = key(powerId);
        if (inst.getModifier(key) != null) {
            inst.removeModifier(key);
        }
        inst.addModifier(new AttributeModifier(key, amount, operation));
    }

    public static void clear(Player player, Attribute attribute, String powerId) {
        AttributeInstance inst = player.getAttribute(attribute);
        if (inst == null) {
            return;
        }
        NamespacedKey key = key(powerId);
        if (inst.getModifier(key) != null) {
            inst.removeModifier(key);
        }
    }

    /**
     * Idempotent variant safe to call every tick: sets the modifier to {@code amount} only when it
     * differs from what's applied, and clears it when {@code amount == 0}. Never churns the
     * modifier when nothing changed (re-adding a MAX_HEALTH modifier each tick would clamp and
     * slowly drain health — see {@code ProgressionPower#attr}).
     */
    public static void sync(Player player, Attribute attribute, String powerId,
                            double amount, AttributeModifier.Operation operation) {
        AttributeInstance inst = player.getAttribute(attribute);
        if (inst == null) {
            return;
        }
        NamespacedKey key = key(powerId);
        AttributeModifier existing = inst.getModifier(key);
        if (amount == 0.0) {
            if (existing != null) {
                inst.removeModifier(key);
            }
            return;
        }
        if (existing != null && existing.getAmount() == amount && existing.getOperation() == operation) {
            return;
        }
        if (existing != null) {
            inst.removeModifier(key);
        }
        inst.addModifier(new AttributeModifier(key, amount, operation));
    }
}
