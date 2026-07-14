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
import scriptservice.ultra_hardcore.classes.subcommand;
import scriptservice.ultra_hardcore.commands.subcommands.uhc.*;
import scriptservice.ultra_hardcore.uhc;

import scriptservice.ultra_hardcore.utils.*;

import java.util.*;

/**
 * commande usage: /uhc <sub> <sub>
 * description: uhc command holder
 */
public class uhcCommand extends initManager implements CommandExecutor, TabCompleter {
    public uhcCommand(uhc plugin) {
        super(plugin);
    }

    // command stuff
    private static final ArrayList<String> mainCommands = new ArrayList<>(); {
        mainCommands.add("help");
        mainCommands.add("say");
        mainCommands.add("start");
        mainCommands.add("stop");
        mainCommands.add("setgroup");
        mainCommands.add("settings");
    }

    private static final ArrayList<String> secondCommands = (ArrayList<String>) mainCommands.clone();
    static {
        secondCommands.remove("help");
    }

    // init
    private static final HashMap<String, subcommand> subCommands = new HashMap<>();
    private static final HashMap<String, Object[]> subCommandsObjects = new HashMap<>();

    @Override
    public void init(PluginManager pluginManager) {
        // register commands
        plugin.getCommand("uhc").setExecutor(this);
        plugin.getCommand("uhc").setTabCompleter(this);

        // put subCommands
        subCommands.put("help", new help(plugin));
        subCommands.put("say", new say(plugin));
        subCommands.put("setgroup", new setgroup(plugin));
        subCommands.put("start", new start(plugin));
        subCommands.put("stop", new stop(plugin));

        // put subCommandsObjects (on va me crasher dessus pour ceci mais nsr)
        subCommandsObjects.put("help", new Object[]{mainCommands});
    }

    // command executor
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            final int argsAmount = strings.length;
            String actionName;

            if (argsAmount == 0) {
                actionName = "help";
                // donnes des informations sur l'uhc (% d'effet, limite de stuff, etc.)
            } else if (argsAmount <= 2) {
                final String subCommand = strings[0].toLowerCase();

                if (mainCommands.contains(subCommand)) {
                    actionName = subCommand;
                } else {
                    player.sendMessage(languageUtil.gets("general-command-introuvable", new Object[]{subCommand}));
                    return true;
                    // introuvable
                }

            } else {
                final String subCommand = strings[0].toLowerCase();
                actionName = subCommand;

                if (!subCommand.equalsIgnoreCase("say")) {
                    // euh, trop d'args
                    player.sendMessage(languageUtil.gets("general-command-too-many-args", new Object[]{2, argsAmount}));
                    return true;
                }
            }

            // handle actionName
            final subcommand subcommand = subCommands.get(actionName);
            final Object[] objects = subCommandsObjects.getOrDefault(actionName, null);
            if (subcommand != null) {
                subcommand.run(player, strings, objects); // je te promets que si je dois envoyer 14 millard d'info, je le fait - femboysanslimite, 14/07/2026, 11:13 UTC+2
            } else {
                player.sendMessage(languageUtil.getErrorPrefix() + (ChatColor.WHITE + "J'ai pas encore fait la sous-commande ") + (ChatColor.AQUA + actionName) + (ChatColor.WHITE + ", donc faut attendre."));
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        final List<String> completions = new ArrayList<>();

        if (strings.length == 1) {
            StringUtil.copyPartialMatches(strings[0], mainCommands, completions);
        } else if (strings.length == 2) {
            if ((strings[0]).equalsIgnoreCase("help")) {
                StringUtil.copyPartialMatches(strings[1], secondCommands, completions);
            }
        }

        return completions;
    }
}
