package scriptservice.ultra_hardcore.commands.subcommands.uhc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptservice.ultra_hardcore.classes.subcommand;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.languageUtil;

import java.util.Arrays;

public class say extends subcommand {
    public say(uhc plugin) {
        super(plugin);
    }

    @Override
    public void run(Player commandSender, String[] strings, Object[] objects) {
        if (commandSender == null) {return;}

        // not op
        final boolean isOp = commandSender.isOp();
        if (!isOp) {
            languageUtil.sendS(commandSender, "noperms");
            return;
        }

        // get full message
        StringBuilder content = new StringBuilder();
        Arrays.stream(strings).skip(1).forEach(string -> content.append(ChatColor.translateAlternateColorCodes('&', string)).append(" "));

        // send it
        gameUtil.say(commandSender, content.toString());
    }
}
