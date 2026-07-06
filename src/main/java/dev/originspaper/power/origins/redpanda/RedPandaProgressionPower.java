package dev.originspaper.power.origins.redpanda;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

/**
 * Red panda progression. XP: roam forest blocks, climb (granted by {@link ClimberPower}), eat
 * berries and forage bites ({@link ForagerPower}). Milestones: Nv3 −1 fall damage here,
 * Nv6 faster Fluffy Tail ({@link FluffyTailPower}), Nv10 climb one block higher
 * ({@link ClimberPower}).
 */
public class RedPandaProgressionPower extends ProgressionPower {

    /** Attribute key of the retired Nv3 "+1 heart" milestone — cleared on apply/remove (migration). */
    private static final String LEGACY_HP_KEY = "red_panda:prog_hp";

    private static final Set<Material> FOREST_FLOOR = Set.of(
            Material.MOSS_BLOCK, Material.MOSS_CARPET, Material.PODZOL, Material.ROOTED_DIRT,
            Material.DIRT_PATH, Material.VINE, Material.BAMBOO, Material.BAMBOO_SAPLING,
            Material.BAMBOO_BLOCK, Material.STRIPPED_BAMBOO_BLOCK, Material.AZALEA,
            Material.FLOWERING_AZALEA);

    public RedPandaProgressionPower(String id) {
        super(id);
    }

    private static boolean isForestBlock(Material type) {
        return type.name().endsWith("_LOG") || type.name().endsWith("_WOOD")
                || type.name().endsWith("_LEAVES") || FOREST_FLOOR.contains(type);
    }

    @Override
    public void onApply(Player player) {
        clearAttr(player, Attribute.MAX_HEALTH, LEGACY_HP_KEY);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        Material below = e.getPlayer().getLocation().clone().subtract(0, 0.1, 0).getBlock().getType();
        if (isForestBlock(below)) {
            accrueDistance(e.getPlayer(), e, 30.0, 5); // "Percorrer florestas e copas"
        }
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        Material m = e.getItem().getType();
        if ((m == Material.SWEET_BERRIES || m == Material.GLOW_BERRIES)
                && readyForXp(e.getPlayer(), 5)) {
            award(e.getPlayer(), 6); // "Comer frutinhas e ovos"
        }
    }

    /** Nv3 "Vitalidade Fofa": every fall hurts one point less. */
    @Override
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL
                || !(e.getEntity() instanceof Player player)) {
            return;
        }
        if (level(player) >= 3) {
            double reduced = Math.max(0.0, e.getDamage() - 1.0);
            if (reduced <= 0.0) {
                e.setCancelled(true);
            } else {
                e.setDamage(reduced);
            }
        }
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.MAX_HEALTH, LEGACY_HP_KEY);
        super.onRemove(player);
    }
}
