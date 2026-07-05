package dev.originspaper.power.origins.equine;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

/** Equine XP: sustained self-powered travel and a modest reward for plant-based meals. */
public class EquineProgressionPower extends ProgressionPower {

    private static final Set<Material> FAVORED_FOODS = Set.of(
            Material.WHEAT, Material.BREAD, Material.APPLE, Material.CARROT,
            Material.GOLDEN_CARROT, Material.BAKED_POTATO, Material.BEETROOT);

    public EquineProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().isSprinting() && !e.getPlayer().isInWater()) {
            accrueDistance(e.getPlayer(), e, 40.0, 6);
        }
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (FAVORED_FOODS.contains(e.getItem().getType()) && readyForXp(e.getPlayer(), 5)) {
            award(e.getPlayer(), 4);
        }
    }

}
