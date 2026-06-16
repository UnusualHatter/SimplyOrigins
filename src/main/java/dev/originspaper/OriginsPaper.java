package dev.originspaper;

import dev.originspaper.api.PowerType;
import dev.originspaper.command.OriginCommand;
import dev.originspaper.listener.ActiveSkillListener;
import dev.originspaper.listener.ElytraGuardListener;
import dev.originspaper.listener.OriginSelectionListener;
import dev.originspaper.listener.PowerEventListener;
import dev.originspaper.registry.OriginRegistry;
import dev.originspaper.registry.PlayerDataManager;
import dev.originspaper.registry.PlayerOriginData;
import dev.originspaper.util.OriginsLogger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/** Plugin entry point: owns the registry, the player-data manager and the single global tick. */
public final class OriginsPaper extends JavaPlugin {

    private static OriginsPaper instance;

    private OriginsLogger log;
    private OriginRegistry originRegistry;
    private PlayerDataManager dataManager;
    private long tick;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        boolean debug = getConfig().getBoolean("debug", false);
        this.log = new OriginsLogger(getLogger(), debug);
        log.info("Initialising OriginsPaper...");

        this.originRegistry = new OriginRegistry();
        this.dataManager = new PlayerDataManager(this);

        getServer().getPluginManager().registerEvents(new PowerEventListener(this), this);
        getServer().getPluginManager().registerEvents(new ActiveSkillListener(this), this);
        getServer().getPluginManager().registerEvents(new OriginSelectionListener(this), this);
        getServer().getPluginManager().registerEvents(new ElytraGuardListener(this), this);

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
                dataManager.unload(player, false);
            }
        }
        log.info("Disabled.");
    }

    private void globalTick() {
        tick++;
        for (Player player : getServer().getOnlinePlayers()) {
            PlayerOriginData data = dataManager.get(player.getUniqueId());
            if (data == null) {
                continue;
            }
            for (PowerType power : data.getPowers()) {
                try {
                    power.onTick(player);
                } catch (Exception e) {
                    log.warn("onTick error in " + power.getId() + " for " + player.getName(), e);
                }
            }
        }
    }

    /**
     * Reloads {@code config.yml} and rebuilds the origin registry, then re-applies every online
     * player's saved origin from the fresh registry. Safe to call at runtime via {@code /origin reload}.
     */
    public void reload() {
        reloadConfig();
        boolean debug = getConfig().getBoolean("debug", false);
        this.log = new OriginsLogger(getLogger(), debug);
        log.info("Reloading OriginsPaper...");

        // Strip every online player's current powers cleanly, rebuild the registry (fresh power
        // instances), then re-apply each player's origin from disk against the new registry.
        for (Player player : getServer().getOnlinePlayers()) {
            dataManager.unload(player, true);
        }
        this.originRegistry = new OriginRegistry();
        for (Player player : getServer().getOnlinePlayers()) {
            dataManager.load(player);
        }

        log.info("Reloaded — " + originRegistry.all().size() + " origins, config refreshed."
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
}
