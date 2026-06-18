package dev.originspaper.progression;

/** Mutable per-origin progression state for a single player: a level (1..MAX) and XP within it. */
public final class OriginProgress {

    public static final int MAX_LEVEL = 10;

    private int level;
    private int xp;

    public OriginProgress() {
        this(1, 0);
    }

    public OriginProgress(int level, int xp) {
        this.level = clampLevel(level);
        this.xp = Math.max(0, xp);
    }

    public int level() {
        return level;
    }

    public int xp() {
        return xp;
    }

    public boolean isMax() {
        return level >= MAX_LEVEL;
    }

    /** Sets the level (clamped to 1..MAX); does not touch XP. */
    public void setLevel(int level) {
        this.level = clampLevel(level);
    }

    public void setXp(int xp) {
        this.xp = Math.max(0, xp);
    }

    private static int clampLevel(int l) {
        return Math.max(1, Math.min(MAX_LEVEL, l));
    }
}
