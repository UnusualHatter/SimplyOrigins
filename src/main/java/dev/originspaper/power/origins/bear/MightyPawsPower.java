package dev.originspaper.power.origins.bear;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.ParticleUtil;
import dev.originspaper.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.Set;

/**
 * Devastating bare-handed strikes with extra knockback — but territorial: the claw bonus only holds
 * near the bear's forest. Stay away from forest biomes for too long and the bonus lapses (no health
 * penalty); return to a forest and it comes back.
 */
public class MightyPawsPower extends AbstractPower {

    private static final double BONUS_DAMAGE = 3.0;

    private final Set<Biome> forestBiomes;
    private final long maxAwaySeconds;

    public MightyPawsPower(String id, Set<Biome> forestBiomes, long maxAwaySeconds) {
        super(id);
        this.forestBiomes = forestBiomes;
        this.maxAwaySeconds = maxAwaySeconds;
    }

    private org.bukkit.NamespacedKey getPdcKey() {
        return new org.bukkit.NamespacedKey(plugin(), "bear_forest_time");
    }

    @Override
    public void onTick(Player player) {
        if (forestBiomes.contains(player.getLocation().getBlock().getBiome())) {
            player.getPersistentDataContainer().set(getPdcKey(), org.bukkit.persistence.PersistentDataType.LONG, System.currentTimeMillis());
        }
    }

    /** True while the bear has been in (or recently left) its forest territory. */
    private boolean clawsActive(Player player) {
        Long last = player.getPersistentDataContainer().get(getPdcKey(), org.bukkit.persistence.PersistentDataType.LONG);
        if (last == null) {
            return true; // Hasn't been recorded yet, assume active
        }
        return System.currentTimeMillis() - last <= (maxAwaySeconds * 50L); // maxAwaySeconds is in ticks, 50ms per tick
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            return;
        }
        if (!clawsActive(player)) {
            player.sendActionBar(TextUtil.msg("§7Suas garras enfraquecem longe da floresta."));
            return;
        }
        e.setDamage(e.getDamage() + BONUS_DAMAGE);
        Vector dir = e.getEntity().getLocation().toVector()
                .subtract(player.getLocation().toVector());
        if (dir.lengthSquared() < 1.0e-4) {
            dir = player.getLocation().getDirection();
        }
        e.getEntity().setVelocity(dir.normalize().multiply(1.4).setY(0.5));

        // Crushing blow: crits plus a heavy crunch tinted by the ground under the target.
        Location loc = e.getEntity().getLocation();
        ParticleUtil.spawnTrail(Particle.CRIT, loc.clone().add(0, 1.0, 0), 8, 0.4);
        Object below = loc.clone().subtract(0, 0.2, 0).getBlock().getBlockData();
        ParticleUtil.spawnGroundBurst(Particle.BLOCK, loc, 0.4, 6, 0.0, below);
    }

    @Override
    public void onRemove(Player player) {
        player.getPersistentDataContainer().remove(getPdcKey());
    }
}
