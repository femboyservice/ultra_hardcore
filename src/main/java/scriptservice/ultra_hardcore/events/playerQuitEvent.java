package scriptservice.ultra_hardcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.stringUtil;

/**
 * event usage: Global
 * description: sends a custom message when a player leaves
 */
public class playerQuitEvent extends initManager implements Listener {
    public playerQuitEvent(uhc plugin) {
        super(plugin);
    }

    // init
    private stringUtil stringUtil;

    @Override
    public void init(PluginManager pluginManager) {
        stringUtil = plugin.stringUtil;

        pluginManager.registerEvents(this, plugin); // register event
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        event.setQuitMessage(stringUtil.gets("player-leave", new Object[]{player.getName()}));
    }
}
