package dev.originspaper.api;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import org.bukkit.entity.Player;
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

/**
 * Base contract for every Origin power. All event hooks are optional defaults so a concrete
 * power only overrides what it needs.
 *
 * <p>Dispatch conventions (see {@code PowerEventListener}):
 * <ul>
 *   <li>{@link #onDamage(EntityDamageEvent)} fires for the <b>victim</b> player. Because
 *       {@code EntityDamageByEntityEvent} shares this handler list, a power may cast the event
 *       to inspect the damager for victim-side, attacker-aware logic.</li>
 *   <li>{@link #onDamageByEntity(EntityDamageByEntityEvent)} fires for the <b>attacker</b>
 *       player (direct or via a projectile they shot).</li>
 *   <li>{@link #onTick(Player)} is called once every 20 ticks from the single global scheduler.</li>
 * </ul>
 */
public interface PowerType {

    String getId();

    void onApply(Player player);

    void onRemove(Player player);

    default void onDamage(EntityDamageEvent e) {}

    default void onDamageByEntity(EntityDamageByEntityEvent e) {}

    default void onMove(PlayerMoveEvent e) {}

    /**
     * Called from the single global scheduler for each online player that has this power, at the
     * rate declared by {@link #getTickCadence()}. Never called for {@link TickCadence#EVENT_ONLY}.
     */
    default void onTick(Player player) {}

    /**
     * How often {@link #onTick} should be dispatched. {@code AbstractPower} narrows the default to
     * {@link TickCadence#EVENT_ONLY} automatically for classes that don't override {@code onTick}.
     */
    default TickCadence getTickCadence() {
        return TickCadence.EVERY_SECOND;
    }

    default void onFoodChange(FoodLevelChangeEvent e) {}

    default void onItemConsume(PlayerItemConsumeEvent e) {}

    default void onBlockBreak(BlockBreakEvent e) {}

    default void onBlockDropItem(BlockDropItemEvent e) {}

    default void onArmorChange(PlayerArmorChangeEvent e) {}

    default void onInteract(PlayerInteractEvent e) {}

    default void onEntityTarget(EntityTargetEvent e) {}

    default void onPotionEffect(EntityPotionEffectEvent e) {}

    default void onBedEnter(PlayerBedEnterEvent e) {}

    default void onProjectileLaunch(PlayerLaunchProjectileEvent e) {}

    default void onProjectileHit(ProjectileHitEvent e) {}

    default void onEntityDeath(org.bukkit.event.entity.EntityDeathEvent e) {}

    /** Fired when the player completes a merchant purchase (villager / wandering trader). */
    default void onVillagerTrade(io.papermc.paper.event.player.PlayerPurchaseEvent e) {}
}
