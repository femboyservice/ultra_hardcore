package scriptservice.ultra_hardcore.commands;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.StringUtil;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.classes.scenarioManager;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.languageUtil;
import scriptservice.ultra_hardcore.utils.playerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * commande usage: /scenarios <sub>
 * description: scenarios command holder
 */
public class scenariosCommand extends initManager implements CommandExecutor, TabCompleter, Listener {
    public scenariosCommand(uhc plugin) {
        super(plugin);
    }

    // init
    @Getter private static Inventory scenarioInventory;
    private static final Map<Integer, scenarioManager> scenarioMapper = new HashMap<>();

    @Override
    public void init(PluginManager pluginManager) {
        //--// init inventory
        scenarioInventory = Bukkit.createInventory(null, 36, (ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Scénarios"));

        // register commands
        plugin.getCommand("scenarios").setExecutor(this);
        plugin.getCommand("scenarios").setTabCompleter(this);

        plugin.getCommand("sc").setExecutor(this);
        plugin.getCommand("sc").setTabCompleter(this);

        // register events
        pluginManager.registerEvents(this, plugin);
    }

    // command executor
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            final int argsAmount = strings.length;
            String actionName;
            String arg = null;

            if (argsAmount == 0) {
                actionName = "null";
                //
            } else if (argsAmount == 1) {
                final String subCommand = strings[0];
                if (subCommand.equalsIgnoreCase("help")) {
                    actionName = "help";
                    //
                } else if (subCommand.equalsIgnoreCase("list")) {
                    actionName = "list";
                    //
                } else {
                    player.sendMessage(languageUtil.gets("general-command-introuvable", new Object[]{subCommand}));
                    return true;
                    // introuvable
                }

            } else if (argsAmount == 2) {
                final String subCommand = strings[0];
                arg = strings[1];

                if (subCommand.equalsIgnoreCase("help")) {
                    actionName = "help";
                } else {
                    player.sendMessage(languageUtil.gets("general-command-introuvable", new Object[]{subCommand}));
                    return true;
                    // introuvable
                }

            } else {
                // euh, trop d'args
                player.sendMessage(languageUtil.gets("general-command-too-many-args", new Object[]{2, argsAmount}));
                return true;
            }

            // handle actionName
            commandUtil.run(player, actionName, arg);
        }

        return true;
    }

    // tab completer
    private final ArrayList<String> mainCommands = new ArrayList<>(); {
        mainCommands.add("help");
    }

    private final  ArrayList<String> secondCommands = new ArrayList<>(); {
        secondCommands.add("quiver");
        secondCommands.add("stonevariants");
        secondCommands.add("cutclean");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        final List<String> completions = new ArrayList<>();

        if (strings.length == 1) {
            StringUtil.copyPartialMatches(strings[0], mainCommands, completions);
        } else if (strings.length == 2) {
            StringUtil.copyPartialMatches(strings[0], secondCommands, completions);
        }

        return completions;
    }

    //--// inventory stuff
    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent event) {
        // wrong inventory
        if (!event.getInventory().equals(scenarioInventory)) {
            return;
        }

        // don't move item
        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();

        // current item is not null
        if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) {
            return;
        }

        // consts
        final Player player = (Player) event.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        scenarioManager scenario = scenarioMapper.get(event.getRawSlot());
        if (scenario != null) {
            scenario.setEnabled(!scenario.isEnabled());
            commandUtil.updateInventory();

            playerUtil.sendMessageToAll(languageUtil.gets("scenarios-" + (scenario.isEnabled() ? "enabled" : "disabled"), new Object[]{scenario.getName()}));
        } else {
            player.sendMessage("No scenario exists for slot " + event.getRawSlot());
        }
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryDragEvent(final InventoryDragEvent event) {
        if (event.getInventory().equals(scenarioInventory)) {
            event.setCancelled(true);
        }
    }

    // inner-helper class
    public static class commandUtil {
        public static void updateInventory() {
            // clear inventory & values
            scenarioInventory.clear();
            scenarioMapper.clear();

            // filler items
            for (int i = 27; i < 36; i++) {
                scenarioInventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
            }

            // scenario items
            int index = 0;
            for (scenarioManager scenario: plugin.scenarioManagers) {
                scenarioMapper.put(index, scenario);
                scenarioInventory.addItem(scenario.getItem(scenario.isEnabled()));

                index++;
            }
        }

        public static void run(Player player, String actionName, String arg) {
            if (actionName.equals("help")) {
                // no arg => main command
                if (arg == null) {
                    player.sendMessage(languageUtil.getm("scenarios-command-help"));
                    return;
                }

                // arg found => specific
                String[] description = null;
                for (scenarioManager scenario: plugin.scenarioManagers) {
                    if (scenario.getName().equalsIgnoreCase(arg)) {
                        description = scenario.getFullDescription();
                        break;
                    }
                }

                if (description != null) {
                    player.sendMessage(description);
                } else {
                    player.sendMessage(languageUtil.gets("scenarios-command-scenario-null", new Object[]{arg}));
                }
            } else if (actionName.equals("null")) {
                updateInventory();
                player.openInventory(getScenarioInventory());
            } else {
                player.sendMessage(languageUtil.getErrorPrefix() + (ChatColor.WHITE + "J'ai pas encore fait la sous-commande ") + (ChatColor.AQUA + actionName) + (ChatColor.WHITE + ", donc faut attendre."));
            }
        }
    }
}
