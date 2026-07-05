package scriptservice.ultra_hardcore.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.*;
import scriptservice.ultra_hardcore.uhc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Getter private final HashMap<UUID, scoreboardSign> scoreboardSigns = new HashMap<>();

    @Getter @Setter private String worldName = "world";

    // per-class methods
    // map related
    public double getWorldBorderSize() {
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

    // scoreboards
    public scoreboardSign createScoreboard(Player player) {
        final UUID uuid = player.getUniqueId();
        scoreboardSign scoreboard = scoreboardSigns.get(uuid);

        if (scoreboard != null) {
            return scoreboard;
        } else {
            scoreboard = new scoreboardSign(player, (ChatColor.GOLD+""+ChatColor.BOLD+" ultra_hardcore  "));
            scoreboard.create();

            scoreboard.setLine(scoreboardLines.DATE, (ChatColor.GRAY + DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDateTime.now())));
            scoreboard.setLine(2,  (ChatColor.WHITE + " "));
            scoreboard.setLine(3,  (ChatColor.DARK_GRAY+"▎ ") + (ChatColor.GOLD+"Informations"));
            scoreboard.setLine(scoreboardLines.PLAYERS,  (ChatColor.DARK_GRAY+"▎ ") + (ChatColor.WHITE+"Joueurs: ") + (ChatColor.RED+"X"));
            scoreboard.setLine(scoreboardLines.GAME_TIME,  (ChatColor.DARK_GRAY+"▎ ") + (ChatColor.WHITE+"Durée: ") + (ChatColor.YELLOW+"0s"));
            scoreboard.setLine(scoreboardLines.GROUPS,  (ChatColor.DARK_GRAY+"▎ ") + (ChatColor.WHITE+"Groupe: ") + (ChatColor.RED+"X"));
            scoreboard.setLine(scoreboardLines.CYCLE,  (ChatColor.DARK_GRAY+"▎ ") + (ChatColor.WHITE+"Cycle: ") + (ChatColor.YELLOW+"Nuit"));
            scoreboard.setLine(8,  (ChatColor.GRAY + "  "));
            scoreboard.setLine(scoreboardLines.BORDER,  (ChatColor.DARK_GRAY+"▎ ") + (ChatColor.WHITE+"Bordure: ") + (ChatColor.RED+"± X"));
            scoreboard.setLine(scoreboardLines.EPISODE, (ChatColor.DARK_GRAY+"▎ ") + (ChatColor.WHITE+"Episode: ") + (ChatColor.YELLOW+"8"));
            scoreboard.setLine(scoreboardLines.KILLS, (ChatColor.DARK_GRAY+"▎ ") + (ChatColor.WHITE+"Kills: ") + (ChatColor.RED+"X"));
            scoreboard.setLine(scoreboardLines.ASSISTS, (ChatColor.DARK_GRAY+"▎ ") + (ChatColor.WHITE+"Assists: ") + (ChatColor.RED+"X"));
            scoreboard.setLine(13, (ChatColor.DARK_GRAY + "   "));
            scoreboard.setLine(14, (ChatColor.GRAY+""+ChatColor.ITALIC+"> femboysanslimite"));

            scoreboardSigns.put(uuid, scoreboard);
            return scoreboard;
        }
    }

    public void updateGlobalScoreboard(UUID uuid, scoreboardSign scoreboard) {
        if (scoreboard == null) {
            return;
        }

        updateScoreboard(scoreboard, scoreboardLines.PLAYERS, getActivePlayers().size()); // we pray
        updateScoreboard(scoreboard, scoreboardLines.GROUPS, plugin.getGameConfig().getGameGroups());
        updateScoreboard(scoreboard, scoreboardLines.CYCLE, (plugin.getGameConfig().isCycle() ? "Jour" : "Nuit"));
        updateScoreboard(scoreboard, scoreboardLines.EPISODE, plugin.getGameConfig().getGameEpisode());

        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }

        final Optional<activePlayer> optionalActivePlayer = isPlayerActive(player);
        optionalActivePlayer.ifPresent(activePlayer -> {
            updateScoreboard(scoreboard, scoreboardLines.KILLS, activePlayer.getKills());
            updateScoreboard(scoreboard, scoreboardLines.ASSISTS, activePlayer.getAssists());
        }); // J'AIME TROP WTFF
    }

    public scoreboardSign createScoreboard(UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return null; }
        return createScoreboard(player);
    }

    public void updateScoreboard(scoreboardSign scoreboard, scoreboardLines scoreboardLine, Object content) {
        if (scoreboard == null) {return;}
        if (content == null) {return;}

        String finalContent = (ChatColor.DARK_GRAY+"▎ ");
        final String formattedContent = (content.toString());

        switch (scoreboardLine) {
            case PLAYERS:
                finalContent += (ChatColor.WHITE+"Joueurs: ") + (ChatColor.YELLOW+formattedContent);
                break;
            case GAME_TIME:
                finalContent += (ChatColor.WHITE+"Durée: ") + (ChatColor.YELLOW+formattedContent);
                break;
            case GROUPS:
                finalContent += (ChatColor.WHITE+"Groupe: ") + (ChatColor.YELLOW+formattedContent);
                break;
            case CYCLE:
                finalContent += (ChatColor.WHITE+"Cycle: ") + (ChatColor.YELLOW+formattedContent);
                break;
            case BORDER:
                finalContent += (ChatColor.WHITE+"Bordure: ") + (ChatColor.YELLOW+"± "+formattedContent);
                break;
            case EPISODE:
                finalContent += (ChatColor.WHITE+"Episode: ") + (ChatColor.YELLOW+formattedContent);
                break;
            case KILLS:
                finalContent += (ChatColor.WHITE+"Kills: ") + (ChatColor.YELLOW+formattedContent);
                break;
            case ASSISTS:
                finalContent += (ChatColor.WHITE+"Assists: ") + (ChatColor.YELLOW+formattedContent);
                break;
        }

        scoreboard.setLine(scoreboardLine, finalContent);
    }

    public void updateScoreboard(UUID uuid, scoreboardLines scoreboardLine, Object content) {
        final scoreboardSign scoreboard = getScoreboard(uuid);
        updateScoreboard(scoreboard, scoreboardLine, content);
    }

    public void updateScoreboard(activePlayer activePlayer, scoreboardLines scoreboardLine, Object content) {
        updateScoreboard(activePlayer.getUUID(), scoreboardLine, content);
    }

    public scoreboardSign getScoreboard(UUID uuid) {
        return scoreboardSigns.get(uuid);
    }

    public void removeScoreboard(UUID uuid) {
        final scoreboardSign scoreboard = getScoreboard(uuid);
        if (scoreboard != null) {
            scoreboard.destroy();

            scoreboardSigns.remove(uuid);
        }
    }

    public void removeScoreboard(Player player) {
        removeScoreboard(player.getUniqueId());
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
            activePlayer.setScoreboard(createScoreboard(player));
        }

        // TODO :: setup spectators (spectator)
        for (Player player: playerUtil.getPlayers(GameMode.SPECTATOR)) {
            player.sendMessage(languageUtil.gets("uhc-now-spectator"));
        }

        // update scoreboards
        for (activePlayer activePlayer: Player_Active.values()) {
            updateGlobalScoreboard(activePlayer.getUUID(), activePlayer.getScoreboard());
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
            removeScoreboard(player);
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
