package dev.originspaper.progression;

/** Machine-readable kind of an XP objective, so a single listener can award XP generically. */
public enum ObjectiveType {
    /** Killing any hostile mob (a {@code Monster}). */
    KILL_HOSTILE,
    /** Killing a hostile mob while it is night. */
    KILL_NIGHT,
    /** Killing a boss (Ender Dragon, Wither, Warden). */
    KILL_BOSS,
    /** Killing a passive animal. */
    KILL_ANIMAL,
    /** Breaking a fully-grown crop. */
    HARVEST,
    /** Breaking an ore block. */
    MINE
}
