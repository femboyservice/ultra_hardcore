package scriptservice.ultra_hardcore.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.languageUtil;

/**
 * event usage: Global
 * description: bucket-related event (bucket as in the item)
 */
public class bucketLimiter extends initManager implements Listener {
    public bucketLimiter(uhc plugin) {
        super(plugin);
    }

    // init
    @Override
    public void init(PluginManager pluginManager) {
        pluginManager.registerEvents(this, plugin); // register event
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = event.getItemStack(); // afaik :: returns the bucket you'll get ?
        final Block blockClicked = event.getBlockClicked();

        final boolean cancelEvent = (
                (itemStack.getType() == Material.WATER_BUCKET && !plugin.getGameConfig().isWaterEnabled()) || // water
                (itemStack.getType() == Material.LAVA_BUCKET && !plugin.getGameConfig().isLavaEnabled()) // lava
        );


        if (cancelEvent) {
            // reset event
            event.setCancelled(true);

            // set stuff
            Bukkit.getScheduler().runTask(plugin, () -> {
                // reset given itemstack + source block
                itemStack.setType(Material.AIR);
                itemStack.setAmount(0);
                blockClicked.setType(blockClicked.getType());

                // send message
                player.sendMessage(languageUtil.gets("cannot-interact-block"));

                // send updates to fix ghost blocks
                player.sendBlockChange(blockClicked.getLocation(), blockClicked.getType(), blockClicked.getData());
                player.updateInventory();
            });
        }
    }
}
