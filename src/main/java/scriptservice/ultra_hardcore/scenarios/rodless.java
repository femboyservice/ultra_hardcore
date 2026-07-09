package scriptservice.ultra_hardcore.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.scenarioManager;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.convertionUtil;
import scriptservice.ultra_hardcore.utils.languageUtil;

import java.util.HashSet;
import java.util.UUID;

/**
 * scenario usage: quiver
 * description: arrow now have limits
 */
public class
rodless extends scenarioManager implements Listener {
    public rodless(uhc plugin) {
        super(plugin);
    }

    // init
    @Override
    public void init(PluginManager pluginManager) {
        pluginManager.registerEvents(this, plugin); // register event

        // define class stuff
        name = "Rodless";
        itemDescription = new String[]{
                (ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "-------") + (ChatColor.WHITE  + "" + ChatColor.BOLD + "∎") + (ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "-------"),
                (ChatColor.GRAY + "No rods.")
        };

        fullDescription = new String[]{
                languageUtil.getInfoPrefix() +
                        (ChatColor.GRAY + "Players") +
                        (ChatColor.RED + " cannot ") +
                        (ChatColor.GRAY + "craft, pickup or use") +
                        (ChatColor.AQUA + " Fishing Rods ") +
                        (ChatColor.GRAY + "anymore.")

        };

        itemMaterial = Material.FISHING_ROD;
        setEnabled(true);
    }

    // private stuff
    private final HashSet<UUID> messageCooldown = new HashSet<>();

    private void sendCooldownMessage(Player player, String message) {
        if (messageCooldown.contains(player.getUniqueId())) {return;} // jouer déjà dans la liste

        // ajoute l'uuid du joueur à la liste et envoie le message
        messageCooldown.add(player.getUniqueId());
        player.sendMessage(message);

        // enleve le cooldown apres x secondes
        Bukkit.getScheduler().runTaskLater(plugin, () -> messageCooldown.remove(player.getUniqueId()), convertionUtil.secondToTick(10));
    }

    // event // plugin.getGameConfig().getMaxArrows()
    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
        if (!isEnabled()) {return;} // not enabled

        // no item
        final Item item = event.getItem();
        if (item == null) {return;}

        // no itemstack
        final ItemStack itemStack = event.getItem().getItemStack();
        if (itemStack == null) {return;}

        // consts
        final Player player = event.getPlayer();



        // block pickup
        if (itemStack.getType() == Material.FISHING_ROD) {
            event.setCancelled(true);
            sendCooldownMessage(player, languageUtil.gets("cannot-pickup-item"));
        }
    }

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event) {
        if (!isEnabled()) { return; } // not enabled

        // inventory click not from player
        if (!(event.getWhoClicked() instanceof Player)) {return;}

        // crafted item is not related
        if (event.getRecipe().getResult().getType() != Material.FISHING_ROD) {return;}

        // consts
        final Player player = (Player) event.getWhoClicked();


        // cancel
        event.setCancelled(true);
        languageUtil.sendS(player, "cannot-craft-item");
    }

    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent event) {
        if (!isEnabled()) { return; } // not enabled

        // const
        final Player player = event.getPlayer();

        // cancel
        event.setCancelled(true);
        languageUtil.sendS(player, "cannot-use-item");
    }
}
