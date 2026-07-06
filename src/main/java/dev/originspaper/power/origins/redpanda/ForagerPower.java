package dev.originspaper.power.origins.redpanda;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The red panda's snack habit: sneak + right-click the air with bamboo or an egg in the main hand
 * to nibble it — one item for a modest bite of hunger. Mirrors the goat's graze input so it never
 * competes with placing blocks or eating real food, and cancelling the interact stops the egg from
 * being thrown.
 */
public class ForagerPower extends AbstractPower {

    private static final int FOOD_PER_BITE = 2;
    private static final float SATURATION_PER_BITE = 1.0f;
    private static final long XP_INTERVAL_SECONDS = 5;

    /** Last tick (1/s) a bite granted XP, per player — keeps "Comer frutinhas e ovos" farm-proof. */
    private final Map<UUID, Long> lastXp = new ConcurrentHashMap<>();

    public ForagerPower(String id) {
        super(id);
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR || e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Player player = e.getPlayer();
        if (!player.isSneaking() || player.getFoodLevel() >= 20) {
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        Material type = item.getType();
        if (type != Material.BAMBOO && type != Material.EGG) {
            return;
        }

        e.setCancelled(true); // also stops the egg from being thrown
        ItemStack crumb = item.clone();
        item.setAmount(item.getAmount() - 1);
        player.getInventory().setItemInMainHand(item.getAmount() <= 0 ? null : item);

        player.setFoodLevel(Math.min(20, player.getFoodLevel() + FOOD_PER_BITE));
        player.setSaturation(Math.min(player.getFoodLevel(),
                player.getSaturation() + SATURATION_PER_BITE));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 0.7f, 1.3f);
        ParticleUtil.spawn(Particle.ITEM, player.getEyeLocation(), 8, 0.15, 0.1, 0.15, 0.05, crumb);

        long now = plugin().tick();
        Long last = lastXp.get(player.getUniqueId());
        if (last == null || now - last >= XP_INTERVAL_SECONDS) {
            lastXp.put(player.getUniqueId(), now);
            plugin().progression().awardXp(player, 4); // "Comer frutinhas e ovos"
        }
    }

    @Override
    public void onRemove(Player player) {
        lastXp.remove(player.getUniqueId());
    }
}
