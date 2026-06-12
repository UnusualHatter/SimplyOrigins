package dev.originspaper.listener;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import dev.originspaper.OriginsPaper;
import dev.originspaper.api.PowerType;
import dev.originspaper.registry.PlayerOriginData;
import dev.originspaper.util.SitStackUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.function.Consumer;

/**
 * Single listener that fans every relevant Bukkit event out to the active powers of the
 * relevant player. See {@link PowerType} for the victim/attacker dispatch convention.
 */
public class PowerEventListener implements Listener {

    private final OriginsPaper plugin;

    public PowerEventListener(OriginsPaper plugin) {
        this.plugin = plugin;
    }

    private void dispatch(Player player, Consumer<PowerType> action) {
        if (player == null) {
            return;
        }
        PlayerOriginData data = plugin.data().get(player.getUniqueId());
        if (data == null) {
            return;
        }
        for (PowerType power : data.getPowers()) {
            try {
                action.accept(power);
            } catch (Exception e) {
                plugin.log().warn("Power " + power.getId() + " failed for " + player.getName(), e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent e) {
        // Victim-side: fire for the player being hurt.
        if (e.getEntity() instanceof Player victim) {
            dispatch(victim, p -> p.onDamage(e));
        }

        // Attacker-side: only run if the event is still live — a victim's immunity power may have
        // already cancelled it, and we must not inflate damage on a cancelled event.
        if (e instanceof EntityDamageByEntityEvent ede && !ede.isCancelled()) {
            Player attacker = resolveAttacker(ede.getDamager());
            if (attacker != null) {
                plugin.log().debug(attacker.getName() + " attacking "
                        + ede.getEntity().getType() + " base=" + String.format("%.2f", ede.getDamage()));
                dispatch(attacker, p -> p.onDamageByEntity(ede));
                plugin.log().debug("  -> final damage=" + String.format("%.2f", ede.getDamage()));
            }
        }
    }

    private Player resolveAttacker(Entity damager) {
        if (damager instanceof Player p) {
            return p;
        }
        if (damager instanceof Projectile proj && proj.getShooter() instanceof Player p) {
            return p;
        }
        return null;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!e.hasChangedPosition()) {
            return;
        }
        dispatch(e.getPlayer(), p -> p.onMove(e));
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player player) {
            dispatch(player, p -> p.onFoodChange(e));
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        dispatch(e.getPlayer(), p -> p.onItemConsume(e));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        dispatch(e.getPlayer(), p -> p.onBlockBreak(e));
    }

    @EventHandler
    public void onBlockDrop(BlockDropItemEvent e) {
        dispatch(e.getPlayer(), p -> p.onBlockDropItem(e));
    }

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent e) {
        dispatch(e.getPlayer(), p -> p.onArmorChange(e));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        dispatch(e.getPlayer(), p -> p.onInteract(e));
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player target) {
            dispatch(target, p -> p.onEntityTarget(e));
            // A player riding (or being ridden by) another via plugins like GSit's
            // player-sit counts as the same "target" for protective powers, e.g. a
            // feline's creeper-scare aura should also cover whoever it's stacked with.
            for (Player linked : SitStackUtil.linkedPlayers(target)) {
                if (e.isCancelled()) {
                    break;
                }
                dispatch(linked, p -> p.onEntityTarget(e));
            }
        }
    }

    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent e) {
        if (e.getEntity() instanceof Player player) {
            dispatch(player, p -> p.onPotionEffect(e));
        }
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent e) {
        dispatch(e.getPlayer(), p -> p.onBedEnter(e));
    }

    @EventHandler
    public void onProjectileLaunch(PlayerLaunchProjectileEvent e) {
        dispatch(e.getPlayer(), p -> p.onProjectileLaunch(e));
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player shooter) {
            dispatch(shooter, p -> p.onProjectileHit(e));
        }
    }
}
