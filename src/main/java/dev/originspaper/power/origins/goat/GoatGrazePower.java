package dev.originspaper.power.origins.goat;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.Set;

/**
 * A goat eats anything. Sneak + right-click the air with any non-food item in the main hand to graze
 * it: one item is consumed for a small bite of hunger ("meio ponto de fome" → +1 food point). Real
 * food is still eaten the normal way.
 *
 * <p>Scoped to right-click-<i>air</i> on purpose, so it never competes with placing a block, using a
 * tool, or eating actual food. Items that do something when used in the air (pearls, potions,
 * buckets, horns, heads...) are refused so a crouching goat can still use them, as are tools,
 * weapons, armour and containers that still hold items. Looking steeply down is ignored: that is the
 * sneak-bridging pose, where a missed block placement would otherwise be eaten.
 */
public class GoatGrazePower extends AbstractPower {

    private static final int FOOD_PER_BITE = 1;
    private static final float MAX_GRAZE_PITCH = 50.0f;

    /** Used in the air (thrown, drunk, played, opened, aimed) or doubles as a plugin tool. */
    private static final Set<Material> USABLE = EnumSet.of(
            Material.ENDER_PEARL, Material.ENDER_EYE, Material.SNOWBALL, Material.SPLASH_POTION,
            Material.LINGERING_POTION, Material.EXPERIENCE_BOTTLE, Material.FIRE_CHARGE,
            Material.WIND_CHARGE, Material.FIREWORK_ROCKET, Material.GOAT_HORN, Material.SPYGLASS,
            Material.GLASS_BOTTLE, Material.MAP, Material.WRITABLE_BOOK, Material.WRITTEN_BOOK,
            Material.KNOWLEDGE_BOOK, Material.COMPASS, Material.RECOVERY_COMPASS, Material.CLOCK,
            Material.STICK, Material.TOTEM_OF_UNDYING);

    public GoatGrazePower(String id) {
        super(id);
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR || e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Player player = e.getPlayer();
        if (!player.isSneaking() || player.getFoodLevel() >= 20
                || player.getLocation().getPitch() > MAX_GRAZE_PITCH) {
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir() || item.getType().isEdible() || isUsable(item)) {
            return;
        }
        // Tools, weapons and armour are exactly the items with durability.
        if (item.getType().getMaxDurability() > 0) {
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

    private static boolean isUsable(ItemStack item) {
        if (item.hasData(DataComponentTypes.CONSUMABLE) || item.hasData(DataComponentTypes.EQUIPPABLE)
                || item.hasData(DataComponentTypes.BLOCKS_ATTACKS)) {
            return true;
        }
        Material type = item.getType();
        String name = type.name();
        return USABLE.contains(type) || Tag.ITEMS_EGGS.isTagged(type)
                || name.endsWith("_SPAWN_EGG") || name.endsWith("BUCKET") || name.endsWith("BUNDLE");
    }
}
