package dev.originspaper.power.shared;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityTargetEvent;

/** Prevents a given mob type from targeting the player (e.g. creepers fear felines). */
public class ScareEntityPower extends AbstractPower {

    private final EntityType type;

    public ScareEntityPower(String id, EntityType type) {
        super(id);
        this.type = type;
    }

    @Override
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getEntityType() == type) {
            e.setCancelled(true);
        }
    }
}
