package dev.originspaper.util;

import java.util.Map;

/** Cooldown helpers operating on a {@code powerId -> lastUseMillis} map (stored per player). */
public final class CooldownUtil {

    private CooldownUtil() {}

    public static boolean isReady(Map<String, Long> cooldowns, String powerId, long cooldownTicks) {
        long last = cooldowns.getOrDefault(powerId, 0L);
        return System.currentTimeMillis() - last >= cooldownTicks * 50L;
    }

    public static void trigger(Map<String, Long> cooldowns, String powerId) {
        cooldowns.put(powerId, System.currentTimeMillis());
    }

    public static long remainingSeconds(Map<String, Long> cooldowns, String powerId, long cooldownTicks) {
        long last = cooldowns.getOrDefault(powerId, 0L);
        long remainingMs = cooldownTicks * 50L - (System.currentTimeMillis() - last);
        return Math.max(0L, (remainingMs + 999L) / 1000L);
    }
}
