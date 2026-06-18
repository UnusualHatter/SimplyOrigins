package dev.originspaper.power.shared;

import dev.originspaper.registry.PlayerOriginData;
import dev.originspaper.util.AttributeUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base for an origin's progression power: the single place that earns that origin's XP and applies
 * its level milestones. Milestones are read <b>dynamically</b> from the player's current level (no
 * apply/revoke bookkeeping), so they light up and dim correctly on level-up, {@code /origin setlevel}
 * and origin switches without ever getting out of sync.
 *
 * <p>Attribute milestones must be driven through {@link #attr} (idempotent — it only mutates when the
 * target value actually changes), never raw {@code AttributeUtil.set} every tick: re-adding a
 * {@code MAX_HEALTH} modifier each tick would clamp and slowly drain a player's health.
 */
public abstract class ProgressionPower extends AbstractPower {

    /** Horizontal distance banked per player toward the next distance-XP chunk. */
    private final Map<UUID, Double> distanceAcc = new ConcurrentHashMap<>();
    /** Last tick (1/s) a rate-limited XP award fired, per player. */
    private final Map<UUID, Long> xpCooldowns = new ConcurrentHashMap<>();

    protected ProgressionPower(String id) {
        super(id);
    }

    /** Current level of the player's active origin (1 if unknown). */
    protected int level(Player player) {
        PlayerOriginData data = plugin().data().get(player.getUniqueId());
        return data == null ? 1 : data.level();
    }

    /** Grants XP toward the player's current origin. No-op for amount &le; 0. */
    protected void award(Player player, int amount) {
        plugin().progression().awardXp(player, amount);
    }

    /**
     * Banks the horizontal distance travelled this move and awards {@code xp} every time the running
     * total crosses {@code blocks}. Keeps {@code awardXp} calls bounded (one per chunk) so the
     * progress bar doesn't flicker constantly while travelling.
     */
    protected void accrueDistance(Player player, PlayerMoveEvent e, double blocks, int xp) {
        if (e.getTo() == null || blocks <= 0) {
            return;
        }
        if (player.isInsideVehicle()) {
            return; // riding a boat/cart isn't self-locomotion — blocks AFK loop farms
        }
        double dx = e.getTo().getX() - e.getFrom().getX();
        double dz = e.getTo().getZ() - e.getFrom().getZ();
        double moved = Math.sqrt(dx * dx + dz * dz);
        if (moved <= 0) {
            return;
        }
        UUID id = player.getUniqueId();
        double acc = distanceAcc.getOrDefault(id, 0.0) + moved;
        if (acc >= blocks) {
            int chunks = (int) (acc / blocks);
            distanceAcc.put(id, acc - chunks * blocks);
            award(player, xp * chunks);
        } else {
            distanceAcc.put(id, acc);
        }
    }

    /**
     * Idempotent attribute modifier keyed by {@code subId}: sets it to {@code amount} only when that
     * differs from what's already applied, clears it when {@code amount == 0}. Safe to call every
     * tick — it never churns the modifier when nothing changed.
     */
    protected void attr(Player player, Attribute attribute, String subId,
                        double amount, AttributeModifier.Operation operation) {
        AttributeInstance inst = player.getAttribute(attribute);
        if (inst == null) {
            return;
        }
        NamespacedKey key = AttributeUtil.key(subId);
        AttributeModifier existing = inst.getModifier(key);
        if (amount == 0.0) {
            if (existing != null) {
                inst.removeModifier(key);
            }
            return;
        }
        if (existing != null && existing.getAmount() == amount && existing.getOperation() == operation) {
            return; // already exactly what we want — leave it alone
        }
        if (existing != null) {
            inst.removeModifier(key);
        }
        inst.addModifier(new AttributeModifier(key, amount, operation));
    }

    /** Removes a milestone attribute modifier keyed by {@code subId}. */
    protected void clearAttr(Player player, Attribute attribute, String subId) {
        AttributeUtil.clear(player, attribute, subId);
    }

    /**
     * Per-player rate limit for spammy event-driven XP (e.g. dive hits): returns true at most once
     * per {@code seconds}, recording the award when it does. Uses the 1/s global tick as its clock.
     */
    protected boolean readyForXp(Player player, long seconds) {
        long now = plugin().tick();
        UUID id = player.getUniqueId();
        Long last = xpCooldowns.get(id);
        if (last != null && now - last < seconds) {
            return false;
        }
        xpCooldowns.put(id, now);
        return true;
    }

    @Override
    public void onRemove(Player player) {
        distanceAcc.remove(player.getUniqueId());
        xpCooldowns.remove(player.getUniqueId());
    }
}
