package dev.originspaper.power.shared;

import java.util.Set;

/** Can only eat meat. */
public class CarnivoreDietPower extends DietPower {

    public CarnivoreDietPower(String id) {
        super(id, true, Set.of(), "§cVocê só consegue comer carne.");
    }
}
