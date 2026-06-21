package dev.originspaper.listener;

import com.artillexstudios.axgraves.api.events.GraveSpawnEvent;
import com.artillexstudios.axgraves.grave.Grave;
import dev.originspaper.power.shared.ElytraFlightPower;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Integration with the AxGraves plugin (Artillex-Studios).
 *
 * <p>The origins' permanent wings are a protected, self-re-equipping Elytra that the player gets
 * back automatically on respawn ({@link ElytraGuardListener#onRespawn}). They must therefore never
 * be stored in a death grave — doing so both leaks a duplicate (the player has fresh wings on
 * respawn <em>and</em> a stored copy in the grave) and lets the item be looted out of the grave.
 *
 * <p>Our {@link ElytraGuardListener#onDeath} removes the wings from the vanilla death drops, but
 * AxGraves captures the inventory through its own path, so that alone isn't enough. The moment a
 * grave spawns we strip any protected wings straight out of its storage — plugin-agnostic about how
 * they got there.
 *
 * <p>This class references AxGraves types directly, so it is only instantiated and registered when
 * the AxGraves plugin is actually present (guarded in {@link dev.originspaper.OriginsPaper}).
 */
public class AxGravesIntegrationListener implements Listener {

    @EventHandler
    public void onGraveSpawn(GraveSpawnEvent e) {
        Grave grave = e.getGrave();
        Inventory storage = grave.getGui();
        if (storage == null) {
            return;
        }
        boolean removed = false;
        ItemStack[] contents = storage.getContents();
        for (int i = 0; i < contents.length; i++) {
            if (ElytraFlightPower.isProtectedWings(contents[i])) {
                storage.setItem(i, null);
                removed = true;
            }
        }
        if (removed) {
            grave.updateHologram(); // refresh the displayed item count after pulling the wings
        }
    }
}
