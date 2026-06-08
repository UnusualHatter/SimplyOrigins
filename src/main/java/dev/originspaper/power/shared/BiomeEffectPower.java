package dev.originspaper.power.shared;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Set;

/**
 * Applies debuffs depending on the player's biome: either when inside a set of biomes
 * (e.g. goats overheat in deserts) or when outside it (e.g. bears weaken away from forests).
 */
public class BiomeEffectPower extends AbstractPower {

    private final Set<Biome> biomes;
    private final boolean applyWhenInSet;
    private final int cadenceCycles;
    private final List<PotionEffect> effects;

    public BiomeEffectPower(String id, Set<Biome> biomes, boolean applyWhenInSet,
                            int cadenceCycles, PotionEffect... effects) {
        super(id);
        this.biomes = biomes;
        this.applyWhenInSet = applyWhenInSet;
        this.cadenceCycles = Math.max(1, cadenceCycles);
        this.effects = List.of(effects);
    }

    @Override
    public void onTick(Player player) {
        if (plugin().tick() % cadenceCycles != 0) {
            return;
        }
        boolean inSet = biomes.contains(player.getWorld().getBiome(player.getLocation()));
        if (inSet == applyWhenInSet) {
            for (PotionEffect effect : effects) {
                player.addPotionEffect(effect);
            }
        }
    }

    @Override
    public void onRemove(Player player) {
        for (PotionEffect effect : effects) {
            player.removePotionEffect(effect.getType());
        }
    }
}
