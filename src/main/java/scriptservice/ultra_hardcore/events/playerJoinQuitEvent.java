package scriptservice.ultra_hardcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import scriptservice.ultra_hardcore.classes.activePlayer;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.classes.scoreboardSign;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.*;

import java.util.Optional;

/**
 * event usage: Global
 * description: when a player joins, sends a message, and if in config.yml "lunarclientExclusif" is set to true, checks if the player is using lunar first, then send it.
 */
public class playerJoinQuitEvent extends initManager implements Listener {
    public playerJoinQuitEvent(uhc plugin) {
        super(plugin);
    }

    // init
    private gameUtil gameUtil;
    private apolloUtil apolloUtil;
    private boolean lunarclientExclusif;

    @Override
    public void init(PluginManager pluginManager) {
        gameUtil = plugin.gameUtil;
        apolloUtil = plugin.apolloUtil;
        lunarclientExclusif = (boolean) plugin.getPluginConfig().get("lunarclientExclusif");

        pluginManager.registerEvents(this, plugin); // register event
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String joinMessage = languageUtil.gets("player-join", new Object[]{player.getName()});

        if (lunarclientExclusif) {
            event.setJoinMessage(null); // bon, bah nique sa race, faut sendMessageToAll a cause de lunar qui est pourrav'

            new BukkitRunnable() {
                @Override
                public void run() {
                    // actual event start
                    if (!player.isOnline()) {return;} // et ouais, s'il deco faut pas en vrai de vrai

                    if (lunarclientExclusif && !apolloUtil.isUsingLunarClient(player)) {
                        player.kickPlayer(languageUtil.gets("player-kicked-not-using-lunar"));
                        return;
                    }

                    playerUtil.sendMessageToAll(joinMessage);
                    // actual event stop
                }
            }.runTaskLater(plugin, convertionUtil.secondToTick(2));
        } else {
            event.setJoinMessage(joinMessage);
        }

        // add scoreboard
        final Optional<activePlayer> optionalActivePlayer = gameUtil.isPlayerActive(player);
        optionalActivePlayer.ifPresent(activePlayer -> {
            final scoreboardSign scoreboard = gameUtil.createScoreboard(activePlayer.getUUID());
            activePlayer.setScoreboard(scoreboard);

            gameUtil.updateGlobalScoreboard(activePlayer.getUUID(), scoreboard);
        }); // hein? wtf? trop bien // ok j'ai compris en vrai (je crois)

    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        event.setQuitMessage(languageUtil.gets("player-leave", new Object[]{player.getName()}));

        // remove scoreboard from global scoreboard list
        gameUtil.removeScoreboard(player);

        // remove scoreboard from activePlayer
        final Optional<activePlayer> optionalActivePlayer = gameUtil.isPlayerActive(player);
        optionalActivePlayer.ifPresent(activePlayer -> activePlayer.setScoreboard(null));
    }
}
