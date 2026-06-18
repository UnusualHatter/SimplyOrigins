package dev.originspaper.power.origins.human;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Human progression: a versatile generalist that grows tougher with experience.
 * Milestones: Nv5 +1 heart, Nv10 a second +1 heart (cumulative +2).
 */
public class HumanProgressionPower extends ProgressionPower {

    private static final String HP_KEY = "human:prog_growth";

    public HumanProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) {
            return;
        }
        if (e.getEntity() instanceof Monster) {
            award(killer, 10);
        } else if (e.getEntity() instanceof Animals) {
            award(killer, 6);
        }
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        accrueDistance(e.getPlayer(), e, 40.0, 5); // "Explorar o mundo"
    }

    @Override
    public void onTick(Player player) {
        int level = level(player);
        double bonus = level >= 10 ? 4.0 : (level >= 5 ? 2.0 : 0.0);
        attr(player, Attribute.MAX_HEALTH, HP_KEY, bonus, AttributeModifier.Operation.ADD_NUMBER);
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.MAX_HEALTH, HP_KEY);
        super.onRemove(player);
    }
}
