package dev.originspaper.power.shared;

import dev.originspaper.util.ParticleUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;
import java.util.UUID;

/**
 * "Trash" foods are safe and slightly nourishing: food-caused debuffs are cancelled at their
 * potion event and the meal grants bonus saturation. Effects from combat, commands or potions are
 * deliberately untouched.
 */
public class TrashGourmetPower extends AbstractPower {

    private static final Set<Material> TRASH_FOODS = Set.of(
            Material.ROTTEN_FLESH, Material.SPIDER_EYE, Material.POISONOUS_POTATO,
            Material.SUSPICIOUS_STEW, Material.PUFFERFISH);

    /** Negatives these foods can inflict; brewed potions are untouched (we only clean post-meal). */
    private static final Set<PotionEffectType> CLEANSED = Set.of(
            PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.NAUSEA,
            PotionEffectType.WEAKNESS, PotionEffectType.BLINDNESS);

    private final float saturationBonus;

    public TrashGourmetPower(String id, float saturationBonus) {
        super(id);
        this.saturationBonus = saturationBonus;
    }

    /** Exposed for progression powers that award XP for eating "trash" foods. */
    public static boolean isTrashFood(Material m) {
        return TRASH_FOODS.contains(m);
    }

    @Override
    public void onPotionEffect(EntityPotionEffectEvent e) {
        if (e.getCause() == EntityPotionEffectEvent.Cause.FOOD
                && e.getNewEffect() != null
                && CLEANSED.contains(e.getNewEffect().getType())) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (!TRASH_FOODS.contains(e.getItem().getType())) {
            return;
        }
        UUID playerId = e.getPlayer().getUniqueId();
        plugin().getServer().getScheduler().runTask(plugin(), () -> {
            Player p = resolveActivePlayer(playerId);
            if (p == null) {
                return;
            }
            p.setSaturation(Math.min(p.getFoodLevel(), p.getSaturation() + saturationBonus));
            ParticleUtil.spawnTrail(Particle.HAPPY_VILLAGER, p.getLocation().add(0, 1.0, 0), 4, 0.3);
        });
    }
}
