package scriptservice.ultra_hardcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.apolloUtil;
import scriptservice.ultra_hardcore.utils.convertionUtil;
import scriptservice.ultra_hardcore.utils.playerUtil;
import scriptservice.ultra_hardcore.utils.stringUtil;

/**
 * event usage: Global
 * description: when a player joins, sends a message, and if in config.yml "lunarclientExclusif" is set to true, checks if the player is using lunar first, then send it.
 */
public class playerJoinEvent extends initManager implements Listener {
    public playerJoinEvent(uhc plugin) {
        super(plugin);
    }

    // init
    private stringUtil stringUtil;
    private apolloUtil apolloUtil;
    private playerUtil playerUtil;
    private convertionUtil convertionUtil;
    private boolean lunarclientExclusif;

    @Override
    public void init(PluginManager pluginManager) {
        stringUtil = plugin.stringUtil;
        apolloUtil = plugin.apolloUtil;
        playerUtil = plugin.playerUtil;
        convertionUtil = plugin.convertionUtil;
        lunarclientExclusif = (boolean) plugin.getPluginConfig().get("lunarclientExclusif");

        pluginManager.registerEvents(this, plugin); // register event
    }

    @EventHandler
    public void onEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String joinMessage = stringUtil.gets("player-join", new Object[]{player.getName()});

        if (lunarclientExclusif) {
            event.setJoinMessage(null); // bon, bah nique sa race, faut sendMessageToAll a cause de lunar qui est pourrav'

            new BukkitRunnable() {
                @Override
                public void run() {
                    // actual event start
                    if (!player.isOnline()) {return;} // et ouais, si il deco faut pas en vrai de vrai

                    if (lunarclientExclusif && !apolloUtil.isUsingLunarClient(player)) {
                        player.kickPlayer(stringUtil.gets("player-kicked-not-using-lunar"));
                        return;
                    }

                    playerUtil.sendMessageToAll(joinMessage);
                    // actual event stop
                }
            }.runTaskLater(plugin, convertionUtil.secondToTick(2)); // 40 tick, 2s
        } else {
            event.setJoinMessage(joinMessage);
        }

    }
}
