package dev.originspaper.power.origins.moth;

import dev.originspaper.power.shared.ProgressionPower;
import dev.originspaper.util.GroundUtil;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 * Moth progression. XP: flutter over flowers, nurture nature (pollinating, awarded in
 * {@link PollinatorPower}), and feed. Milestone Nv6 ("Corpo Resiliente") gives back a heart here;
 * Nv3 ("Pólen Fértil", bigger pollination) and Nv10 ("Aura de Vida", saturation aura) live in
 * {@link PollinatorPower}.
 */
public class MothProgressionPower extends ProgressionPower {

    private static final String HP_KEY = "moth:prog_hp";
    private static final int FLOWER_SCAN_RADIUS = 2;

    public MothProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        award(e.getPlayer(), 6); // "Comer frutas e plantas"
    }

    @Override
    public void onTick(Player player) {
        attr(player, Attribute.MAX_HEALTH, HP_KEY,
                level(player) >= 6 ? 2.0 : 0.0, AttributeModifier.Operation.ADD_NUMBER);

        // "Voar perto de flores": made literal so the objective matches its name — the moth only
        // earns this XP while airborne (fluttering/gliding) over or beside flowers, never by just
        // walking around or standing AFK in a flower field.
        if (!GroundUtil.isOnGround(player) && nearFlower(player) && readyForXp(player, 2)) {
            award(player, 5);
        }
    }

    /** True if any flower sits within a small box around the moth. */
    private boolean nearFlower(Player player) {
        Location base = player.getLocation();
        for (int dx = -FLOWER_SCAN_RADIUS; dx <= FLOWER_SCAN_RADIUS; dx++) {
            for (int dy = -FLOWER_SCAN_RADIUS; dy <= 1; dy++) {
                for (int dz = -FLOWER_SCAN_RADIUS; dz <= FLOWER_SCAN_RADIUS; dz++) {
                    if (Tag.FLOWERS.isTagged(base.clone().add(dx, dy, dz).getBlock().getType())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.MAX_HEALTH, HP_KEY);
        super.onRemove(player);
    }
}
