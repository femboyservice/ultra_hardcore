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
import scriptservice.ultra_hardcore.classes.states;
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

    // init
    private static gameUtil gameUtil;

    @Override
    public void init(PluginManager pluginManager) {
        gameUtil = plugin.gameUtil;

        plugin.getCommand("uhc").setExecutor(this);
        plugin.getCommand("uhc").setTabCompleter(this);
    }

    // command stuff
    private static final ArrayList<String> mainCommands = new ArrayList<>(); {
        mainCommands.add("help");
        mainCommands.add("start");
        mainCommands.add("stop");
        mainCommands.add("setgroup");
        mainCommands.add("settings");
    }

    private static final ArrayList<String> secondCommands = new ArrayList<>(); {
        secondCommands.add("start");
        secondCommands.add("stop");
        secondCommands.add("setgroup");
        secondCommands.add("settings");
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
                actionName = "help";
                // donnes des informations sur l'uhc (% d'effet, limite de stuff, etc.)
            } else if (argsAmount == 1) {
                final String subCommand = strings[0].toLowerCase();

                if (mainCommands.contains(subCommand)) {
                    actionName = subCommand;
                } else {
                    player.sendMessage(languageUtil.gets("general-command-introuvable", new Object[]{subCommand}));
                    return true;
                    // introuvable
                }
            } else if (argsAmount == 2) {
                final String subCommand = strings[0].toLowerCase();
                arg = strings[1];

                if (mainCommands.contains(subCommand)) {
                    actionName = subCommand;
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

    public static class commandUtil {
        private static void commandStart(Player player) {
            if (player == null) {return;}

            // not op
            final boolean isOp = player.isOp();
            if (!isOp) {
                languageUtil.sendS(player, "noperms");
                return;
            }

            // already started
            if (plugin.getGameConfig().getGameState() != states.WAIT) {
                languageUtil.sendS(player, "uhc-command-start-already");
                return;
            }

            // set main world
            gameUtil.setWorldName(player.getWorld().getName());

            // launch
            gameUtil.startSTART();
        }

        private static void commandStop(Player player) {
            if (player == null) {return;}

            // not op
            final boolean isOp = player.isOp();
            if (!isOp) {
                languageUtil.sendS(player, "noperms");
                return;
            }

            // stop for each gameState
            final states gameState = plugin.getGameConfig().getGameState();
            switch (gameState) {
                case WAIT:
                    // nothing to stop (srupid)
                    languageUtil.sendS(player, "uhc-command-stop-nothing");
                    return;


                case START:
                    // stop start timers ?
                    gameUtil.stopSTART();
                    return;


                case TELEPORT:
                    return; // cuz fuck you (you had 10 SECONDS !!)

                case PREGAME:
                    // clear player inventories, effects, extra health, remove scoreboard
                    gameUtil.stopPREGAME();
                    return;
                case GAME:
                    // PREGAME + destroy joueur class
                    gameUtil.stopGAME();

                    return;
                case END:
                    player.sendMessage(ChatColor.RED + "pas fait");
                    return;


                default: // not possible? || who tf set state to CHAT_(DISABLED || ENABLED) || NIGHT || DAY ??
            }
        }

        private static void commandSetGroup(Player player, String arg) {
            if (player == null) {return;}

            // not op
            final boolean isOp = player.isOp();
            if (!isOp) {
                languageUtil.sendS(player, "noperms");
                return;
            }

            // check gameState
            if (plugin.getGameConfig().getGameState() == states.WAIT) {
                player.sendMessage(languageUtil.gets("uhc-command-setgroup-game-not-started"));
                return;
            }

            // check arg
            try {
                gameUtil.setGroup(player, Integer.parseInt(arg));
            } catch (Exception e) {
                player.sendMessage(languageUtil.gets("global-command-arg-not-integer", new Object[]{arg}));
            }
        }

        public static void run(Player player, String actionName, String arg) {
            switch (actionName) {
                case "help":
                    if (arg == null || arg.isEmpty()) {
                        player.sendMessage(languageUtil.getm("uhc-command-help"));
                    } else {
                        arg = arg.toLowerCase();
                        if (mainCommands.contains(arg)) {
                            player.sendMessage(languageUtil.getm("uhc-command" + "-" + arg + "-help"));
                        } else {
                            player.sendMessage(languageUtil.gets("general-command-introuvable", new Object[]{arg}));
                        }
                    }

                    break;
                case "start":
                    commandStart(player);
                    break;
                case "stop":
                    commandStop(player);
                    break;
                case "setgroup":
                    commandSetGroup(player, arg);
                    break;

                case "abracadabra": // me demande pas pourquoi
                    break;
                default:
                    player.sendMessage(languageUtil.getErrorPrefix() + (ChatColor.WHITE + "J'ai pas encore fait la sous-commande ") + (ChatColor.AQUA + actionName) + (ChatColor.WHITE + ", donc faut attendre."));
                    break;
            }
        }
    }
}
