package scriptservice.ultra_hardcore.utils;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.*;
import scriptservice.ultra_hardcore.uhc;

import java.util.*;

public class gameUtil extends initManager {
    public gameUtil(uhc plugin) {
        super(plugin);
    }

    // init
    private timerUtil timerUtil;
    @Override
    public void init(PluginManager pluginManager) {
        timerUtil = plugin.timerUtil;
    }

    // per-class vars
    public HashMap<Player, activePlayer> Player_Active = new HashMap<>();
    public HashMap<activePlayer, Player> Active_Player = new HashMap<>();

    @Getter @Setter private String worldName = "world";

    // per-class methods
    // map related
    public double getMapSize() {
        final World world = Bukkit.getWorld(getWorldName());

        if (world != null) {
            return (world.getWorldBorder().getSize() / 2);
        } else {
            return 0.0;
        }
    }

    // active players
    public activePlayer setupActivePlayer(Player player) {
        activePlayer activePlayer = new activePlayer(plugin, player.getUniqueId());

        Player_Active.put(player, activePlayer);
        Active_Player.put(activePlayer, player);

        return activePlayer;
    }

    public Collection<activePlayer> getActivePlayers() {
        return Player_Active.values();
    }

    public activePlayer[] getActivePlayers(boolean alive) {
        ArrayList<activePlayer> arrayList = new ArrayList<>();

        for (activePlayer activePlayer: getActivePlayers()) {
            if (activePlayer.isAlive() == alive) {
                arrayList.add(activePlayer);
            }
        }
        return arrayList.toArray(new activePlayer[0]);
    }

    public Optional<activePlayer> isPlayerActive(Player player) {
        for (activePlayer activePlayer: getActivePlayers()) {
            if (activePlayer.getUUID().equals(player.getUniqueId())) {
                return Optional.of(activePlayer);
            }
        }

        return Optional.empty();
    }

    // starts
    public void startSTART() {
        // set state
        plugin.getGameConfig().setGameState(states.START);

        // start timer to tp
        timerUtil.startCountdown(3);
    }

    public void startTELEPORT() {
        // set state
        plugin.getGameConfig().setGameState(states.TELEPORT);

        // message
        playerUtil.sendActionTextToAll(languageUtil.gets("uhc-command-start-teleport")); // "§fTéléportation des joueurs."

        // teleport players
        // TODO :: load map - create tp platforms - tp ppl - tps fix

        // start PREGAME
        startPREGAME();
    }

    public void startPREGAME() {
        // set state
        plugin.getGameConfig().setGameState(states.PREGAME);

        // disable chat
        plugin.getGameConfig().setChatState(states.CHAT_DISABLED);
        playerUtil.sendMessageToAll(languageUtil.gets("uhc-chat-now-disabled"));

        // TODO :: clear platforms && temps blocks

        // setup active players (survival, creative, adventure) -> info + scoreboard
        for (Player player: playerUtil.getPlayers(new GameMode[]{GameMode.SURVIVAL, GameMode.CREATIVE, GameMode.ADVENTURE})) {
            // create joueur
            activePlayer activePlayer = setupActivePlayer(player);

            // setup infos
            activePlayer.setAlive(true);
            activePlayer.setScoreboard(playerUtil.createScoreboard(player));
        }

        // TODO :: setup spectators (spectator)
        for (Player player: playerUtil.getPlayers(GameMode.SPECTATOR)) {
            player.sendMessage(languageUtil.gets("uhc-now-spectator"));
        }

        // update scoreboards
        for (activePlayer activePlayer: Player_Active.values()) {
            playerUtil.updateGlobalScoreboard(activePlayer.getUUID(), activePlayer.getScoreboard());
        }

        // start timers
        timerUtil.startInGame();
        timerUtil.startCycle();
        timerUtil.startEpisode();
    }

    public void startGAME() {
        // set state
        plugin.getGameConfig().setGameState(states.GAME);
        // TODO
    }

    public void startEND() {
        // set state
        plugin.getGameConfig().setGameState(states.END);
        // TODO
    }

    // stops
    public void stopSTART() {
        // set state
        plugin.getGameConfig().setGameState(states.WAIT);

        // stop timers
        timerUtil.stopCountdown(true);
    }

    public void stopPREGAME() {
        // clear player inventories, effects, extra health, remove scoreboard
        // set state
        plugin.getGameConfig().setGameState(states.WAIT);

        // stop timers
        timerUtil.stopCycle();
        timerUtil.stopInGame();
        timerUtil.stopEpisode();
        timerUtil.stopCountdown(false);

        // apply changes
        for (activePlayer activePlayer: getActivePlayers()) {
            final Player player = activePlayer.getPlayer();

            // clear inventories
            player.getInventory().clear();

            // clear each armor pieces (holy this is really annoying)
            if (player.getInventory().getHelmet() != null) {
                player.getInventory().setHelmet(null);
            }

            if (player.getInventory().getChestplate() != null) {
                player.getInventory().setChestplate(null);
            }

            if (player.getInventory().getLeggings() != null) {
                player.getInventory().setLeggings(null);
            }

            if (player.getInventory().getBoots() != null) {
                player.getInventory().setBoots(null);
            }

            // update inventories
            player.updateInventory();

            // clear effects
            playerUtil.clearEffects(player);

            // remove extra health
            player.setMaxHealth(20);

            // remove scoreboards
            playerUtil.removeScoreboard(player);
        }

        // clear lists
        Player_Active.clear();
        Active_Player.clear();
    }

    public void stopGAME() {
        stopPREGAME();

        // destroy joueur class

    }
}
