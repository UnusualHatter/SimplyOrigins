package dev.originspaper.power.shared;

import dev.originspaper.api.ActivePowerType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

/** Active skill: applies a burst of self-buffs on activation. */
public class ActiveBuffPower extends AbstractPower implements ActivePowerType {

    private final long cooldownTicks;
    private final List<PotionEffect> effects;
    private final Sound sound;

    public ActiveBuffPower(String id, long cooldownTicks, Sound sound, PotionEffect... effects) {
        super(id);
        this.cooldownTicks = cooldownTicks;
        this.sound = sound;
        this.effects = List.of(effects);
    }

    @Override
    public void onActivate(Player player) {
        for (PotionEffect effect : effects) {
            player.addPotionEffect(effect);
        }
        if (sound != null) {
            player.getWorld().playSound(player.getLocation(), sound, 1.0f, 1.0f);
        }
    }

    @Override
    public long getCooldownTicks() {
        return cooldownTicks;
    }

    /** True while any of this skill's buff effects is still on the player (used for aura visuals). */
    protected boolean isBuffActive(Player player) {
        for (PotionEffect effect : effects) {
            if (player.hasPotionEffect(effect.getType())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRemove(Player player) {
        for (PotionEffect effect : effects) {
            player.removePotionEffect(effect.getType());
        }
    }
}
