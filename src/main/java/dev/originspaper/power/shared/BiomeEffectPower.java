package dev.originspaper.power.shared;

import dev.originspaper.api.TickCadence;
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
    private final TickCadence cadence;
    private final List<PotionEffect> effects;

    public BiomeEffectPower(String id, Set<Biome> biomes, boolean applyWhenInSet,
                            TickCadence cadence, PotionEffect... effects) {
        super(id);
        this.biomes = biomes;
        this.applyWhenInSet = applyWhenInSet;
        this.cadence = cadence;
        this.effects = List.of(effects);
    }

    @Override
    public TickCadence getTickCadence() {
        return cadence;
    }

    @Override
    public void onTick(Player player) {
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
