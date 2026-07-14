package scriptservice.ultra_hardcore.commands.subcommands.uhc;

import org.bukkit.entity.Player;
import scriptservice.ultra_hardcore.classes.subcommand;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.languageUtil;
import scriptservice.ultra_hardcore.utils.playerUtil;

import java.util.ArrayList;

public class help extends subcommand {
    public help(uhc plugin) {
        super(plugin);
    }

    @Override
    public void run(Player commandSender, String[] strings, Object[] objects) {
        if (objects == null || objects.length == 0) {return;}
        if (commandSender == null) {return;}

        final String arg = (strings.length >= 2 ? strings[1].toLowerCase() : "").toLowerCase();
        final ArrayList<String> mainCommands = (ArrayList<String>) objects[0];

        if (mainCommands == null) {return;}

        playerUtil.sendMessageToAll("arg: " + arg);
        playerUtil.sendMessageToAll("mainCommands: " + mainCommands);

        if (arg.isEmpty()) {
            commandSender.sendMessage(languageUtil.getm("uhc-command-help"));
        } else {
            if (mainCommands.contains(arg)) {
                commandSender.sendMessage(languageUtil.getm("uhc-command" + "-" + arg + "-help"));
            } else {
                commandSender.sendMessage(languageUtil.gets("general-command-introuvable", new Object[]{arg}));
            }
        }
    }
}
