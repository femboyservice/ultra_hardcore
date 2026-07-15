package scriptservice.ultra_hardcore.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;

import java.util.HashMap;
import java.util.UUID;

public class movementLimiter extends initManager implements Listener {
    public movementLimiter(uhc plugin) {super(plugin);}

    // init
    @Override
    public void init(PluginManager pluginManager) {
        pluginManager.registerEvents(this, plugin); // register event
    }

    // var
    private final HashMap<UUID, Location> zones = new HashMap<>();

    // method
    public void addZone(UUID uuid, Location location) {
        zones.put(uuid, location);
    }

    public void removeZone(UUID uuid) {
        zones.remove(uuid);
    }

    // events
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location location = zones.get(player.getUniqueId());

        if (location == null) {
            return; // no zone
        }

        final Location newLocation = event.getTo();
        final Location originalLocation = event.getFrom();

        if (originalLocation.getBlockX() == newLocation.getBlockX() && originalLocation.getBlockY() == newLocation.getBlockY() && originalLocation.getBlockZ() == newLocation.getBlockZ()) {
            return; // didn't change ?
        }

        if (!newLocation.getWorld().equals(location.getWorld())) {
            return; // wrong world
        }


        // tp
        if (newLocation.distance(location) > plugin.getGameConfig().getPlatformMaxOut()) {
            player.teleport(location);
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (player != null) {
            removeZone(player.getUniqueId());
        }
    }
}
