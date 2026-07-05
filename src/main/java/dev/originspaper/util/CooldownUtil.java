package dev.originspaper.util;

import java.util.Map;

/** Cooldown helpers operating on a {@code powerId -> lastUseMillis} map (stored per player). */
public final class CooldownUtil {

    private CooldownUtil() {}

    public static boolean isReady(Map<String, Long> cooldowns, String powerId, long cooldownTicks) {
        long last = cooldowns.getOrDefault(powerId, 0L);
        long now = System.currentTimeMillis();
        long elapsed = last <= 0L ? Long.MAX_VALUE : last > now ? 0L : now - last;
        return elapsed >= toMillis(cooldownTicks);
    }

    public static void trigger(Map<String, Long> cooldowns, String powerId) {
        cooldowns.put(powerId, System.currentTimeMillis());
    }

    public static long remainingSeconds(Map<String, Long> cooldowns, String powerId, long cooldownTicks) {
        long last = cooldowns.getOrDefault(powerId, 0L);
        long now = System.currentTimeMillis();
        long elapsed = last <= 0L ? Long.MAX_VALUE : last > now ? 0L : now - last;
        long cooldownMs = toMillis(cooldownTicks);
        if (elapsed >= cooldownMs) {
            return 0L;
        }
        long remainingMs = cooldownMs - elapsed;
        return remainingMs / 1000L + (remainingMs % 1000L == 0L ? 0L : 1L);
    }

    private static long toMillis(long cooldownTicks) {
        if (cooldownTicks <= 0L) {
            return 0L;
        }
        return cooldownTicks > Long.MAX_VALUE / 50L
                ? Long.MAX_VALUE
                : cooldownTicks * 50L;
    }
}
