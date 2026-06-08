package dev.originspaper.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/** Holder identifying the origin-detail GUI (screen 2) and remembering which origin it shows. */
public class DetailHolder implements InventoryHolder {

    private final String originId;
    private Inventory inventory;

    public DetailHolder(String originId) {
        this.originId = originId;
    }

    public String getOriginId() {
        return originId;
    }

    void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
