package scriptservice.ultra_hardcore.scenarios;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.scenarioManager;
import scriptservice.ultra_hardcore.uhc;

/**
 * scenario usage: Stone Variant
 * description: when andesite, granite or diorite is broken, set it to stone to drop cobblestone
 */
public class stoneVariant extends scenarioManager implements Listener {
    public stoneVariant(uhc plugin) {
        super(plugin);
    }

    @Override
    public void init(PluginManager pluginManager) {
        pluginManager.registerEvents(this, plugin); // register event

        // define class stuff
        name = "StoneVariants";
        itemDescription = new String[]{
                (ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "-------") + (ChatColor.WHITE  + "" + ChatColor.BOLD + "∎") + (ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "-------"),
                (ChatColor.GRAY + "Andesite, Granite, Diorite => Cobblestone.")
        };

        fullDescription = new String[]{
                plugin.languageUtil.getInfoPrefix() + (ChatColor.GRAY+"Andesite") +
                        (ChatColor.WHITE+", ") +
                        (ChatColor.GRAY+"Granite") +
                        (ChatColor.WHITE+" and ") +
                        (ChatColor.GRAY+"Diorite ") +
                        (ChatColor.WHITE+"blocks are transformed into ") +
                        (ChatColor.GRAY+"Cobblestone ") +
                        (ChatColor.WHITE+"when mined."),
        };

        itemMaterial = Material.COBBLESTONE;
        setEnabled(true);
    }


    // event
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onEvent(BlockBreakEvent event) {
        if (!isEnabled()) {return;} // not enabled

        // consts
        final Player player = event.getPlayer();
        final Block block = event.getBlock();

        // creative player
        if (player.getGameMode() == GameMode.CREATIVE) {return;}

        // not stone block
        if (block.getType() != Material.STONE) {return;}

        // if granite || diorite || andesite => set to stone
        final byte blockData = block.getData();
        if (blockData == (byte) 1 || blockData == (byte) 3 || blockData == (byte) 5) {
            event.getBlock().setData((byte) 0);
        }
    }
}
