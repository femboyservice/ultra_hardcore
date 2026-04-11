package scriptservice.ultra_hardcore;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import scriptservice.ultra_hardcore.classes.*;
import scriptservice.ultra_hardcore.commands.*;
import scriptservice.ultra_hardcore.events.*;
import scriptservice.ultra_hardcore.utils.*;

public final class uhc extends JavaPlugin {
    //--// definition
    // juste pour faire chier qqn
    private final uhc plugin = this;

    // plugin config
    @Getter private final FileConfiguration pluginConfig = getConfig();
    @Getter private final gameConfig gameConfig = new gameConfig();

    // utils
    public stringUtil stringUtil;
    public apolloUtil apolloUtil;
    public playerUtil playerUtil;
    public convertionUtil convertionUtil;

    @Override
    public void onEnable() {
        // Plugin startup logic
        final PluginManager pluginManager = getServer().getPluginManager();

        //--// create
        // events
        final entityDamageByEntityEvent entityDamageByEntityEvent = new entityDamageByEntityEvent(plugin);
        final playerJoinEvent playerJoinEvent = new playerJoinEvent(plugin);
        final playerQuitEvent playerQuitEvent = new playerQuitEvent(plugin);
        // utils
        stringUtil = new stringUtil(plugin);
        apolloUtil = new apolloUtil(plugin);
        playerUtil = new playerUtil(plugin);
        convertionUtil = new convertionUtil(plugin);
        // commands
        final uhcCommand uhcCommand = new uhcCommand(plugin);
        // init
        for (initManager util: new initManager[]{
                playerJoinEvent, playerQuitEvent, entityDamageByEntityEvent, // events
                stringUtil, apolloUtil, playerUtil, convertionUtil, // utils
                uhcCommand, // commands
        }) {
            util.init(pluginManager);
        }

        // yaml config
        pluginConfig.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {}
}
