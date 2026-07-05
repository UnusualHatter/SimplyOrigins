package dev.originspaper.integration;

import dev.originspaper.OriginsPaper;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * AxGraves (Artillex-Studios) integration: strips the origins' protected wings out of graves the
 * moment they spawn — see {@link AxGravesGraveListener} for the why. This class holds no AxGraves
 * imports; the listener that does is only instantiated once the plugin is confirmed present.
 */
public class AxGravesHook extends AbstractIntegrationHook {

    private Listener listener;

    AxGravesHook(OriginsPaper plugin) {
        super(plugin, "AxGraves", "AxGraves", "axgraves");
    }

    @Override
    protected void onEnable() {
        listener = new AxGravesGraveListener();
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        plugin.log().info("AxGraves detected — origin wings will stay out of death graves.");
    }

    @Override
    protected void onDisable() {
        if (listener != null) {
            HandlerList.unregisterAll(listener);
            listener = null;
        }
    }
}
