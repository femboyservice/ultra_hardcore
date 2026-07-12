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
    public apolloUtil apolloUtil;
    public gameUtil gameUtil;
    public timerUtil timerUtil;

    public scenarioManager[] scenarioManagers;

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
        final bucketLimiter bucketLimiter = new bucketLimiter(plugin);
        final chatListener chatListener = new chatListener(plugin);
        final damageLimiter damageLimiter = new damageLimiter(plugin);
        final damagePatcher damagePatcher = new damagePatcher(plugin);
        final enchantmentLimiter enchantmentLimiter = new enchantmentLimiter(plugin);
        final playerJoinQuitEvent playerJoinQuitEvent = new playerJoinQuitEvent(plugin);
        final projectileLimiter projectileLimiter = new projectileLimiter(plugin);
        final statsTracker statsTracker = new statsTracker(plugin);
        // commands
        final uhcCommand uhcCommand = new uhcCommand(plugin);
        final scenariosCommand scenariosCommand = new scenariosCommand(plugin);
        // scenarios
        final stoneVariant scenario_stoneVariant = new stoneVariant(plugin);
        final quiver scenario_quiver = new quiver(plugin);
        final cutClean scenario_cutClean = new cutClean(plugin);
        final rodless scenario_rodless = new rodless(plugin);
        //--// init
        // utils
        for (initManager util: new initManager[]{
                bucketLimiter, chatListener, damageLimiter, damagePatcher, enchantmentLimiter, playerJoinQuitEvent, projectileLimiter, statsTracker, // events
                apolloUtil, timerUtil, gameUtil, // utils
                uhcCommand, scenariosCommand, // commands
        }) {
            util.init(pluginManager);
        }

        // scenarios
        scenarioManagers = new scenarioManager[]{scenario_cutClean, scenario_stoneVariant, scenario_quiver, scenario_rodless};
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
