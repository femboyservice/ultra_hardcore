package scriptservice.ultra_hardcore.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
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
    private String ownerName = "";
    private timerUtil timerUtil;
    @Override
    public void init(PluginManager pluginManager) {
        timerUtil = plugin.timerUtil;
        ownerName = plugin.getServer().getOfflinePlayer(UUID.fromString("0fc289a2-8dda-429a-b727-7f1e9811d747")).getName();
    }

    // per-class vars
    public HashMap<Player, activePlayer> Player_Active = new HashMap<>();
    public HashMap<activePlayer, Player> Active_Player = new HashMap<>();

    @Getter private final HashMap<UUID, scoreboardSign> scoreboardSigns = new HashMap<>();

    @Getter @Setter private String worldName = "world";

    // -- per-class methods
    // map related
    public Optional<World> getWorld() {
        return Optional.of(Bukkit.getWorld(getWorldName()));
    }

    public double getWorldBorderSize() {
        return getWorld().map(world -> (world.getWorldBorder().getSize() / 2)).orElse(0.0); // olala
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
            scoreboard = new scoreboardSign(player, (ChatColor.DARK_GREEN+""+ChatColor.BOLD+" ultra_hardcore  "));
            scoreboard.create();

            scoreboard.setLine(0, (" "));
            scoreboard.setLine(1,  ((ChatColor.DARK_GREEN+"▏ ") + (""+ChatColor.WHITE+ChatColor.BOLD+"Partie")));
            scoreboard.setLine(scoreboardLines.PLAYERS,  ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Joueurs: ") + (ChatColor.GREEN+"0")));
            scoreboard.setLine(scoreboardLines.GAME_TIME,  ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Durée: ") + (ChatColor.GREEN+"0s")));
            scoreboard.setLine(scoreboardLines.GROUPS,  ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Groupes: ") + (ChatColor.GREEN+"0")));
            scoreboard.setLine(5,  ("  "));
            scoreboard.setLine(6,  ((ChatColor.DARK_GREEN+"▏ ") + (""+ChatColor.WHITE+ChatColor.BOLD+"Informations")));
            scoreboard.setLine(scoreboardLines.KILLS,  ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Kills: ") + (ChatColor.GREEN+"0")));
            scoreboard.setLine(scoreboardLines.ASSISTS,  ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Assists: ") + (ChatColor.GREEN+"0")));
            scoreboard.setLine(9, ("   "));
            scoreboard.setLine(10, ((ChatColor.DARK_GREEN+"▏ ") + (""+ChatColor.WHITE+ChatColor.BOLD+"Bordure")));
            scoreboard.setLine(scoreboardLines.BORDER, ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Taille: ") + (ChatColor.GREEN+"± 0.0")));
            scoreboard.setLine(12, ("    "));
            scoreboard.setLine(scoreboardLines.DATE, (ChatColor.GRAY + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now())));
            scoreboard.setLine(14, ((ChatColor.GRAY+""+ChatColor.ITALIC+"Dev by " + ownerName)));

            scoreboardSigns.put(uuid, scoreboard);
            return scoreboard;
        }
    }

    public void updateGlobalLine(scoreboardLines scoreboardLine, Object content) {
        for (activePlayer activePlayer: getActivePlayers()) {
            updateScoreboard(activePlayer.getScoreboard(), scoreboardLine, content);
        }
    }

    public void updateAllScoreboard(UUID uuid, scoreboardSign scoreboard) {
        if (scoreboard == null) {
            return;
        }

        updateScoreboard(scoreboard, scoreboardLines.PLAYERS, getActivePlayers().size()); // we pray
        updateScoreboard(scoreboard, scoreboardLines.GROUPS, plugin.getGameConfig().getGameGroups());
        updateScoreboard(scoreboard, scoreboardLines.CYCLE, ((plugin.getGameConfig().getCycle() == states.DAY) ? "Jour" : "Nuit"));
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

        String finalContent = (ChatColor.DARK_GRAY+" ▪ ");
        final String formattedContent = (content.toString());

        switch (scoreboardLine) {
            case PLAYERS:
                finalContent += (ChatColor.WHITE+"Joueurs: ");
                break;
            case GAME_TIME:
                finalContent += (ChatColor.WHITE+"Durée: ");
                break;
            case GROUPS:
                finalContent += (ChatColor.WHITE+"Groupe: ") ;
                break;
            case CYCLE:
                finalContent += (ChatColor.WHITE+"Cycle: ");
                break;
            case BORDER:
                finalContent += (ChatColor.WHITE+"Bordure: ");
                break;
            case EPISODE:
                finalContent += (ChatColor.WHITE+"Episode: ");
                break;
            case KILLS:
                finalContent += (ChatColor.WHITE+"Kills: ");
                break;
            case ASSISTS:
                finalContent += (ChatColor.WHITE+"Assists: ");
                break;
        }

        finalContent += (ChatColor.GREEN+formattedContent);

        if (scoreboardLine.getLine() >= 0 && scoreboardLine.getLine() <= 14) {
            scoreboard.setLine(scoreboardLine, finalContent);
        // } else {
        //    System.out.println("[ultra_hardcore] [ERROR] gameUtil#updateScoreboard -> scoreboardLine is not between 0 and 14.");
        }
    }

    public void updateScoreboard(activePlayer activePlayer, scoreboardLines scoreboardLine, Object content) {
        updateScoreboard(activePlayer.getScoreboard(), scoreboardLine, content);
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

    // -- game related
    // commands
    public void setGroup(Player commandSender, int group) {
        // set config
        plugin.getGameConfig().setGameGroups(group);

        // set line
        updateGlobalLine(scoreboardLines.GROUPS, group);

        // send title
        playerUtil.sendTitleToAll(
                ChatColor.GOLD+""+ChatColor.BOLD + "⚠" + ChatColor.WHITE + " Groupes de " + ChatColor.DARK_GREEN + group + " " + ChatColor.GOLD + ChatColor.BOLD + "⚠"
                ,
                ChatColor.GREEN + "Veuillez respecter la limite de groupe.",
                3, 20, 5
        );

        // play sound
        playerUtil.playSoundToAll(Sound.ENDERDRAGON_HIT, 1.2f, 1.0f);

        // send message to commandSender
        commandSender.sendMessage(languageUtil.gets("uhc-command-setgroup-new-group", new Object[]{group}));

    }

    // starts
    public void startSTART() {
        // set state
        plugin.getGameConfig().setGameState(states.START);

        // set worldborder
        getWorld().ifPresent(world -> world.getWorldBorder().setSize(plugin.getGameConfig().getBorderSize()));

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
            updateAllScoreboard(activePlayer.getUUID(), activePlayer.getScoreboard());
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
