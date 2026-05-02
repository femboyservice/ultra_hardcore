package scriptservice.ultra_hardcore.utils;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;

import java.util.Arrays;

import static org.bukkit.Bukkit.getServer;

public class playerUtil extends initManager {
    public playerUtil(uhc plugin) {
        super(plugin);
    }

    // init
    @Override
    public void init(PluginManager pluginManager) {}

    //--// per-class methods
    // messages
    public final void sendMessageToAll(String text) {
        for (Player player: Bukkit.getOnlinePlayers()) {
            player.sendMessage(text);
        }
    }

    public final void sendMessageToAll(String[] texts) {
        for (Player player: Bukkit.getOnlinePlayers()) {
            player.sendMessage(texts);
        }
    }

    // action bar
    // from https://www.spigotmc.org/threads/how-to-send-action-bar-in-spigot-1-7-x-1-8-x.93800/
    public void sendActionText(Player player, String message){
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void sendActionTextToAll(String message) {
        for (Player player: getServer().getOnlinePlayers()) {
            sendActionText(player, message);
        }
    }

    // inventory
    public final int countMaterial(Player player, Material material) {
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

    public final ItemStack[] getItemStacks(Player player, Material material) {
        return Arrays.stream(player.getInventory().getContents())
                .filter(itemStack -> (itemStack != null && itemStack.getType() == material))
                .toArray(ItemStack[]::new);
    }

    // sounds
    public void playSound(Player player, Sound soundPlayed, float volume, float pitch) {
        Bukkit.getScheduler().runTask(plugin, () -> player.playSound(player.getLocation(), soundPlayed, volume, pitch));
    }

    public void playSound(Player player, Sound soundPlayed) {
        playSound(player, soundPlayed, 1f, 1f);
    }

    public void playSoundToAll(Sound soundPlayed, float volume, float pitch) {
        for (Player player: getServer().getOnlinePlayers()) {
            Bukkit.getScheduler().runTask(plugin, () -> player.playSound(player.getLocation(), soundPlayed, volume, pitch));
        }
    }

    public void playSoundToAll(Sound soundPlayed) {
        playSoundToAll(soundPlayed, 1, 1);
    }

}
