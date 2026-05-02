package scriptservice.ultra_hardcore.scenarios;

import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.scenarioManager;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.*;

import java.util.*;

/**
 * scenario usage: quiver
 * description: arrow now have limits
 */
public class quiver extends scenarioManager implements Listener {
    public quiver(uhc plugin) {
        super(plugin);
    }

    // init
    private playerUtil playerUtil;
    private languageUtil languageUtil;
    private convertionUtil convertionUtil;

    @Override
    public void init(PluginManager pluginManager) {
        playerUtil = plugin.playerUtil;
        languageUtil = plugin.languageUtil;
        convertionUtil = plugin.convertionUtil;

        pluginManager.registerEvents(this, plugin); // register event

        // define class stuff
        name = "Quiver";
        itemDescription = new String[]{
                (ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "-------") + (ChatColor.WHITE  + "" + ChatColor.BOLD + "∎") + (ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "-------"),
                (ChatColor.GRAY + "Arrows now have a limit per player")
        };

        fullDescription = new String[]{
                plugin.languageUtil.getInfoPrefix() +
                        (ChatColor.GRAY + "Arrows ") +
                        (ChatColor.WHITE + "have a limit set at ") +
                        (ChatColor.AQUA + "" + plugin.getGameConfig().getMaxArrows()) +
                        (ChatColor.WHITE + " per player.")

        };

        itemMaterial = Material.ARROW;
        setEnabled(false);
    }

    // private stuff
    private final HashSet<UUID> featherMessageCooldown = new HashSet<>();
    private final HashSet<UUID> arrowMessageCooldown = new HashSet<>();

