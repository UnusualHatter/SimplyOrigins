package dev.originspaper.util;

/**
 * Lightweight performance counters surfaced by {@code /origin debug perf}. Written only from the
 * main server thread (the global tick, event handlers and claim checks all run there), so plain
 * longs are safe — no atomics needed.
 */
public final class PerfStats {

    private static long onTickCalls;
    private static long onTickSkips;
    private static long onTickErrors;
    private static long entityScans;
    private static long claimChecks;
    private static long claimDenials;

    private PerfStats() {}

    /** An onTick actually dispatched to a power. */
    public static void tickCall() {
        onTickCalls++;
    }

    /** An onTick skipped by cadence filtering (event-only or off-cycle). */
    public static void tickSkip() {
        onTickSkips++;
    }

    public static void tickError() {
        onTickErrors++;
    }

    /** A radius entity scan (getNearbyEntities and friends) issued by a power. */
    public static void entityScan() {
        entityScans++;
    }

    public static void claimCheck() {
        claimChecks++;
    }

    public static void claimDenial() {
        claimDenials++;
    }

    public static long onTickCalls() {
        return onTickCalls;
    }

    public static long onTickSkips() {
        return onTickSkips;
    }

    public static long onTickErrors() {
        return onTickErrors;
    }

    public static long entityScans() {
        return entityScans;
    }

    public static long claimChecks() {
        return claimChecks;
    }

    public static long claimDenials() {
        return claimDenials;
    }

    public static void reset() {
        onTickCalls = 0;
        onTickSkips = 0;
        onTickErrors = 0;
        entityScans = 0;
        claimChecks = 0;
        claimDenials = 0;
    }
}
