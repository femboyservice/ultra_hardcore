package scriptservice.ultra_hardcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.StringUtil;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;

import scriptservice.ultra_hardcore.utils.*;

import java.util.ArrayList;
import java.util.List;

/**
 * commande usage: /uhc <sub> <sub>
 * description: uhc command holder
 */
public class uhcCommand extends initManager implements CommandExecutor, TabCompleter {
    public uhcCommand(uhc plugin) {
        super(plugin);
    }

    // init
    private static stringUtil stringUtil;

    @Override
    public void init(PluginManager pluginManager) {
        stringUtil = plugin.stringUtil;

        plugin.getCommand("uhc").setExecutor(this::onCommand);
        plugin.getCommand("uhc").setTabCompleter(this::onTabComplete);
    }

    // command executor
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            final int argsAmmount = strings.length;
            String actionName;
            String arg = null;

            if (argsAmmount == 0) {
                actionName = "null";
                // donnes des informations sur l'uhc (% d'effet, limite de stuff, etc.)
            } else if (argsAmmount == 1) {
                final String subCommand = strings[0];
                if (subCommand.equalsIgnoreCase("help")) {
                    actionName = "help";
                    // renvoie au joueur toutes les sous commandes auxquelles il a access
                } else if (subCommand.equalsIgnoreCase("start")) {
                    actionName = "start";
                    // active le cooldown pour commencer l'uhc
                } else if (subCommand.equalsIgnoreCase("stop")) {
                    actionName = "stop";
                    // stop le cooldown (si il est actif)
                } else if (subCommand.equalsIgnoreCase("settings")) {
                    actionName = "settings";
                    // ouvre un inventaire avec tous les settings changeable de l'uhc
                } else {
                    player.sendMessage(stringUtil.gets("general-command-infindable", new Object[]{subCommand}));
                    return true;
                    // introuvable
                }

            } else if (argsAmmount == 2) {
                final String subCommand = strings[0];
                arg = strings[1];

                if (subCommand.equalsIgnoreCase("help")) {
                    actionName = "help";
                } else {
                    player.sendMessage(stringUtil.gets("general-command-infindable", new Object[]{subCommand}));
                    return true;
                    // introuvable
                }

            } else {
                // euh, trop d'args
                player.sendMessage(stringUtil.gets("general-command-too-many-args", new Object[]{2, argsAmmount}));
                return true;
            }

            // handle actionName
            commandUtil.run(player, actionName, arg);
        }

        return true;
    }

    // tab completer
    private static final ArrayList<String> mainCommands = new ArrayList<>(); {
        mainCommands.add("help");
        mainCommands.add("start");
        mainCommands.add("stop");
        mainCommands.add("settings");
    }

    private static final ArrayList<String> secondCommands = new ArrayList<>(); {
        secondCommands.add("start");
        secondCommands.add("stop");
        secondCommands.add("settings");
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

    public static class commandUtil {
        public static void run(Player player, String actionName, String arg) {
            if (actionName.equals("help")) {
                final boolean isOp = player.isOp();
                player.sendMessage(stringUtil.getm("uhc-command" + ((arg == null) ? "" : ("-" + arg)) + "-help" + (isOp ? "" : "-non") + "-op"));
            } else {
                player.sendMessage(stringUtil.getErrorPrefix() + (ChatColor.WHITE + "J'ai pas encore fait la sous-commande ") + (ChatColor.AQUA + actionName) + (ChatColor.WHITE + ", donc faut attendre."));
            }
        }
    }
}
