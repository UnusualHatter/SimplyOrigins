package dev.originspaper.power.origins.raccoon;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.TextUtil;
import io.papermc.paper.event.player.PlayerPurchaseEvent;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * "Ladrãozinho": now and then a villager purchase comes out free — the trade completes normally
 * (the villager's stock and prices move as usual) and the ingredients sneak back into the
 * raccoon's pockets one tick later. A per-player cooldown keeps it a treat, not an economy hole,
 * and the refund resolves the player by UUID and re-checks the power is still applied.
 */
public class ThiefPower extends AbstractPower {

    private static final long STEAL_INTERVAL_SECONDS = 30;

    private final double chance;

    /** Last tick (1/s) a trade came out free, per player. */
    private final Map<UUID, Long> lastSteal = new ConcurrentHashMap<>();

    public ThiefPower(String id, double chance) {
        super(id);
        this.chance = chance;
    }

    @Override
    public void onVillagerTrade(PlayerPurchaseEvent e) {
        Player player = e.getPlayer();
        long now = plugin().tick();
        Long last = lastSteal.get(player.getUniqueId());
        if (last != null && now - last < STEAL_INTERVAL_SECONDS) {
            return;
        }
        if (ThreadLocalRandom.current().nextDouble() >= chance) {
            return;
        }

        // Refund what was actually paid: the demand-adjusted first ingredient plus the second.
        MerchantRecipe trade = e.getTrade();
        List<ItemStack> refund = new ArrayList<>(2);
        ItemStack first = trade.getAdjustedIngredient1();
        if ((first == null || first.getType().isAir()) && !trade.getIngredients().isEmpty()) {
            first = trade.getIngredients().get(0);
        }
        if (first != null && !first.getType().isAir()) {
            refund.add(first.clone());
        }
        List<ItemStack> ingredients = trade.getIngredients();
        if (ingredients.size() > 1 && ingredients.get(1) != null
                && !ingredients.get(1).getType().isAir()) {
            refund.add(ingredients.get(1).clone());
        }
        if (refund.isEmpty()) {
            return;
        }
        lastSteal.put(player.getUniqueId(), now);

        // The purchase consumes the ingredients after this event; give them back next tick.
        UUID playerId = player.getUniqueId();
        plugin().getServer().getScheduler().runTask(plugin(), () -> {
            Player p = resolveActivePlayer(playerId);
            if (p == null) {
                return;
            }
            for (ItemStack item : refund) {
                for (ItemStack leftover : p.getInventory().addItem(item).values()) {
                    p.getWorld().dropItemNaturally(p.getLocation(), leftover);
                }
            }
            p.sendActionBar(TextUtil.msg("§6Ladrãozinho: §fa troca saiu de graça!"));
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.7f, 1.2f);
            ParticleUtil.spawnTrail(Particle.SMOKE, p.getLocation().add(0, 1.0, 0), 6, 0.3);
        });
    }

    @Override
    public void onRemove(Player player) {
        lastSteal.remove(player.getUniqueId());
    }
}
