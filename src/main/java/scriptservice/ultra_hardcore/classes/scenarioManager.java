package scriptservice.ultra_hardcore.classes;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.uhc;

public abstract class scenarioManager {
    // consts
    protected static uhc plugin;

    // constructor
    public scenarioManager(uhc plugin) {
        scenarioManager.plugin = plugin;
    }

    // methods
    public abstract void init(PluginManager pluginManager);
    public abstract ItemStack getItem(boolean enabled);

    // vars
    @Getter public String name;
    @Getter public String[] fullDescription;
    @Getter public String[] itemDescription;
    @Getter public Material itemMaterial;
    @Getter @Setter private boolean enabled;
}
