package scriptservice.ultra_hardcore.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.languageUtil;

/**
 * event usage: Global
 * description: projectile-related event (ender pearl)
 */
public class projectileLimiter extends initManager implements Listener {
    public projectileLimiter(uhc plugin) {
        super(plugin);
    }

    // init
    private languageUtil languageUtil;

    @Override
    public void init(PluginManager pluginManager) {
        languageUtil = plugin.languageUtil;

        pluginManager.registerEvents(this, plugin); // register event
    }

    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
        final Projectile projectile = event.getEntity();

        if (projectile instanceof EnderPearl) {
            if (projectile.getShooter() instanceof Player) {
                if (!plugin.getGameConfig().isPearlEnabled()) {
                    final Player player = (Player) projectile.getShooter();

                    // cancel event
                    event.setCancelled(true);

                    // send message
                    player.sendMessage(languageUtil.gets("cannot-interact-item"));

                    // give item (if not creative or spec)
                    if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
                        player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                    }
                }
            }
        }
    }
}
