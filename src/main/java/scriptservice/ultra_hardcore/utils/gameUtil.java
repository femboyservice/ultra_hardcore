package scriptservice.ultra_hardcore.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;
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
    private final HashMap<String, Byte> colorMap = new HashMap<>(); {
        colorMap.put("white", (byte) 0);
        colorMap.put("orange", (byte) 1);
        colorMap.put("magenta", (byte) 2);

        colorMap.put("lblue", (byte) 3);
        colorMap.put("l_blue", (byte) 3);
        colorMap.put("light_blue", (byte) 3);
        colorMap.put("lightblue", (byte) 3);

        colorMap.put("yellow", (byte) 4);
        colorMap.put("lime", (byte) 5);
        colorMap.put("pink", (byte) 6);
        colorMap.put("gray", (byte) 7);
        colorMap.put("grey", (byte) 7);

        colorMap.put("lgray", (byte) 8);
        colorMap.put("l_gray", (byte) 8);
        colorMap.put("light_gray", (byte) 8);
        colorMap.put("lightgray", (byte) 8);

        colorMap.put("lgrey", (byte) 8);
        colorMap.put("l_grey", (byte) 8);
        colorMap.put("light_grey", (byte) 8);
        colorMap.put("lightgrey", (byte) 8);

        colorMap.put("cyan", (byte) 9);
        colorMap.put("purple", (byte) 10);
        colorMap.put("blue", (byte) 11);
        colorMap.put("brown", (byte) 12);
        colorMap.put("green", (byte) 13);
        colorMap.put("red", (byte) 14);
        colorMap.put("black", (byte) 15);

        colorMap.put("GLASS", (byte) 16);
        colorMap.put("glass", (byte) 16);

        colorMap.put("barrier", (byte) 17);

        colorMap.put("bedrock", (byte) 18);
    }
    private final ArrayList<Block> temporaryBlocks = new ArrayList<>();

    // -- per-class methods
    // map related
    public Optional<World> getWorld() {
        return Optional.of(Bukkit.getWorld(getWorldName()));
    }

    public double getWorldBorderSize() {
        return getWorld().map(world -> (world.getWorldBorder().getSize() / 2)).orElse(0.0); // olala
    }

    public Location getWorldCenter() {
        return getWorld().map(world -> new Location(world, 0, world.getHighestBlockYAt(0, 0), 0)).orElse(null);
    }

    private void setBlockType(Block block, Material material) {
        Bukkit.getScheduler().runTask(plugin, () -> block.setType(material, false));
    }

    @SuppressWarnings("deprecation")
    private void setBlockColorData(Block block, byte color) {
        Bukkit.getScheduler().runTask(plugin, () -> block.setData(color));
    }

    private void spawnPlatform(int radius, Location location, byte color, boolean walls) {
        // consts
        Material materialUsed = Material.STAINED_GLASS;
        final World world = location.getWorld();
        int centerX = location.getBlockX();
        int centerY = location.getBlockY();
        int centerZ = location.getBlockZ();

        // decide custom material
        switch (color) {
            case ((byte) 16):
                materialUsed = Material.GLASS;
                break;
            case ((byte) 17):
                materialUsed = Material.BARRIER;
                break;
            case ((byte) 18):
                materialUsed = Material.BEDROCK;
                break;
        }

        // square around location with radius;
        for (int offsetX = -radius; offsetX <= radius; offsetX++) {
            for (int offsetZ = -radius; offsetZ <= radius; offsetZ++) {
                // get block
                Block block = world.getBlockAt(centerX + offsetX, centerY, centerZ + offsetZ);

                // pre-set materials
                if (block.getLocation().equals(location)) {
                    // middle block
                    setBlockType(block, Material.BEDROCK);
                } else if ((offsetZ == radius || offsetZ == -radius || offsetX == radius || offsetX == -radius) & walls) {
                    // walls
                    setBlockType(block, Material.BEDROCK);

                    Block wallBlock = world.getBlockAt(centerX + offsetX, centerY+2, centerZ + offsetZ);
                    setBlockType(wallBlock, Material.BARRIER);
                    temporaryBlocks.add(wallBlock);
                } else {
                    // others
                    setBlockType(block, materialUsed);
                }

                // color glass if material is stained_glass
                if (materialUsed == Material.STAINED_GLASS) {
                    setBlockColorData(block, color);
                }

                // add to unbreakable blocks
                temporaryBlocks.add(block);
            }
        }
    }

    private void spawnPlatform(int radius, Location location, String color, boolean walls) {
        spawnPlatform(
                radius,
                location,
                ((colorMap.get(color) != null) ? colorMap.get(color) : (byte) 16),  // defaults to white
                walls
        );
    }

    private Location getRandomLocation(int radius, int y) {
        int range = ((radius)-(-radius)+1);
        int randomX = (int) (Math.random() * range) + (-radius);
        int randomZ = (int) (Math.random() * range) + (-radius);

        return getWorld().map(world -> new Location(world, randomX, y, randomZ)).orElse(null); // giga kiffe
    }

    public Location spawnRandomPlatform(int spawnRadius, int y, int platformRadius, String color, boolean walls) {
        Location newLocation = getRandomLocation(spawnRadius, y);
        spawnPlatform(platformRadius, newLocation, color, walls);

        return new Location(newLocation.getWorld(), (newLocation.getBlockX() + 0.5), (newLocation.getBlockY() + 2), (newLocation.getBlockZ() + 0.5)); // +2 blocks height for player location
    }

    private void clearTemporaryBlocks() {
        for (Block block: temporaryBlocks) {
            setBlockType(block, Material.AIR);
        }

        temporaryBlocks.clear();
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

    public Collection<activePlayer> getActivePlayers(boolean connected) {
        Collection<activePlayer> collection = new ArrayList<>();
        for (activePlayer activePlayer: getActivePlayers()) {
            if (activePlayer.isConnected() == connected) {
                collection.add(activePlayer);
            }
        }

        return collection;
    }

    public Optional<activePlayer> isPlayerActive(UUID uuid) {
        for (activePlayer activePlayer: getActivePlayers()) {
            if (activePlayer.getUUID().equals(uuid)) {
                return Optional.of(activePlayer);
            }
        }

        return Optional.empty();
    }

    public Optional<activePlayer> isPlayerActive(Player player) {
        if (player == null) {return Optional.empty();}
        return isPlayerActive(player.getUniqueId());
    }

    // scoreboards
    public scoreboardSign createScoreboard(Player player) {
        if (player == null) {return null;}
        final UUID uuid = player.getUniqueId();
        scoreboardSign scoreboard = scoreboardSigns.get(uuid);

        if (scoreboard != null) {
            return scoreboard;
        } else {
            // (ChatColor.GRAY + "Date: " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now()))
            scoreboard = new scoreboardSign(player, (ChatColor.DARK_GREEN+""+ChatColor.BOLD+"ultra_hardcore   "));
            scoreboard.create();

            scoreboard.setLine(0, (" "));
            scoreboard.setLine(1,  ((ChatColor.DARK_GREEN+"▏ ") + (""+ChatColor.WHITE+ChatColor.BOLD+"Partie")));
            scoreboard.setLine(scoreboardLines.PLAYERS,  ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Joueurs: ") + (ChatColor.GREEN+"0")));
            scoreboard.setLine(scoreboardLines.GROUPS,  ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Groupes: ") + (ChatColor.GREEN+"0")));
            scoreboard.setLine(scoreboardLines.GAME_TIME,  ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Durée: ") + (ChatColor.GREEN+"0s")));
            scoreboard.setLine(5,  ("  "));
            scoreboard.setLine(6,  ((ChatColor.DARK_GREEN+"▏ ") + (""+ChatColor.WHITE+ChatColor.BOLD+"Informations")));
            scoreboard.setLine(scoreboardLines.KILLS,  ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Kills: ") + (ChatColor.GREEN+"0")));
            scoreboard.setLine(scoreboardLines.ASSISTS,  ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Assists: ") + (ChatColor.GREEN+"0")));
            scoreboard.setLine(9, ("   "));
            scoreboard.setLine(10, ((ChatColor.DARK_GREEN+"▏ ") + (""+ChatColor.WHITE+ChatColor.BOLD+"Bordure")));
            scoreboard.setLine(scoreboardLines.BORDER, ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Taille: ") + (ChatColor.GREEN+"± 0.0")));
            scoreboard.setLine(scoreboardLines.BORDER_DISTANCE, ((ChatColor.DARK_GRAY +" ▪ ") + (ChatColor.WHITE+"Distance: ") + (ChatColor.GREEN+"? 0")));
            scoreboard.setLine(13, ("    "));
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


        if (scoreboardLine.getLine() >= 0 && scoreboardLine.getLine() <= 14) {
            scoreboard.setLine(
                    scoreboardLine,
                    (ChatColor.DARK_GRAY+" ▪ ") + (ChatColor.WHITE + scoreboardLine.getPrefix() + ": ") + (ChatColor.GREEN + content.toString())
            );
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
        if (player == null) {return;}
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

    public void stop(Player commandSender) {
        // stop for each gameState
        final states gameState = plugin.getGameConfig().getGameState();
        switch (gameState) {
            case WAIT:
                // nothing to stop (srupid)
                languageUtil.sendS(commandSender, "uhc-command-stop-nothing");
                return;


            case START:
                // stop start timers ?
                stopSTART();
                return;


            case TELEPORT:
                return; // cuz fuck you (you had 10 SECONDS !!)

            case GAME:
                // clear player inventories, effects, extra health, remove scoreboard
                stopGAME();

                return;
            case END:
                commandSender.sendMessage(ChatColor.RED + "pas fait ;L");
                return;


            default: // not possible? || who tf set state to CHAT_(DISABLED || ENABLED) || NIGHT || DAY ??
        }
    }

    public void start(Player commandSender) {
        // set main world
        setWorldName(commandSender.getWorld().getName());

        // launch
        startSTART();
    }

    public void say(Player commandSender, String content) {
        // send it
        playerUtil.sendMessageToAll(languageUtil.getm("uhc-command-say", new Object[]{commandSender.getName(), content}));
    }

    // timer stuff
    public void teleport() {
        startTELEPORT();
    }

    // starts
    private void startSTART() {
        // set state
        plugin.getGameConfig().setGameState(states.START);

        // set worldborder
        getWorld().ifPresent(world -> world.getWorldBorder().setSize(plugin.getGameConfig().getBorderSize()));

        // start timer to tp
        timerUtil.startCountdown(3);
    }

    private void startTELEPORT() {
        // set state
        plugin.getGameConfig().setGameState(states.TELEPORT);

        // message
        playerUtil.sendActionTextToAll(languageUtil.gets("uhc-command-start-teleport")); // "§fTéléportation des joueurs."

        // create platforms
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (!(player.getGameMode() == GameMode.SPECTATOR)) {
                // -- !spectators
                // create platform
                Location platformLocation = spawnRandomPlatform(
                        (int) getWorldBorderSize(),
                        150,
                        3,
                        "green",
                        true
                );

                // teleport with delay
                player.teleport(platformLocation);
                playerUtil.sendMessageToAll(languageUtil.gets("uhc-teleport", new Object[]{player.getName()}));
            } else {
                // -- spectators
                getWorld().ifPresent(world -> player.teleport(new Location(world, 0, world.getHighestBlockYAt(0, 0), 0)));
            }
        } // TODO :: tps fix ?

        // start game
        startGAME();
    }

    private void startGAME() {
        // set state
        plugin.getGameConfig().setGameState(states.GAME);

        // schedule task 5 sec later
        Timer tempTimer = new Timer();
        tempTimer.scheduleAtFixedRate(new TimerTask() {
            int finalCounter = 5;

            @Override
            public void run() {
                if (finalCounter > 0) {
                    // send title
                    playerUtil.sendTitleToAll(
                            (ChatColor.DARK_GREEN + "" + ChatColor.BOLD + finalCounter),
                            null,
                            5, 20, 0
                    );

                    // update
                    finalCounter -= 1;
                } else {
                    // disable chat
                    plugin.getGameConfig().setChatState(states.CHAT_DISABLED);
                    playerUtil.sendMessageToAll(languageUtil.gets("uhc-chat-now-disabled"));

                    // clear temp blocks
                    clearTemporaryBlocks();

                    // setup active players (survival, creative, adventure) -> info + scoreboard
                    for (Player player: playerUtil.getPlayers(new GameMode[]{GameMode.SURVIVAL, GameMode.CREATIVE, GameMode.ADVENTURE})) {
                        // create joueur
                        activePlayer activePlayer = setupActivePlayer(player);

                        // setup infos
                        activePlayer.setAlive(true);
                        activePlayer.setScoreboard(createScoreboard(player));

                        // set no damage ticks to 5 secs
                        final int time = 5; // in seconds
                        activePlayer.setInvincible(true);

                        // send start and end message
                        player.sendMessage(languageUtil.gets("uhc-invincibility-start", new Object[]{time}));
                        plugin.getServer().getScheduler().runTaskLater(
                                plugin,
                                () -> {
                                    activePlayer.setInvincible(false);
                                    player.sendMessage(languageUtil.gets("uhc-invincibility-end"));
                                },
                                convertionUtil.secondToTick(time)
                        );
                    }

                    // TODO :: setup spectators
                    for (Player player: playerUtil.getPlayers(GameMode.SPECTATOR)) {
                        player.sendMessage(languageUtil.gets("uhc-now-spectator"));
                    }

                    // update scoreboards
                    for (activePlayer activePlayer: Player_Active.values()) {
                        updateAllScoreboard(activePlayer.getUUID(), activePlayer.getScoreboard());
                    }

                    // start timers
                    timerUtil.startInGame();
                    timerUtil.startScoreboardBorder();
                    // timerUtil.startCycle();
                    // timerUtil.startEpisode();

                    // cancel & null Timer and Task
                    tempTimer.cancel();
                    this.cancel();
                }
            }
        }, Calendar.getInstance().getTime(), (long) convertionUtil.secondToMillisecond(1));
    }

    private void startEND(activePlayer winner) {
        // set state
        plugin.getGameConfig().setGameState(states.END);

        // send chat closing message
        playerUtil.sendMessageToAll(languageUtil.gets("uhc-end-closing"));

        // send title winner
        playerUtil.sendTitleToAll(languageUtil.gets("uhc-end-title"), languageUtil.gets("uhc-end-subtitle", new Object[]{winner.getPlayer().getName()}), 5, 40, 5);

        // TODO :: fireworks
        //  game stats (time)
        //  winner stats in chat
        //  topkill, topassist
        //  kill/assist leaderboard
    }

    // stops
    private void stopSTART() {
        // set state
        plugin.getGameConfig().setGameState(states.WAIT);

        // stop timers
        timerUtil.stopCountdown(true);
    }

    private void stopGAME() {
        // clear player inventories, effects, extra health, remove scoreboard
        // set state
        plugin.getGameConfig().setGameState(states.WAIT);

        // stop timers
        timerUtil.stopInGame();
        timerUtil.stopScoreboardBorder();
        timerUtil.stopCycle();
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

            // destroy?
            activePlayer.destroy();
        }

        // clear lists
        Player_Active.clear();
        Active_Player.clear();

        // enable chat
        plugin.getGameConfig().setChatState(states.CHAT_ENABLED);

        // send message
        playerUtil.sendMessageToAll(languageUtil.gets("uhc-chat-now-enabled"));
    }
}
