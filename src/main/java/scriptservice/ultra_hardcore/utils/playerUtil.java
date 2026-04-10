package scriptservice.ultra_hardcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;

public class playerUtil extends initManager {
    public playerUtil(uhc plugin) {
        super(plugin);
    }

    // init
    @Override
    public void init(PluginManager pluginManager) {}

    // per-class methods
    public final void sendMessageToAll(String text) {
        for (Player player: Bukkit.getOnlinePlayers()) {
            player.sendMessage(text);
        }
    }

    public final void sendMessageToAll(String[] texts) {
        for (Player player: Bukkit.getOnlinePlayers()) {
            player.sendMessage(texts);
        }
    }
}
