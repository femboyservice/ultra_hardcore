package scriptservice.ultra_hardcore.utils;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import scriptservice.ultra_hardcore.classes.activePlayer;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.classes.scoreboardLines;
import scriptservice.ultra_hardcore.classes.scoreboardSign;
import scriptservice.ultra_hardcore.uhc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class playerUtil extends initManager {
    public playerUtil(uhc plugin) {
        super(plugin);
    }

    // init
    @Override
    public void init(PluginManager pluginManager) {}

    //--// per-class vars/vals
    @Getter private final static HashMap<UUID, scoreboardSign> scoreboardSigns = new HashMap<>();

    //--// per-class methods
    // player
    public static Player[] getPlayers() {
        return new ArrayList<>(Bukkit.getOnlinePlayers()).toArray(new Player[0]);
    }

    public static Player[] getPlayers(GameMode neededGameMode) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == neededGameMode) {
                players.add(player);
            }
        }

        return players.toArray(new Player[0]);
    }

    public static Player[] getPlayers(GameMode[] neededGameModes) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (Arrays.asList(neededGameModes).contains(player.getGameMode())) {
                players.add(player);
            }
        }

        return players.toArray(new Player[0]);
    }

    // messages
    public static void sendMessageToAll(String text) {
        for (Player player: Bukkit.getOnlinePlayers()) {
            player.sendMessage(text);
        }
    }

    public static void sendMessageToAll(String[] texts) {
        for (Player player: Bukkit.getOnlinePlayers()) {
            player.sendMessage(texts);
        }
    }

    // action bar
    // from https://www.spigotmc.org/threads/how-to-send-action-bar-in-spigot-1-7-x-1-8-x.93800/
    public static void sendActionText(Player player, String message){
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendActionTextToAll(String message) {
        for (Player player: getServer().getOnlinePlayers()) {
            sendActionText(player, message);
        }
    }

    // effects
    public static void clearEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    public static void giveEffect(Player player, PotionEffect potionEffect) {
        Bukkit.getScheduler().runTask(plugin, () -> player.addPotionEffect(potionEffect));
    }

    public static void giveEffect(Player player, Collection<PotionEffect> collection) {
        Bukkit.getScheduler().runTask(plugin, () -> player.addPotionEffects(collection));
    }

    // scoreboard
    public static scoreboardSign createScoreboard(Player player) {
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

    public static void updateGlobalScoreboard(UUID uuid, scoreboardSign scoreboard) {
        if (scoreboard == null) {
            return;
        }

        final gameUtil gameUtil = plugin.gameUtil; // TODO :: GTFO
        updateScoreboard(scoreboard, scoreboardLines.PLAYERS, gameUtil.getActivePlayers().size()); // we pray
        updateScoreboard(scoreboard, scoreboardLines.GROUPS, plugin.getGameConfig().getGameGroups());
        updateScoreboard(scoreboard, scoreboardLines.CYCLE, (plugin.getGameConfig().isCycle() ? "Jour" : "Nuit"));
        updateScoreboard(scoreboard, scoreboardLines.EPISODE, plugin.getGameConfig().getGameEpisode());

        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }

        final Optional<activePlayer> optionalActivePlayer = gameUtil.isPlayerActive(player);
        optionalActivePlayer.ifPresent(activePlayer -> {
            updateScoreboard(scoreboard, scoreboardLines.KILLS, activePlayer.getKills());
            updateScoreboard(scoreboard, scoreboardLines.ASSISTS, activePlayer.getAssists());
        }); // J'AIME TROP WTFF
    }

    public static scoreboardSign createScoreboard(UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return null; }
        return createScoreboard(player);
    }

    public static void updateScoreboard(scoreboardSign scoreboard, scoreboardLines scoreboardLine, Object content) {
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

    public static void updateScoreboard(UUID uuid, scoreboardLines scoreboardLine, Object content) {
        final scoreboardSign scoreboard = getScoreboard(uuid);
        updateScoreboard(scoreboard, scoreboardLine, content);
    }

    public static void updateScoreboard(activePlayer activePlayer, scoreboardLines scoreboardLine, Object content) {
        updateScoreboard(activePlayer.getUUID(), scoreboardLine, content);
    }

    public static scoreboardSign getScoreboard(UUID uuid) {
        return scoreboardSigns.get(uuid);
    }

    public static void removeScoreboard(UUID uuid) {
        final scoreboardSign scoreboard = getScoreboard(uuid);
        if (scoreboard != null) {
            scoreboard.destroy();

            scoreboardSigns.remove(uuid);
        }
    }

    public static void removeScoreboard(Player player) {
        removeScoreboard(player.getUniqueId());
    }

    // inventory
    public static int countMaterial(Player player, Material material) {
        if (player == null || material == null) { return 0; }
        int total = 0;

        for (ItemStack itemStack: player.getInventory().getContents()) {
            if (itemStack != null) {
                if (itemStack.getType() == material) {
                    total += itemStack.getAmount();
                }
            }
        }

        return total;
    }

    public static ItemStack[] getItemStacks(Player player, Material material) {
        return Arrays.stream(player.getInventory().getContents())
                .filter(itemStack -> (itemStack != null && itemStack.getType() == material))
                .toArray(ItemStack[]::new);
    }

    // sounds
    public static void playSound(Player player, Sound soundPlayed, float volume, float pitch) {
        Bukkit.getScheduler().runTask(plugin, () -> player.playSound(player.getLocation(), soundPlayed, volume, pitch));
    }

    public static void playSound(Player player, Sound soundPlayed) {
        playSound(player, soundPlayed, 1f, 1f);
    }

    public static void playSoundToAll(Sound soundPlayed, float volume, float pitch) {
        for (Player player: getServer().getOnlinePlayers()) {
            Bukkit.getScheduler().runTask(plugin, () -> player.playSound(player.getLocation(), soundPlayed, volume, pitch));
        }
    }

    public static void playSoundToAll(Sound soundPlayed) {
        playSoundToAll(soundPlayed, 1f, 1f);
    }

}
