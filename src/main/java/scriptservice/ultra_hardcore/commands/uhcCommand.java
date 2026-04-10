package scriptservice.ultra_hardcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.StringUtil;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;

import java.util.ArrayList;
import java.util.List;

/**
 * event usage: Global
 * description: when a player joins, sends a message, and if in config.yml "lunarclientExclusif" is set to true, checks if the player is using lunar first, then send it.
 */
public class uhcCommand extends initManager implements CommandExecutor, TabCompleter {
    public uhcCommand(uhc plugin) {
        super(plugin);
    }

    // init
    @Override
    public void init(PluginManager pluginManager) {
        plugin.getCommand("uhc").setExecutor(this);
        plugin.getCommand("uhc").setTabCompleter(this);
    }

    // command executor
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            player.sendMessage("j'ai pas encore fait les trucs donc faut attendre");
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

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        final List<String> completions = new ArrayList<>();

        if (strings.length == 1) {
            StringUtil.copyPartialMatches(strings[0], mainCommands, completions);
        } // TODO :: ADD STUFF SAID IN README.md !

        return completions;
    }
}
