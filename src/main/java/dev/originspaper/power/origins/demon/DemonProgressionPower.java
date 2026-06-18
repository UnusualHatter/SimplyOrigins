package dev.originspaper.power.origins.demon;

import dev.originspaper.power.shared.NightTimeEffectPower;
import dev.originspaper.power.shared.ProgressionPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Demon progression. XP: slay hostiles, hunt in the Nether, hunt at night.
 * Milestones: Nv3 hotter strikes (+0.5 heart melee), Nv6 regenerate near heat, Nv10 Strength on kill.
 */
public class DemonProgressionPower extends ProgressionPower {

    private static final String BRASA_KEY = "demon:prog_brasa";

    public DemonProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null || !(e.getEntity() instanceof Monster)) {
            return;
        }
        award(killer, 10); // "Caçar criaturas hostis"
        if (killer.getWorld().getEnvironment() == World.Environment.NETHER) {
            award(killer, 20); // "Caçar no Nether"
        }
        if (NightTimeEffectPower.isNight(killer)) {
            award(killer, 15); // "Caçar à noite"
        }
        if (level(killer) >= 10) { // Nv10 "Fúria Infernal"
            EffectUtil.apply(killer, PotionEffectType.STRENGTH, 100, 0);
        }
    }

    @Override
    public void onTick(Player player) {
        int level = level(player);
        attr(player, Attribute.ATTACK_DAMAGE, BRASA_KEY,
                level >= 3 ? 1.0 : 0.0, AttributeModifier.Operation.ADD_NUMBER);

        if (level >= 6 && nearHeat(player)) { // Nv6 "Pele de Cinzas"
            EffectUtil.apply(player, PotionEffectType.REGENERATION, 60, 0);
        }
    }

    /** True if a heat source sits within a small box around the demon's feet. */
    private boolean nearHeat(Player player) {
        Block feet = player.getLocation().getBlock();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (isHeat(feet.getRelative(dx, dy, dz).getType())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isHeat(Material type) {
        return type == Material.FIRE || type == Material.SOUL_FIRE || type == Material.LAVA
                || type == Material.MAGMA_BLOCK || type == Material.CAMPFIRE
                || type == Material.SOUL_CAMPFIRE || type == Material.LANTERN
                || type == Material.GLOWSTONE || type == Material.SHROOMLIGHT;
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.ATTACK_DAMAGE, BRASA_KEY);
        super.onRemove(player);
    }
}
