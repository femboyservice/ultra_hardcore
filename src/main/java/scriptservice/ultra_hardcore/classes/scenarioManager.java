package scriptservice.ultra_hardcore.classes;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.uhc;

import java.util.Arrays;

public abstract class scenarioManager {
    // consts
    protected static uhc plugin;

    // constructor
    public scenarioManager(uhc plugin) {
        scenarioManager.plugin = plugin;
    }

    // methods
    public abstract void init(PluginManager pluginManager);

    public final ItemStack getItem(boolean enabled) {
        ItemStack itemStack = new ItemStack(itemMaterial);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName((enabled ? ChatColor.GREEN : ChatColor.RED) + name);
        itemMeta.setLore(Arrays.asList(itemDescription));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    // vars
    @Getter public String name;
    @Getter public String[] fullDescription;
    @Getter public String[] itemDescription;
    @Getter public Material itemMaterial;
    @Getter @Setter private boolean enabled;
}
