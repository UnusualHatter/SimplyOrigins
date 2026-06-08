package dev.originspaper.registry;

import dev.originspaper.api.Origin;
import dev.originspaper.api.PowerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Per-player runtime state: current origin, active powers and active-skill cooldowns. */
public class PlayerOriginData {

    private final UUID playerId;
    private Origin origin;
    private final List<PowerType> activePowers = new ArrayList<>();
    private final Map<String, Long> cooldowns = new HashMap<>();

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
}
