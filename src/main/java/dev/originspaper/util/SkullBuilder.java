package dev.originspaper.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/** Helper to build display items: textured player heads, plain named items and filler panes. */
public final class SkullBuilder {

    private SkullBuilder() {}

    /** Builds a PLAYER_HEAD with a custom skin texture URL. */
    public static ItemStack createSkull(String textureUrl, String displayName, List<String> lore) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        skull.editMeta(SkullMeta.class, meta -> {
            try {
                PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
                PlayerTextures textures = profile.getTextures();
                URL url = URI.create(textureUrl).toURL();
                textures.setSkin(url);
                profile.setTextures(textures);
                meta.setPlayerProfile(profile);
            } catch (Exception ignored) {
                // If the texture cannot be parsed the head simply stays blank; not fatal.
            }
            meta.displayName(TextUtil.item(displayName));
            meta.lore(TextUtil.itemLore(lore));
        });
        return skull;
    }

    /** Builds any material as a named/lored display item. */
    public static ItemStack create(Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.displayName(TextUtil.item(displayName));
            meta.lore(TextUtil.itemLore(lore));
        });
        return item;
    }

    /** A nameless black glass pane used to fill empty GUI slots. */
    public static ItemStack filler() {
        ItemStack pane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        pane.editMeta(meta -> meta.displayName(TextUtil.item(" ")));
        return pane;
    }
}
