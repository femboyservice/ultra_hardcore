package scriptservice.ultra_hardcore;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import scriptservice.ultra_hardcore.classes.*;
import scriptservice.ultra_hardcore.commands.*;
import scriptservice.ultra_hardcore.events.*;
import scriptservice.ultra_hardcore.scenarios.*;
import scriptservice.ultra_hardcore.utils.*;

public final class uhc extends JavaPlugin {
    //--// definition
    // juste pour faire chier qqn
    private final uhc plugin = this;

    // plugin config
    @Getter private final FileConfiguration pluginConfig = getConfig();
    @Getter private final gameConfig gameConfig = new gameConfig();

    // utils
    @Getter private apolloUtil apolloUtil;
    @Getter private gameUtil gameUtil;
    @Getter private timerUtil timerUtil;

    public scenarioManager[] scenarioManagers;

    // event w/ utils
    @Getter private movementLimiter movementLimiter; // jsp si je suis fan, mais j'ai pas d'autre idée en tête..

    @Override
    public void onEnable() {
        // Plugin startup logic
        final PluginManager pluginManager = getServer().getPluginManager();

        //--// create
        // utils
        apolloUtil = new apolloUtil(plugin);
        gameUtil = new gameUtil(plugin);
        timerUtil = new timerUtil(plugin);
        // events
        movementLimiter = new movementLimiter(plugin);
        //--// init
        // utils
        for (initManager util: new initManager[]{
                new bucketLimiter(plugin), new chatListener(plugin), new damageLimiter(plugin), new damagePatcher(plugin), new enchantmentLimiter(plugin), movementLimiter,
                new playerJoinQuitEvent(plugin), new projectileLimiter(plugin), new statsTracker(plugin), // events
                apolloUtil, timerUtil, gameUtil, // utils
                new uhcCommand(plugin), new scenariosCommand(plugin), // commands
        }) {
            util.init(pluginManager);
        }

        // scenarios
        scenarioManagers = new scenarioManager[]{new cutClean(plugin), new stoneVariant(plugin), new quiver(plugin), new rodless(plugin)};
        for (scenarioManager scenario: scenarioManagers) {
            scenario.init(pluginManager);
        }

        //--// yaml config
        pluginConfig.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {}
}
