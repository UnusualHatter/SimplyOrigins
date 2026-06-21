package dev.originspaper.power.origins.moth;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.EffectUtil;
import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffectType;

/** Regeneration while bathed in artificial light; sluggish in pitch-black darkness. */
public class DrawnToLightPower extends AbstractPower {

    private static final int LIGHT_THRESHOLD = 8;
    /** Light-basking healing isn't free: it burns through food, so it can't idle-heal forever. */
    private static final int MIN_FOOD_TO_HEAL = 7;
    private static final float HEAL_EXHAUSTION = 0.5f;

    public DrawnToLightPower(String id) {
        super(id);
    }

    @Override
    public void onTick(Player player) {
        Block block = player.getLocation().getBlock();
        // getLightFromBlocks = light emitted by blocks (torches/lanterns/glowstone/froglights/
        // shroomlights/sea lanterns...), excluding the sun — exactly "artificial light".
        if (block.getLightFromBlocks() >= LIGHT_THRESHOLD) {
            // Balanced healing: only regenerate when actually hurt and well-fed, and let the
            // regeneration draw on hunger — so basking next to a torch isn't infinite free healing.
            if (player.getFoodLevel() >= MIN_FOOD_TO_HEAL && player.getHealth() < maxHealth(player)) {
                EffectUtil.ensure(player, PotionEffectType.REGENERATION, 0);
                player.setExhaustion(player.getExhaustion() + HEAL_EXHAUSTION);
            } else {
                EffectUtil.clear(player, PotionEffectType.REGENERATION);
            }
            // Slow orbit of light motes around the moth.
            double offset = plugin().tick() * 0.6;
            Location c = player.getLocation().add(0, 1.0, 0);
            for (int i = 0; i < 4; i++) {
                double angle = offset + i * Math.PI / 2;
                Location p = c.clone().add(Math.cos(angle) * 0.8, 0, Math.sin(angle) * 0.8);
                ParticleUtil.spawnTrail(Particle.WAX_ON, p, 1, 0.0);
                ParticleUtil.spawnTrail(Particle.END_ROD, p, 1, 0.0);
            }
            return;
        }

        EffectUtil.clear(player, PotionEffectType.REGENERATION);
        // Pitch black (no block light and no sky light at all) saps the light-loving moth.
        // On the surface the stored sky light is 15 even at night, so this only bites in true dark.
        if (block.getLightLevel() == 0) {
            EffectUtil.apply(player, PotionEffectType.SLOWNESS, 40, 0);
            ParticleUtil.spawnTrail(Particle.SMOKE, player.getLocation().add(0, 1.0, 0), 2, 0.3);
        }
    }

    /** Glow berries are the moth's light-food: they grant extra saturation on top of the hunger. */
    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != Material.GLOW_BERRIES) {
            return;
        }
        Player player = e.getPlayer();
        // Apply next tick so vanilla's own food/saturation update doesn't overwrite ours.
        plugin().getServer().getScheduler().runTask(plugin(), () -> {
            if (player.isOnline()) {
                player.setSaturation(Math.min(player.getFoodLevel(), player.getSaturation() + 4.0f));
            }
        });
    }

    private double maxHealth(Player player) {
        AttributeInstance attr = player.getAttribute(Attribute.MAX_HEALTH);
        return attr == null ? 20.0 : attr.getValue();
    }

    @Override
    public void onRemove(Player player) {
        EffectUtil.clear(player, PotionEffectType.REGENERATION);
        EffectUtil.clear(player, PotionEffectType.SLOWNESS);
    }
}
