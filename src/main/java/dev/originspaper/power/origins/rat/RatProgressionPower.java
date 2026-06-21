package dev.originspaper.power.origins.rat;

import dev.originspaper.listener.PlacedBlockTracker;
import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Rat progression XP source: hunting and scavenging. The level milestones live where the behaviour
 * does — evasion scaling in {@link EvasionPower} (Nv3/Nv10) and the Scurry cooldown cut in
 * {@link ScurryPower} (Nv6) — so this power only earns XP.
 */
public class RatProgressionPower extends ProgressionPower {

    public RatProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer != null && e.getEntity() instanceof Monster) {
            award(killer, 12); // "Matar monstros"
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        if (!PlacedBlockTracker.isPlayerPlaced(e.getBlock())) {
            award(e.getPlayer(), 3); // "Minerar e coletar recursos"
        }
    }
}
