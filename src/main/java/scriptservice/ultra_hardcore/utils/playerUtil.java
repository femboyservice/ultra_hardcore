package scriptservice.ultra_hardcore.utils;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class playerUtil extends initManager {
    public playerUtil(uhc plugin) {
        super(plugin);
    }

    @Override
    public void init(PluginManager pluginManager) {}

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
