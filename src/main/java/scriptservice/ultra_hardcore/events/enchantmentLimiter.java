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
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.stringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * event usage: Global
 * description: enchantments-related event
 */
public class enchantmentLimiter extends initManager implements Listener {
    public enchantmentLimiter(uhc plugin) {
        super(plugin);
    }

    // init
    private stringUtil stringUtil;

    @Override
    public void init(PluginManager pluginManager) {
        stringUtil = plugin.stringUtil;
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

    private int getMaxLevel(ItemStack enchantedItem, Enchantment enchantment) {
        if (enchantment.equals(Enchantment.ARROW_DAMAGE)) {
            return plugin.getGameConfig().getPowerMax();
        } else if (enchantment.equals(Enchantment.DAMAGE_ALL)) {
            if (enchantedItem.getType() == Material.DIAMOND_SWORD) {
                return plugin.getGameConfig().getDiamondSharpnessMax();
            } else {
                return plugin.getGameConfig().getOthersSharpnessMax();
            }
        } else if (enchantment.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) {
            if (enchantedItem.getType() == Material.DIAMOND_HELMET || enchantedItem.getType() == Material.DIAMOND_CHESTPLATE || enchantedItem.getType() == Material.DIAMOND_LEGGINGS || enchantedItem.getType() == Material.DIAMOND_BOOTS) {
                return plugin.getGameConfig().getDiamondProtectionMax();
            } else {
                return plugin.getGameConfig().getOthersProtectionMax();
            }
        } else if (enchantment.equals(Enchantment.FIRE_ASPECT)) {
            return plugin.getGameConfig().getFireAspectMax();
        } else if (enchantment.equals(Enchantment.ARROW_FIRE)) {
            return plugin.getGameConfig().getFlameMax();
        } else if (enchantment.equals(Enchantment.KNOCKBACK)) {
            return plugin.getGameConfig().getKnockbackMax();
        } else if (enchantment.equals(Enchantment.ARROW_KNOCKBACK)) {
            return plugin.getGameConfig().getPunchMax();
        } else {
            return enchantment.getMaxLevel(); // not possible normally, fallback incase
        }
    }

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent event) {
        final Player player = event.getEnchanter();
        final ItemStack enchantedItem = event.getItem();
        Map<Enchantment, Integer> enchantments = event.getEnchantsToAdd(); // enchantement, enchantementLevel

        for (Enchantment enchantment: enchantments.keySet()) {
            // not modified one
            if (!modifiedEnchantments.contains(enchantment)) {continue;}

            // consts
            final int enchantementLevel = enchantments.get(enchantment);
            int maxLevel = getMaxLevel(enchantedItem, enchantment);

            // en dessous du cap => cbon
            if (enchantementLevel <= maxLevel) {continue;}

            if (maxLevel > 0) {
                enchantments.replace(enchantment, enchantementLevel, maxLevel);
                stringUtil.sendS(player, "enchantlimiter-too-high", new Object[]{enchantmentNames.getOrDefault(enchantment, enchantment.getName()), enchantementLevel, maxLevel});
            } else {
                enchantments.remove(enchantment);
                stringUtil.sendS(player, "enchantlimiter-removed", new Object[]{enchantmentNames.getOrDefault(enchantment, enchantment.getName())});
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

        for (Enchantment enchantment: enchantments.keySet()) {
            // not modified one
            if (!modifiedEnchantments.contains(enchantment)) {continue;}

            // consts
            final int enchantementLevel = enchantments.get(enchantment);
            int maxLevel = getMaxLevel(resultItem, enchantment);

            // en dessous du cap => cbon
            if (enchantementLevel <= maxLevel) {continue;}

            if (maxLevel > 0) {
                resultItem.removeEnchantment(enchantment);
                resultItem.addUnsafeEnchantment(enchantment, maxLevel);
                modifiedOutput = true;

                stringUtil.sendS(player, "enchantlimiter-too-high", new Object[]{enchantmentNames.getOrDefault(enchantment, enchantment.getName()), enchantementLevel, maxLevel});
            } else {
                resultItem.removeEnchantment(enchantment);
                modifiedOutput = true;

                stringUtil.sendS(player, "enchantlimiter-removed", new Object[]{enchantmentNames.getOrDefault(enchantment, enchantment.getName())});
            }
        }

        // update item if needed
        if (modifiedOutput) {
            event.setCurrentItem(resultItem);
        }
    }
}
