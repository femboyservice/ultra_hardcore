package scriptservice.ultra_hardcore.events;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.activePlayer;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.gameUtil;
import scriptservice.ultra_hardcore.utils.languageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * event usage: Global
 * description: enchantments-related event
 */
public class enchantmentLimiter extends initManager implements Listener {
    public enchantmentLimiter(uhc plugin) {
        super(plugin);
    }

    // init
    private gameUtil gameUtil;
    @Override
    public void init(PluginManager pluginManager) {
        gameUtil = plugin.gameUtil;
        pluginManager.registerEvents(this, plugin); // register event
    }

    private final ArrayList<Enchantment> modifiedEnchantments = new ArrayList<>(); {
        modifiedEnchantments.add(Enchantment.DAMAGE_ALL); // sharpness
        modifiedEnchantments.add(Enchantment.ARROW_DAMAGE); // power
        modifiedEnchantments.add(Enchantment.PROTECTION_ENVIRONMENTAL); // protection
        modifiedEnchantments.add(Enchantment.FIRE_ASPECT); // fire aspect
        modifiedEnchantments.add(Enchantment.ARROW_FIRE); // flame
        modifiedEnchantments.add(Enchantment.KNOCKBACK); // knockback
        modifiedEnchantments.add(Enchantment.ARROW_KNOCKBACK); // punch

    }

