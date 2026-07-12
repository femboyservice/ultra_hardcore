package scriptservice.ultra_hardcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.activePlayer;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.gameUtil;

import java.util.Optional;

/**
 * event usage: Global
 * description: cancels damage if needed
 */
public class damageLimiter extends initManager implements Listener {
    public damageLimiter(uhc plugin) {
        super(plugin);
    }

    // vars
    private gameUtil gameUtil;

    @Override
    public void init(PluginManager pluginManager) {
        gameUtil = plugin.gameUtil;
        pluginManager.registerEvents(this, plugin); // register event
    }

    @EventHandler()
    public void onEntityDamageEvent(EntityDamageEvent event) {
        // entity not player
        if (!(event.getEntity() instanceof Player)) {return;}
        final Player player = (Player) event.getEntity();

        // player not active
        final Optional<activePlayer> optionalActivePlayer = gameUtil.isPlayerActive(player);
        if (!optionalActivePlayer.isPresent()) {return;}
        final activePlayer activePlayer = optionalActivePlayer.get();

        // cancel damage
        if (activePlayer.isInvincible()) {
            event.setCancelled(true);
        }
    }
}
