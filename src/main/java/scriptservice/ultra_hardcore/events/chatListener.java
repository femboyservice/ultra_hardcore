package scriptservice.ultra_hardcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.classes.states;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.languageUtil;

/**
 * event usage: Global
 * description: disable chat if chatState is set to CHAT_DISABLED
 */
public class chatListener extends initManager implements Listener {
    public chatListener(uhc plugin) {
        super(plugin);
    }

    @Override
    public void init(PluginManager pluginManager) {
        pluginManager.registerEvents(this, plugin); // register event
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (plugin.getGameConfig().getChatState() == states.CHAT_DISABLED) {
            event.setCancelled(true);
            player.sendMessage(languageUtil.gets("uhc-chat-disabled"));
        }
    }
}
