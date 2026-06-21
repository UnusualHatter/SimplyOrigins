package dev.originspaper.power.origins.fox;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Set;

/**
 * Fox progression. XP: hunt animals, hunt hostiles, savour fruit.
 * Milestones: Nv3 higher jump, Nv6 more luck on drops. Nv10 ("Bote Mortal") lives in
 * {@link PouncePower}, scaling the pounce's damage.
 */
public class FoxProgressionPower extends ProgressionPower {

    private static final String JUMP_KEY = "fox:prog_jump";
    private static final String LUCK_KEY = "fox:prog_luck";

    private static final Set<Material> FRUIT = Set.of(
            Material.APPLE, Material.GOLDEN_APPLE, Material.ENCHANTED_GOLDEN_APPLE,
            Material.SWEET_BERRIES, Material.GLOW_BERRIES, Material.MELON_SLICE);

    public FoxProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) {
            return;
        }
        if (e.getEntity() instanceof Animals) {
            award(killer, 10); // "Caçar animais"
        } else if (e.getEntity() instanceof Monster) {
            award(killer, 8); // "Matar monstros"
        }
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (FRUIT.contains(e.getItem().getType())) {
            award(e.getPlayer(), 6); // "Comer frutas"
        }
    }

    @Override
    public void onTick(Player player) {
        int level = level(player);
        attr(player, Attribute.JUMP_STRENGTH, JUMP_KEY,
                level >= 3 ? 0.05 : 0.0, AttributeModifier.Operation.ADD_NUMBER);
        attr(player, Attribute.LUCK, LUCK_KEY,
                level >= 6 ? 1.0 : 0.0, AttributeModifier.Operation.ADD_NUMBER);
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.JUMP_STRENGTH, JUMP_KEY);
        clearAttr(player, Attribute.LUCK, LUCK_KEY);
        super.onRemove(player);
    }
}
