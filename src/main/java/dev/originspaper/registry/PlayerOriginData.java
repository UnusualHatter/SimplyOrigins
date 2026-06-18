package dev.originspaper.registry;

import dev.originspaper.api.Origin;
import dev.originspaper.api.PowerType;
import dev.originspaper.progression.OriginProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Per-player runtime state: current origin, active powers, cooldowns and per-origin progression. */
public class PlayerOriginData {

    private final UUID playerId;
    private Origin origin;
    private final List<PowerType> activePowers = new ArrayList<>();
    private final Map<String, Long> cooldowns = new HashMap<>();
    /** Progression per origin id, so switching away and back keeps each origin's level/XP. */
    private final Map<String, OriginProgress> progress = new HashMap<>();

    public PlayerOriginData(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public boolean hasOrigin() {
        return origin != null;
    }

    public List<PowerType> getPowers() {
        return activePowers;
    }

    public Map<String, Long> getCooldowns() {
        return cooldowns;
    }

    /** Progression for a specific origin id, created at level 1 on first access. */
    public OriginProgress progress(String originId) {
        return progress.computeIfAbsent(originId, k -> new OriginProgress());
    }

    /** Backing map of all per-origin progression (used for persistence). */
    public Map<String, OriginProgress> getProgressMap() {
        return progress;
    }

    /** Current origin's level, or 1 if no origin is selected. */
    public int level() {
        return origin == null ? 1 : progress(origin.id()).level();
    }

    /** Current origin's progression, or {@code null} if no origin is selected. */
    public OriginProgress currentProgress() {
        return origin == null ? null : progress(origin.id());
    }
}
