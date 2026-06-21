package dev.originspaper.power.origins.wolf;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.power.shared.NightTimeEffectPower;
import dev.originspaper.util.EffectUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffectType;

/** Bonus melee damage at night, and progression XP/scaling. */
public class NightFangsPower extends AbstractPower {

    public NightFangsPower(String id) {
        super(id);
    }

    /** Current origin level, null-safe (1 if the player's data isn't loaded yet). */
    private int levelOf(Player player) {
        var data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player player && NightTimeEffectPower.isNight(player)) {
            int level = levelOf(player);
            double bonus = 1.0;
            if (level >= 2) {
                bonus += (level - 1) * 0.1;
            }
            if (level >= 3) {
                bonus += 1.0; // Marco Nv3: Presas Afiadas
            }
            e.setDamage(e.getDamage() + bonus);
        }
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;

        EntityType type = e.getEntity().getType();
        if (type == EntityType.WITHER || type == EntityType.ENDER_DRAGON) {
            plugin().progression().awardXp(killer, 200);
            return;
        }

        if (e.getEntity() instanceof Monster) {
            plugin().progression().awardXp(killer, 15);
            if (NightTimeEffectPower.isNight(killer)) {
                plugin().progression().awardXp(killer, 25);
            }
        }
    }

    @Override
    public void onTick(Player player) {
        int level = levelOf(player);
        if (level >= 10) {
            if (NightTimeEffectPower.isNight(player)) {
                EffectUtil.ensure(player, PotionEffectType.STRENGTH, 0);
            } else {
                EffectUtil.clear(player, PotionEffectType.STRENGTH);
            }
        } else {
            EffectUtil.clear(player, PotionEffectType.STRENGTH);
        }
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.STRENGTH);
    }
}
