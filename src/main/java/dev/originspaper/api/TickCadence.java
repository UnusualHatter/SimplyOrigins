package dev.originspaper.api;

/**
 * How often a power's {@link PowerType#onTick} runs, measured in global tick cycles.
 * One cycle = 20 game ticks = 1 second ({@code OriginsPaper.tick()} advances once per cycle).
 *
 * <p>The global dispatcher filters on this <b>before</b> calling {@code onTick}, so a power that
 * declares {@link #EVERY_2_SECONDS} must not re-throttle itself with {@code tick() % 2} inside
 * {@code onTick} — it would double-filter. Powers that only gate <i>part</i> of their work (e.g.
 * particles every 2s but effects every second) should stay {@link #EVERY_SECOND} and keep the
 * internal check for the slow part.
 */
public enum TickCadence {

    /** Runs every global cycle (once per second). The default for powers that override onTick. */
    EVERY_SECOND(1),
    EVERY_2_SECONDS(2),
    EVERY_3_SECONDS(3),
    EVERY_5_SECONDS(5),
    EVERY_10_SECONDS(10),
    /** Never dispatched from the tick loop — the power reacts to events only. */
    EVENT_ONLY(0);

    private final int cycles;

    TickCadence(int cycles) {
        this.cycles = cycles;
    }

    /** Number of global cycles between runs; 0 means the power is never ticked. */
    public int cycles() {
        return cycles;
    }

    /** True when a power with this cadence should run on the given global tick value. */
    public boolean shouldRun(long tick) {
        return cycles > 0 && tick % cycles == 0;
    }
}
