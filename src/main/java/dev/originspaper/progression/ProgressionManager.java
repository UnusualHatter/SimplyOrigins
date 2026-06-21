package dev.originspaper.progression;

import dev.originspaper.OriginsPaper;
import dev.originspaper.api.Origin;
import dev.originspaper.registry.PlayerOriginData;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.TextUtil;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.title.Title;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Drives per-origin progression: awards XP, handles level-ups, and shows each player's origin
 * progress on a transient boss bar (separate from the vanilla XP bar, which is left untouched).
 * Progress itself lives in {@link PlayerOriginData}; this class is the engine around it.
 */
public class ProgressionManager {

    private final OriginsPaper plugin;
    private final Map<UUID, BossBar> bars = new ConcurrentHashMap<>();

    public ProgressionManager(OriginsPaper plugin) {
        this.plugin = plugin;
    }

    /** XP needed to advance from {@code level} to {@code level + 1}. Steepens with level. */
    public static int xpToNext(int level) {
        if (level >= OriginProgress.MAX_LEVEL) {
            return Integer.MAX_VALUE;
        }
        return 100 * level * level; // L1→2:100, L2→3:400 ... L9→10:8100 (~28.5k total)
    }

    /** Grants {@code amount} XP to the player's current origin, handling any level-ups. */
    public void awardXp(Player player, int amount) {
        if (amount <= 0) {
            return;
        }
        PlayerOriginData data = plugin.data().get(player.getUniqueId());
        if (data == null || !data.hasOrigin()) {
            return;
        }
        OriginProgress prog = data.progress(data.getOrigin().id());
        if (prog.isMax()) {
            return;
        }
        prog.setXp(prog.xp() + amount);
        boolean leveled = false;
        while (!prog.isMax() && prog.xp() >= xpToNext(prog.level())) {
            prog.setXp(prog.xp() - xpToNext(prog.level()));
            prog.setLevel(prog.level() + 1);
            leveled = true;
            announceLevelUp(player, data.getOrigin(), prog.level());
        }
        if (prog.isMax()) {
            prog.setXp(0);
        }
        showBar(player, data, prog);
        if (leveled) {
            save(player, data);
        }
    }

    /** Admin override: jump to a level, resetting XP within it. */
    public void setLevel(Player player, int level) {
        PlayerOriginData data = plugin.data().get(player.getUniqueId());
        if (data == null || !data.hasOrigin()) {
            return;
        }
        OriginProgress prog = data.progress(data.getOrigin().id());
        prog.setLevel(level);
        prog.setXp(0);
        showBar(player, data, prog);
        save(player, data);
    }

    /** Flash the progress bar for a player who already has an origin (e.g. on join). */
    public void showBar(Player player) {
        PlayerOriginData data = plugin.data().get(player.getUniqueId());
        if (data != null && data.hasOrigin()) {
            showBar(player, data, data.progress(data.getOrigin().id()));
        }
    }

    /** Persists progress and clears the boss bar on quit. */
    public void handleQuit(Player player) {
        PlayerOriginData data = plugin.data().get(player.getUniqueId());
        if (data != null && data.hasOrigin()) {
            save(player, data);
        }
        clearBar(player);
    }

    /** Hides and forgets the player's progression boss bar (quit, or {@code /origin reset}). */
    public void clearBar(Player player) {
        BossBar bar = bars.remove(player.getUniqueId());
        if (bar != null) {
            player.hideBossBar(bar);
        }
    }

    private void announceLevelUp(Player player, Origin origin, int level) {
        player.showTitle(Title.title(
                TextUtil.msg("§6§lNível " + level),
                TextUtil.msg("§e" + origin.displayName() + " ficou mais forte!"),
                Title.Times.times(Duration.ofMillis(300), Duration.ofSeconds(2), Duration.ofMillis(700))));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        ParticleUtil.spawnGroundBurst(Particle.HAPPY_VILLAGER, player.getLocation().add(0, 1.0, 0), 1.2, 25, 0.3);
        ParticleUtil.spawnGroundBurst(Particle.TOTEM_OF_UNDYING, player.getLocation().add(0, 1.0, 0), 0.8, 15, 0.4);
    }

    private void showBar(Player player, PlayerOriginData data, OriginProgress prog) {
        UUID id = player.getUniqueId();
        String name = data.getOrigin().displayName();
        String title;
        float progress;
        if (prog.isMax()) {
            title = "§d" + name + " §7Nível §6" + prog.level() + " §6(MÁX)";
            progress = 1.0f;
        } else {
            int need = xpToNext(prog.level());
            title = "§d" + name + " §7Nível §e" + prog.level() + " §8• §e" + prog.xp() + "§7/§e" + need;
            progress = (float) Math.max(0.0, Math.min(1.0, (double) prog.xp() / need));
        }

        BossBar bar = bars.get(id);
        if (bar == null) {
            bar = BossBar.bossBar(TextUtil.msg(title), progress, BossBar.Color.PURPLE, BossBar.Overlay.NOTCHED_10);
            bars.put(id, bar);
        } else {
            bar.name(TextUtil.msg(title));
            bar.progress(progress);
        }
        // Kept on screen permanently so the player can always gauge how close the next level is,
        // instead of having to reopen /origin to check. Cleared on quit / reset via clearBar.
        player.showBossBar(bar);
    }

    private void save(Player player, PlayerOriginData data) {
        plugin.data().persistence().save(player.getUniqueId(), data.getOrigin().id(), data.getProgressMap());
    }
}
