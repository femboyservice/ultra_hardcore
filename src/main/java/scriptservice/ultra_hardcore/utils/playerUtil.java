package scriptservice.ultra_hardcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;

import java.util.Arrays;

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
}
