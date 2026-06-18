package dev.originspaper.power.origins.moth;

import dev.originspaper.power.shared.ProgressionPower;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Moth progression. XP: flutter around, nurture nature (pollinating, awarded in
 * {@link PollinatorPower}), and feed. Milestone Nv6 ("Corpo Resiliente") gives back a heart here;
 * Nv3 ("Pólen Fértil", bigger pollination) and Nv10 ("Aura de Vida", saturation aura) live in
 * {@link PollinatorPower}.
 */
public class MothProgressionPower extends ProgressionPower {

    private static final String HP_KEY = "moth:prog_hp";

    public MothProgressionPower(String id) {
        super(id);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        accrueDistance(e.getPlayer(), e, 30.0, 5); // "Voar entre as flores"
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        award(e.getPlayer(), 6); // "Conviver com a natureza"
    }

    @Override
    public void onTick(Player player) {
        attr(player, Attribute.MAX_HEALTH, HP_KEY,
                level(player) >= 6 ? 2.0 : 0.0, AttributeModifier.Operation.ADD_NUMBER);
    }

    @Override
    public void onRemove(Player player) {
        clearAttr(player, Attribute.MAX_HEALTH, HP_KEY);
        super.onRemove(player);
    }
}
