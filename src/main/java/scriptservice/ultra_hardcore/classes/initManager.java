package scriptservice.ultra_hardcore.classes;

import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.uhc;

public abstract class initManager {
    // consts
    protected uhc plugin;

    // constructor
    public initManager(uhc plugin) {
        this.plugin = plugin;
    }

    // methods
    public abstract void init(PluginManager pluginManager);
}
