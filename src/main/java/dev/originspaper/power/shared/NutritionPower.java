package dev.originspaper.power.shared;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.function.Predicate;

/** Scales the hunger restored by matching foods (e.g. carrots double, meat for goats less). */
public class NutritionPower extends AbstractPower {

    private final Predicate<Material> matcher;
    private final double multiplier;

    public NutritionPower(String id, Predicate<Material> matcher, double multiplier) {
        super(id);
        this.matcher = matcher;
        this.multiplier = multiplier;
    }

    @Override
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (e.getItem() == null || !matcher.test(e.getItem().getType())) {
            return;
        }
        if (!(e.getEntity() instanceof HumanEntity human)) {
            return;
        }
        int current = human.getFoodLevel();
        int proposed = e.getFoodLevel();
        int delta = proposed - current;
        if (delta <= 0) {
            return; // only scale gains
        }
        int scaled = (int) Math.round(delta * multiplier);
        e.setFoodLevel(Math.max(0, Math.min(20, current + scaled)));
    }
}
