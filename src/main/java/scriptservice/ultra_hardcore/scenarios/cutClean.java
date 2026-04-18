package scriptservice.ultra_hardcore.scenarios;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;
import scriptservice.ultra_hardcore.classes.scenarioManager;
import scriptservice.ultra_hardcore.uhc;

import java.util.*;

/**
 * scenario usage: Cut Clean
 * description: when ores/raw food is dropped, set it to its corresponding cooked item.
 * ores :: iron / gold
 * cooked :: porkchop / beef / chicken / mutton / rabbit
 */
public class cutClean extends scenarioManager implements Listener {
    public cutClean(uhc plugin) {
        super(plugin);
    }

    @Override
    public void init(PluginManager pluginManager) {
        pluginManager.registerEvents(this, plugin); // register event

        // define class stuff
        name = "CutClean";
        itemDescription = new String[]{
                (ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "-------") + (ChatColor.WHITE  + "" + ChatColor.BOLD + "∎") + (ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "-------"),
                (ChatColor.GRAY + "Ores & Raw Foods are cooked when dropped.")
        };

        fullDescription = new String[]{
                plugin.stringUtil.getInfoPrefix() + (ChatColor.YELLOW+"Ores") +
                        (ChatColor.WHITE+" and ") +
                        (ChatColor.GOLD+"Foods") +
                        (ChatColor.WHITE+" are cooked when dropped, this includes:"),

                (ChatColor.DARK_GRAY+"> ") + (ChatColor.YELLOW+"Iron Ore"),
                (ChatColor.DARK_GRAY+"> ") + (ChatColor.YELLOW+"Gold Ore"),
                (ChatColor.DARK_GRAY+"> ") + (ChatColor.GOLD+"Porkchop"),
                (ChatColor.DARK_GRAY+"> ") + (ChatColor.GOLD+"Beef"),
                (ChatColor.DARK_GRAY+"> ") + (ChatColor.GOLD+"Chicken"),
                (ChatColor.DARK_GRAY+"> ") + (ChatColor.GOLD+"Mutton"),
                (ChatColor.DARK_GRAY+"> ") + (ChatColor.GOLD+"Rabbit")

        };

        itemMaterial = Material.IRON_BLOCK;
        setEnabled(false);
    }

    // ores
    private final List<Material> acceptedPickaxeTiers = new ArrayList<>(); {
        acceptedPickaxeTiers.add(Material.DIAMOND_PICKAXE);
        acceptedPickaxeTiers.add(Material.IRON_PICKAXE);
        acceptedPickaxeTiers.add(Material.STONE_PICKAXE); // should be gold only, too lazy tho ^^

    }

    // animals
    private final List<EntityType> acceptedAnimals = new ArrayList<>(); {
        acceptedAnimals.add(EntityType.PIG);
        acceptedAnimals.add(EntityType.COW);
        acceptedAnimals.add(EntityType.CHICKEN);
        acceptedAnimals.add(EntityType.SHEEP);
        acceptedAnimals.add(EntityType.RABBIT);
    }

    private final Map<Material, Material> changedAnimalsDrop = new HashMap<>(); {
        changedAnimalsDrop.put(Material.PORK, Material.GRILLED_PORK);
        changedAnimalsDrop.put(Material.RAW_BEEF, Material.COOKED_BEEF);
        changedAnimalsDrop.put(Material.RAW_CHICKEN, Material.COOKED_CHICKEN);
        changedAnimalsDrop.put(Material.MUTTON, Material.COOKED_MUTTON);
        changedAnimalsDrop.put(Material.RABBIT, Material.COOKED_RABBIT);
    }

    // methods
    public final ItemStack getItem(boolean enabled) {
        ItemStack itemStack = new ItemStack(itemMaterial);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName((enabled ? ChatColor.GREEN : ChatColor.RED) + name);
        itemMeta.setLore(Arrays.asList(itemDescription));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    // event
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (!isEnabled()) {return;} // not enabled

        // consts
        final Player player = event.getPlayer();
        final ItemStack heldItem = player.getItemInHand();
        final Block block = event.getBlock();
        final Material material = block.getType();
        final World world = block.getWorld();

        // creative player
        if (player.getGameMode() == GameMode.CREATIVE) {return;}

        // not holding pickaxe
        if (!acceptedPickaxeTiers.contains(heldItem.getType())) {return;}

        // not affected block
        if (block.getType() != Material.IRON_ORE && block.getType() != Material.GOLD_ORE) {return;}



        // cancel drop
        block.setType(Material.AIR);

        // get informations
        Material droppedMaterial;
        int droppedExp;

        if (material == Material.GOLD_ORE) {
            droppedMaterial = Material.GOLD_INGOT;
            droppedExp = 2;
        } else {
            droppedMaterial = Material.IRON_INGOT;
            droppedExp = 1;
        }

        // drop cooked item
        final Location droppedLocation = block.getLocation().add(new Vector(0.5, 0.5, 0.5));
        final Item item = world.dropItem(droppedLocation, new ItemStack(droppedMaterial, 1));
        item.setVelocity(new Vector(0, 0, 0));

        // give xp
        player.giveExp(droppedExp);
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (!isEnabled()) {return;} // not enabled

        // consts
        final LivingEntity entity = event.getEntity();

        // not affected entity
        if (!acceptedAnimals.contains(entity.getType())) {return;}

        // change drops
        final List<ItemStack> drops = event.getDrops();
        for (ItemStack itemStack: drops) {
            if (changedAnimalsDrop.containsKey(itemStack.getType())) {
                itemStack.setType(changedAnimalsDrop.get(itemStack.getType()));
            }
        }
    }
}
