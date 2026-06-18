package dev.originspaper.power.origins.bear;

import dev.originspaper.power.shared.ProgressionPower;
import dev.originspaper.util.FoodUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 * Bear progression. XP: slay hostiles, hunt animals, feast on raw meat.
 * Milestones: Nv3 brutal bare-handed strikes, Nv6 thicker hide (armor toughness), Nv10 +2 hearts.
 */
public class BearProgressionPower extends ProgressionPower {

    private static final String TOUGH_KEY = "bear:prog_tough";
    private static final String HP_KEY = "bear:prog_hp";

    public BearProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) {
            return;
        }
        if (e.getEntity() instanceof Monster) {
            award(killer, 12); // "Derrotar inimigos"
        } else if (e.getEntity() instanceof Animals) {
            award(killer, 6); // "Caçar animais"
        }
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (FoodUtil.isRawMeat(e.getItem().getType())) {
            award(e.getPlayer(), 8); // "Comer carne crua"
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        // Nv3 "Patas Brutais": extra damage when striking bare-handed.
        if (e.getDamager() instanceof Player player
                && level(player) >= 3
                && player.getInventory().getItemInMainHand().getType().isAir()) {
            e.setDamage(e.getDamage() + 2.0);
        }
    }

    @Override
    public void onTick(Player player) {
        int level = level(player);
        attr(player, Attribute.ARMOR_TOUGHNESS, TOUGH_KEY,
                level >= 6 ? 4.0 : 0.0, AttributeModifier.Operation.ADD_NUMBER);
        attr(player, Attribute.MAX_HEALTH, HP_KEY,
                level >= 10 ? 4.0 : 0.0, AttributeModifier.Operation.ADD_NUMBER);
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.ARMOR_TOUGHNESS, TOUGH_KEY);
        clearAttr(player, Attribute.MAX_HEALTH, HP_KEY);
        super.onRemove(player);
    }
}
