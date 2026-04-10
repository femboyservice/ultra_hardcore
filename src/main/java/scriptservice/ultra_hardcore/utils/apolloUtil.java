package scriptservice.ultra_hardcore.utils;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.ApolloModuleManager;
import com.lunarclient.apollo.module.modsetting.ModSettingModule;
import com.lunarclient.apollo.player.ApolloPlayerManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;

public class apolloUtil extends initManager {
    public apolloUtil(uhc plugin) {
        super(plugin);
    }

    // globals
    @Getter private final ApolloPlayerManager apolloPlayerManager = Apollo.getPlayerManager();
    @Getter private final ApolloModuleManager apolloModuleManager = Apollo.getModuleManager();
    @Getter private final ModSettingModule modSettingModule = getApolloModuleManager().getModule(ModSettingModule.class);

    // init
    @Override
    public void init(PluginManager pluginManager) {}

    // per-class methods
    public final boolean isUsingLunarClient(Player player) {
        return getApolloPlayerManager().hasSupport(player.getUniqueId());
    }
}
