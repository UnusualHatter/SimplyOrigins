package dev.originspaper;

import dev.originspaper.api.PowerType;
import dev.originspaper.api.TickCadence;
import dev.originspaper.command.OriginCommand;
import dev.originspaper.integration.IntegrationManager;
import dev.originspaper.listener.ActiveSkillListener;
import dev.originspaper.listener.ElytraGuardListener;
import dev.originspaper.listener.OriginSelectionListener;
import dev.originspaper.listener.PlacedBlockTracker;
import dev.originspaper.listener.PowerEventListener;
import dev.originspaper.progression.ProgressionManager;
import dev.originspaper.registry.OriginRegistry;
import dev.originspaper.registry.PlayerDataManager;
import dev.originspaper.registry.PlayerOriginData;
import dev.originspaper.util.OriginsLogger;
import dev.originspaper.util.PerfStats;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/** Plugin entry point: owns the registry, the player-data manager and the single global tick. */
public final class OriginsPaper extends JavaPlugin {

    private static OriginsPaper instance;

    private OriginsLogger log;
    private OriginRegistry originRegistry;
    private PlayerDataManager dataManager;
    private ProgressionManager progression;
    private IntegrationManager integrations;
    private long tick;
    private int tickSummaryEverySeconds;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        boolean debug = getConfig().getBoolean("debug", false);
        this.log = new OriginsLogger(getLogger(), debug);
        this.tickSummaryEverySeconds = getConfig().getInt("performance.log-tick-summary-seconds", 0);
        log.info("Initialising OriginsPaper...");

        this.originRegistry = new OriginRegistry();
        this.dataManager = new PlayerDataManager(this);
        this.progression = new ProgressionManager(this);

        // Third-party detection before any player data loads, so powers applied below already see
        // the final claim-protection service.
        this.integrations = new IntegrationManager(this);
        integrations.enableAll();

        getServer().getPluginManager().registerEvents(new PowerEventListener(this), this);
        getServer().getPluginManager().registerEvents(new ActiveSkillListener(this), this);
        getServer().getPluginManager().registerEvents(new OriginSelectionListener(this), this);
        getServer().getPluginManager().registerEvents(new ElytraGuardListener(this), this);
        getServer().getPluginManager().registerEvents(new PlacedBlockTracker(), this);

        OriginCommand command = new OriginCommand(this);
        if (getCommand("origin") != null) {
            getCommand("origin").setExecutor(command);
            getCommand("origin").setTabCompleter(command);
        }

        // Single global 20-tick scheduler driving every active power's onTick.
        getServer().getScheduler().runTaskTimer(this, this::globalTick, 20L, 20L);

        // Handle /reload: re-apply origins for already-online players.
        for (Player player : getServer().getOnlinePlayers()) {
            dataManager.load(player);
        }

        log.info("Enabled with " + originRegistry.all().size() + " origins."
                + (debug ? " [DEBUG mode ON]" : ""));
    }

    @Override
    public void onDisable() {
        log.info("Disabling — saving all online player data...");
        if (dataManager != null) {
            for (Player player : getServer().getOnlinePlayers()) {
                PlayerOriginData d = dataManager.get(player.getUniqueId());
                if (d != null && d.hasOrigin()) {
                    // awardXp only writes to disk on level-up, so flush progress before unloading.
                    dataManager.persistence().save(player.getUniqueId(), d.getOrigin().id(), d.getProgressMap());
                }
                dataManager.unload(player, false);
            }
        }
        if (progression != null) {
            progression.clearAllBars();
        }
        if (integrations != null) {
            integrations.disableAll();
        }
        PlacedBlockTracker.clearAll();
        log.info("Disabled.");
    }

    /**
     * Drives every active power's {@code onTick} at its declared {@link TickCadence}. Event-only
     * powers are never dispatched; slower powers only run on their cycle.
     */
    private void globalTick() {
        tick++;
        for (Player player : getServer().getOnlinePlayers()) {
            PlayerOriginData data = dataManager.get(player.getUniqueId());
            if (data == null) {
                continue;
            }
            for (PowerType power : data.getPowers()) {
                if (!power.getTickCadence().shouldRun(tick)) {
                    PerfStats.tickSkip();
                    continue;
                }
                try {
                    PerfStats.tickCall();
                    power.onTick(player);
                } catch (Exception e) {
                    PerfStats.tickError();
                    log.warn("onTick error in " + power.getId() + " for " + player.getName(), e);
                }
            }
        }
        if (tickSummaryEverySeconds > 0 && tick % tickSummaryEverySeconds == 0) {
            log.debug("tick=" + tick + " onTick calls=" + PerfStats.onTickCalls()
                    + " skips=" + PerfStats.onTickSkips() + " errors=" + PerfStats.onTickErrors()
                    + " entityScans=" + PerfStats.entityScans() + " claimChecks=" + PerfStats.claimChecks()
                    + " claimDenials=" + PerfStats.claimDenials());
        }
    }

    /**
     * Reloads {@code config.yml}, re-detects integrations and rebuilds the origin registry, then
     * re-applies every online player's saved origin from the fresh registry. Safe to call at
     * runtime via {@code /origin reload}.
     */
    public void reload() {
        reloadConfig();
        boolean debug = getConfig().getBoolean("debug", false);
        this.log = new OriginsLogger(getLogger(), debug);
        this.tickSummaryEverySeconds = getConfig().getInt("performance.log-tick-summary-seconds", 0);
        log.info("Reloading OriginsPaper...");

        integrations.reload();

        // Strip every online player's current powers cleanly, rebuild the registry (fresh power
        // instances), then re-apply each player's origin from disk against the new registry.
        for (Player player : getServer().getOnlinePlayers()) {
            PlayerOriginData d = dataManager.get(player.getUniqueId());
            if (d != null && d.hasOrigin()) {
                dataManager.persistence().save(player.getUniqueId(), d.getOrigin().id(), d.getProgressMap());
            }
            dataManager.unload(player, true);
        }
        this.originRegistry = new OriginRegistry();
        for (Player player : getServer().getOnlinePlayers()) {
            dataManager.load(player);
            progression.showBar(player); // refresh the level bar against the reloaded state
        }

        log.info("Reloaded — " + originRegistry.all().size() + " origins, config and integrations refreshed."
                + (debug ? " [DEBUG mode ON]" : ""));
    }

    /** Monotonic counter incremented once per 20-tick cycle; powers use it for slower cadences. */
    public long tick() {
        return tick;
    }

    public static OriginsPaper instance() {
        return instance;
    }

    public OriginsLogger log() {
        return log;
    }

    public OriginRegistry origins() {
        return originRegistry;
    }

    public PlayerDataManager data() {
        return dataManager;
    }

    public ProgressionManager progression() {
        return progression;
    }

    public IntegrationManager integrations() {
        return integrations;
    }
}
