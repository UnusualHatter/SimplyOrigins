package dev.originspaper.progression;

import java.util.List;

/**
 * Design-time progression definition for one origin: the thematic challenges that grant XP and the
 * milestone unlocks that light up as the player levels. This is the content the progression GUI
 * displays; wiring the actual XP earning and unlock effects is done per origin.
 */
public record OriginProgression(List<Objective> objectives, List<Unlock> unlocks) {

    /** A thematic challenge that awards {@code xp} when performed. */
    public record Objective(String description, int xp) {}

    /** A milestone that becomes active once the player's origin reaches {@code level}. */
    public record Unlock(int level, String name, String description) {}
}
