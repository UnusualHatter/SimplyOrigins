package dev.originspaper.power.origins.owl;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.power.shared.NightTimeEffectPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

/** At night: Speed 1, Night Vision, and small damage bonus. */
public class NightHunterPower extends AbstractPower {

    public NightHunterPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        if (NightTimeEffectPower.isNight(player)) {
            EffectUtil.ensure(player, PotionEffectType.SPEED, 0);
            EffectUtil.ensure(player, PotionEffectType.NIGHT_VISION, 0);
            if (plugin().tick() % 3 == 0) { // nocturnal hunter's glow
                Location loc = player.getLocation().add(0, 1.0, 0);
                ParticleUtil.spawnTrail(Particle.END_ROD, loc, 2, 0.3);
                ParticleUtil.spawnTrail(Particle.ENCHANT, loc, 2, 0.3);
            }
        } else {
            EffectUtil.clear(player, PotionEffectType.SPEED);
            EffectUtil.clear(player, PotionEffectType.NIGHT_VISION);
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player player && NightTimeEffectPower.isNight(player)) {
            e.setDamage(e.getDamage() + 2.0);
        }
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.SPEED);
        EffectUtil.clear(player, PotionEffectType.NIGHT_VISION);
    }
}
