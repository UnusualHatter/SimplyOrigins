package dev.originspaper.power.origins.feline;

import dev.originspaper.power.shared.AbstractPower;
import dev.originspaper.util.TextUtil;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

/** Weak arms: cannot break stone embedded in too many other solid blocks without help. */
public class WeakArmsPower extends AbstractPower {

    private static final BlockFace[] FACES = {
            BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST,
            BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

    public WeakArmsPower(String id) {
        super(id);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        if (!isStone(block.getType().name())) {
            return;
        }
        if (player.hasPotionEffect(PotionEffectType.STRENGTH)) {
            return;
        }
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType().name().endsWith("_PICKAXE")) {
            return;
        }
        int adjacent = 0;
        for (BlockFace face : FACES) {
            if (block.getRelative(face).getType().isSolid()) {
                adjacent++;
            }
        }
        if (adjacent > 2) {
            e.setCancelled(true);
            player.sendActionBar(TextUtil.msg("§cSeus braços são fracos demais para arrancar esta pedra."));
        }
    }

    private boolean isStone(String name) {
        return name.contains("STONE") || name.endsWith("_ORE") || name.equals("DEEPSLATE")
                || name.equals("NETHERRACK") || name.equals("ANDESITE")
                || name.equals("DIORITE") || name.equals("GRANITE");
    }
}
