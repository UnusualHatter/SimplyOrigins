package dev.originspaper.power.shared;

import dev.originspaper.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * Subtle ambient particles based on the player's biome — the cosmetic twin of
 * {@link BiomeEffectPower}. Runs on a slow cadence (a few seconds) with a low count so it stays
 * readable on busy servers. Fires either when inside a biome set (e.g. a demon shivering in the
 * cold) or when outside it (e.g. a bear pining away from forests).
 */
public class BiomeParticlePower extends AbstractPower {

    private final Set<Biome> biomes;
    private final boolean applyWhenInSet;
    private final int cadenceCycles;
    private final int count;
    private final List<Particle> particles;

    public BiomeParticlePower(String id, Set<Biome> biomes, boolean applyWhenInSet,
                              int cadenceCycles, int count, Particle... particles) {
        super(id);
        this.biomes = biomes;
        this.applyWhenInSet = applyWhenInSet;
        this.cadenceCycles = Math.max(1, cadenceCycles);
        this.count = count;
        this.particles = List.of(particles);
    }

    @Override
    public void onTick(Player player) {
        if (plugin().tick() % cadenceCycles != 0) {
            return;
        }
        boolean inSet = biomes.contains(player.getWorld().getBiome(player.getLocation()));
        if (inSet != applyWhenInSet) {
            return;
        }
        Location loc = player.getLocation().add(0, 1.0, 0);
        for (Particle particle : particles) {
            ParticleUtil.spawnTrail(particle, loc, count, 0.5);
        }
    }
}