    private final HashMap<Enchantment, String> enchantmentNames = new HashMap<>(); {
        enchantmentNames.put(Enchantment.DAMAGE_ALL, "Tranchant");
        enchantmentNames.put(Enchantment.ARROW_DAMAGE, "Puissance");
        enchantmentNames.put(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection");
        enchantmentNames.put(Enchantment.FIRE_ASPECT, "Aura de feu");
        enchantmentNames.put(Enchantment.ARROW_FIRE, "Flamme");
        enchantmentNames.put(Enchantment.KNOCKBACK, "Recul");
        enchantmentNames.put(Enchantment.ARROW_KNOCKBACK, "Frappe");
    }

    private int getMaxLevel(activePlayer activePlayer, ItemStack enchantedItem, Enchantment enchantment) {
        if (enchantment.equals(Enchantment.ARROW_DAMAGE)) {
            return activePlayer.getPowerMax();
        } else if (enchantment.equals(Enchantment.DAMAGE_ALL)) {
            if (enchantedItem.getType() == Material.DIAMOND_SWORD) {
                return activePlayer.getDiamondSharpnessMax();
            } else {
                return activePlayer.getOthersSharpnessMax();
            }
        } else if (enchantment.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) {
            if (enchantedItem.getType() == Material.DIAMOND_HELMET || enchantedItem.getType() == Material.DIAMOND_CHESTPLATE || enchantedItem.getType() == Material.DIAMOND_LEGGINGS || enchantedItem.getType() == Material.DIAMOND_BOOTS) {
                return activePlayer.getDiamondProtectionMax();
            } else {
                return activePlayer.getOthersProtectionMax();
            }
        } else if (enchantment.equals(Enchantment.FIRE_ASPECT)) {
            return activePlayer.getFireAspectMax();
        } else if (enchantment.equals(Enchantment.ARROW_FIRE)) {
            return activePlayer.getFlameMax();
        } else if (enchantment.equals(Enchantment.KNOCKBACK)) {
            return activePlayer.getKnockbackMax();
        } else if (enchantment.equals(Enchantment.ARROW_KNOCKBACK)) {
            return activePlayer.getPunchMax();
        } else {
            return enchantment.getMaxLevel(); // not possible normally, fallback incase
        }
    }

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent event) {
        // prevent bad event format
        if (event == null) {return;}
        if (event.getItem() == null) {return;}

        // consts
        final Player player = event.getEnchanter();
        final ItemStack enchantedItem = event.getItem();
        Map<Enchantment, Integer> enchantments = event.getEnchantsToAdd(); // enchantement, enchantementLevel

        // activePlayer check
        final Optional<activePlayer> optionalActivePlayer = gameUtil.isPlayerActive(player);
        if (!optionalActivePlayer.isPresent()) {return;}
        final activePlayer activePlayer = optionalActivePlayer.get();

        if (enchantments == null) {return;}
        if (enchantments.isEmpty()) {return;}

        for (Map.Entry<Enchantment, Integer> entry: new HashMap<>(enchantments).entrySet()) { // woa | blehhh ConcurrentModificationException
            final Enchantment enchantment = entry.getKey();
            final int enchantementLevel = entry.getValue();

            // not modified one
            if (enchantment == null) {continue;}
            if (!modifiedEnchantments.contains(enchantment)) {continue;}

            // consts
            int maxLevel = getMaxLevel(activePlayer, enchantedItem, enchantment);

            // en dessous du cap => cbon
            if (enchantementLevel <= maxLevel) {continue;}

            if (maxLevel > 0) {
                enchantments.replace(enchantment, enchantementLevel, maxLevel);
                languageUtil.sendS(player, "enchantlimiter-too-high", new Object[]{enchantmentNames.getOrDefault(enchantment, enchantment.getName()), enchantementLevel, maxLevel});
            } else {
                enchantments.remove(enchantment);
                languageUtil.sendS(player, "enchantlimiter-removed", new Object[]{enchantmentNames.getOrDefault(enchantment, enchantment.getName())});
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        // consts
        final Inventory inventory = event.getInventory();
        final int rawSlot = event.getRawSlot();
        ItemStack resultItem = event.getCurrentItem();

        Player player = null;
        for (HumanEntity entity: inventory.getViewers()) {
            if (entity instanceof Player) {
                player = (Player) entity;
                break;
            }
        }

        // activePlayer check
        final Optional<activePlayer> optionalActivePlayer = gameUtil.isPlayerActive(player);
        if (!optionalActivePlayer.isPresent()) {return;}
        final activePlayer activePlayer = optionalActivePlayer.get();

        // not anvil inv
        if (inventory.getType() != InventoryType.ANVIL) {return;}

        // not wanted slot (2: result item from anvil)
        if (rawSlot != 2) {return;}

        // no item on result
        if (resultItem == null || resultItem.getType() == Material.AIR) {return;}

        // item doesnt have meta
        if (!resultItem.hasItemMeta()) {return;}

        // item doesnt have enchants
        ItemMeta itemMeta = resultItem.getItemMeta();
        if (!itemMeta.hasEnchants()) {return;}

        // modify the item
        Map<Enchantment, Integer> enchantments = itemMeta.getEnchants();
        boolean modifiedOutput = false;

        if (enchantments != null) {
            for (Map.Entry<Enchantment, Integer> entry: new HashMap<>(enchantments).entrySet()) { // cme ?
                // consts
                final Enchantment enchantment = entry.getKey();
                final int enchantementLevel = entry.getValue();
                int maxLevel = getMaxLevel(activePlayer, resultItem, enchantment);

                // not modified one
                if (!modifiedEnchantments.contains(enchantment)) {continue;}

                // en dessous du cap => cbon
                if (enchantementLevel <= maxLevel) {continue;}

                if (maxLevel > 0) {
                    resultItem.removeEnchantment(enchantment);
                    resultItem.addUnsafeEnchantment(enchantment, maxLevel);
                    modifiedOutput = true;

                    languageUtil.sendS(player, "enchantlimiter-too-high", new Object[]{enchantmentNames.getOrDefault(enchantment, enchantment.getName()), enchantementLevel, maxLevel});
                } else {
                    resultItem.removeEnchantment(enchantment);
                    modifiedOutput = true;

                    languageUtil.sendS(player, "enchantlimiter-removed", new Object[]{enchantmentNames.getOrDefault(enchantment, enchantment.getName())});
                }
            }
        }

        // update item if needed
        if (modifiedOutput) {
            event.setCurrentItem(resultItem);
        }
    }
}