    private void sendCooldownMessage(Player player, String message, HashSet<UUID> cooldownSet) {
        if (cooldownSet.contains(player.getUniqueId())) {return;} // jouer déjà dans la liste

        // ajoute l'uuid du joueur à la liste et envoie le message
        cooldownSet.add(player.getUniqueId());
        player.sendMessage(message);

        // enleve le cooldown apres x secondes
        Bukkit.getScheduler().runTaskLater(plugin, () -> cooldownSet.remove(player.getUniqueId()), convertionUtil.secondToTick(10));
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
        int currentArrowAmount = playerUtil.countMaterial(player, Material.ARROW);



        // block items
        if (itemStack.getType() == Material.FEATHER) {
            // has arrow in inv
            if (currentArrowAmount != 0) {
                event.setCancelled(true);
                sendCooldownMessage(player, languageUtil.gets("quiver-pickup-feather-blocked"), featherMessageCooldown);
            }
        } else if (itemStack.getType() == Material.ARROW) {
            // has feather in inv
            if (playerUtil.countMaterial(player, Material.FEATHER) != 0) {
                event.setCancelled(true);
                sendCooldownMessage(player, languageUtil.gets("quiver-pickup-arrow-blocked"), arrowMessageCooldown);
                return;
            }

            // already at max
            if (currentArrowAmount >= plugin.getGameConfig().getMaxArrows()) {
                event.setCancelled(true);
                sendCooldownMessage(player, (languageUtil.gets("quiver-arrow-blocked", new Object[]{plugin.getGameConfig().getMaxArrows()})), arrowMessageCooldown);
                return;
            }

            // ground related
            int groundArrowsAmount = itemStack.getAmount();
            if (currentArrowAmount + groundArrowsAmount > plugin.getGameConfig().getMaxArrows()) {
                int maximumAllowed = plugin.getGameConfig().getMaxArrows() - currentArrowAmount;

                // add max arrows
                player.getInventory().addItem(new ItemStack(Material.ARROW, maximumAllowed));

                // reset grounded arrows
                event.getItem().setItemStack(new ItemStack(Material.ARROW, groundArrowsAmount - maximumAllowed));

                // cancel event
                event.setCancelled(true);

                // message
                sendCooldownMessage(player, (languageUtil.gets("quiver-arrow-limit-reached", new Object[]{plugin.getGameConfig().getMaxArrows()})), arrowMessageCooldown);
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!isEnabled()) {return;} // not enabled

        // inventory click not from player
        if (!(event.getWhoClicked() instanceof Player)) {return;}

        // consts
        final Player player = (Player) event.getWhoClicked();
        ItemStack cursorItem = event.getCursor(); // item in cursor BEFORE click
        ItemStack slotItem = event.getCurrentItem(); // item in clicked slot BEFORE click

        // nulls
        if (slotItem == null || cursorItem == null) {
            return;
        }

        // cursor item not arrow
        if (cursorItem.getType() != Material.ARROW) {
            return;
        }

        // consts
        final int currentArrowAmount = playerUtil.countMaterial(player, Material.ARROW);
        final int inSlotArrowAmount = (slotItem.getType() == Material.ARROW) ? slotItem.getAmount() : 0;
        final int addingArrowAmount = cursorItem.getAmount();
        final int totalArrowsWithoutSlot = currentArrowAmount - inSlotArrowAmount;

        // if added arrows over limit
        if (totalArrowsWithoutSlot + inSlotArrowAmount + addingArrowAmount > plugin.getGameConfig().getMaxArrows()) {
            int maximumAllowed = plugin.getGameConfig().getMaxArrows() - totalArrowsWithoutSlot - inSlotArrowAmount;

            // no more arrows allowed (at max ?)
            if (maximumAllowed <= 0) {
                event.setCancelled(true);
                sendCooldownMessage(player, (languageUtil.gets("quiver-arrow-blocked", new Object[]{plugin.getGameConfig().getMaxArrows()})), arrowMessageCooldown);
                return;
            }

            // cancel event
            event.setCancelled(true);

            // add max allowed arrows
            if (event.getSlot() >= 0 && event.getSlot() < player.getInventory().getSize()) {
                slotItem = (slotItem.getType() == Material.ARROW) ? slotItem.clone() : new ItemStack(Material.ARROW, 0);

                slotItem.setAmount(inSlotArrowAmount + maximumAllowed);
                player.getInventory().setItem(event.getSlot(), slotItem);
            }

            // set remaining arrow amount
            cursorItem.setAmount(addingArrowAmount - maximumAllowed);

            // drop remaining arrows
            player.setItemOnCursor(null);
            player.getWorld().dropItemNaturally(player.getLocation().add(0.0, 0.5, 0.0), cursorItem);

            // message
            languageUtil.sendS(player, "quiver-arrow-limit-reached", new Object[]{plugin.getGameConfig().getMaxArrows()});
        }
    }

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event) {
        if (!isEnabled()) { return; } // not enabled

        // inventory click not from player
        if (!(event.getWhoClicked() instanceof Player)) {return;}

        // crafted item is not related
        if (event.getRecipe().getResult().getType() != Material.ARROW) {return;}

        // consts
        final Player player = (Player) event.getWhoClicked();
        final int currentArrowAmount = playerUtil.countMaterial(player, Material.ARROW);



        // max arrows
        if (currentArrowAmount >= plugin.getGameConfig().getMaxArrows()) {
            event.setCancelled(true);
            sendCooldownMessage(player, (languageUtil.gets("quiver-arrow-blocked", new Object[]{plugin.getGameConfig().getMaxArrows()})), arrowMessageCooldown);
        }

        // shiftclick
        if (event.isShiftClick()) {
            event.setCancelled(true);
            languageUtil.sendS(player, "quiver-shift-click");
            return;
        }

        // reached limit
        final int craftedArrowAmount = event.getRecipe().getResult().getAmount();
        final int cursorArrowAmount = ((player.getItemOnCursor() != null) ? (player.getItemOnCursor().getAmount() + craftedArrowAmount) : craftedArrowAmount);
        if (currentArrowAmount + cursorArrowAmount > plugin.getGameConfig().getMaxArrows()) {
            // inventory + crafted
            event.setCancelled(true);
            languageUtil.sendS(player, "quiver-arrow-limit-reached", new Object[]{plugin.getGameConfig().getMaxArrows()});
        }
    }
}
