package dev.originspaper.power.shared;

import dev.originspaper.OriginsPaper;
import dev.originspaper.api.PowerType;
import dev.originspaper.api.TickCadence;
import dev.originspaper.integration.ClaimProtectionService;
import dev.originspaper.integration.IntegrationManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Convenience base storing the power id and exposing the plugin singleton. */
public abstract class AbstractPower implements PowerType {

    /** Per-class cache of "does this power actually override onTick?" (see {@link #getTickCadence}). */
    private static final Map<Class<?>, Boolean> OVERRIDES_ON_TICK = new ConcurrentHashMap<>();

    protected final String id;
    private final TickCadence defaultCadence;

    protected AbstractPower(String id) {
        this.id = id;
        this.defaultCadence = overridesOnTick(getClass()) ? TickCadence.EVERY_SECOND : TickCadence.EVENT_ONLY;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Event-only powers (no {@code onTick} override anywhere in their hierarchy) are detected
     * automatically and skipped by the global dispatcher. Powers that tick slower than once per
     * second override this and return the cadence they want.
     */
    @Override
    public TickCadence getTickCadence() {
        return defaultCadence;
    }

    private static boolean overridesOnTick(Class<?> type) {
        return OVERRIDES_ON_TICK.computeIfAbsent(type, t -> {
            try {
                return t.getMethod("onTick", org.bukkit.entity.Player.class).getDeclaringClass() != PowerType.class;
            } catch (NoSuchMethodException e) {
                return true; // can't tell — err on the side of ticking
            }
        });
    }

    protected OriginsPaper plugin() {
        return OriginsPaper.instance();
    }

    /** Claim-protection checks (GriefPrevention when present, otherwise allow-all). */
    protected ClaimProtectionService claims() {
        IntegrationManager integrations = plugin().integrations();
        return integrations == null ? ClaimProtectionService.ALLOW_ALL : integrations.claims();
    }

    @Override
    public void onApply(org.bukkit.entity.Player player) {}

    @Override
    public void onRemove(org.bukkit.entity.Player player) {}

    /**
     * True when the player's main hand holds any tier of spear — the vanilla 26.x charge weapon.
     * Used to cap coelho/cabra dash hits (RabbitDashPower / LeapPower / RamPower).
     */
    protected static boolean isHoldingSpear(org.bukkit.entity.Player player) {
        return player.getInventory().getItemInMainHand().getType().name().endsWith("_SPEAR");
    }
}
