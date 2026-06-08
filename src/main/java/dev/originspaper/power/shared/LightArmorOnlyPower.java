package dev.originspaper.power.shared;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import dev.originspaper.util.ArmorUtil;

/** Disallows equipping armor pieces whose defense value exceeds a threshold. */
public class LightArmorOnlyPower extends ArmorRevertSupport {

    private final int maxDefense;

    public LightArmorOnlyPower(String id, int maxDefense) {
        super(id);
        this.maxDefense = maxDefense;
    }

    @Override
    public void onArmorChange(PlayerArmorChangeEvent e) {
        if (ArmorUtil.defense(e.getNewItem()) > maxDefense) {
            revert(e.getPlayer(), e.getSlot(), e.getNewItem(), "§cEsta armadura é pesada demais para você.");
        }
    }
}
