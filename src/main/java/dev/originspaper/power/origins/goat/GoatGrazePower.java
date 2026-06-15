package dev.originspaper.power.origins.goat;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * A goat eats anything. Sneak + right-click the air with a non-food block in the main hand to graze
 * it: one item is consumed for a small bite of hunger ("meio ponto de fome" → +1 food point).
 *
 * <p>Scoped to blocks and to right-click-<i>air</i> on purpose: blocks have no air interaction, so
 * this never competes with placing, tool use, or thrown items — there is no item-use to hijack.
 */
public class GoatGrazePower extends AbstractPower {

    private static final int FOOD_PER_BITE = 1;

    public GoatGrazePower(String id) {
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
        if (item.getType().isAir() || item.getType().isEdible() || !item.getType().isBlock()) {
            return;
        }
        if (item.hasItemMeta() && item.getItemMeta() instanceof org.bukkit.inventory.meta.BlockStateMeta) {
            return;
        }

        e.setCancelled(true);
        ItemStack crumb = item.clone();
        item.setAmount(item.getAmount() - 1);
        player.getInventory().setItemInMainHand(item.getAmount() <= 0 ? null : item);

        player.setFoodLevel(Math.min(20, player.getFoodLevel() + FOOD_PER_BITE));
        player.setSaturation(Math.min(player.getFoodLevel(), player.getSaturation() + 0.5f));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 0.7f, 1.2f);
        ParticleUtil.spawn(Particle.ITEM, player.getEyeLocation(), 8, 0.15, 0.1, 0.15, 0.05, crumb);
    }
}
