package scriptservice.ultra_hardcore.commands.subcommands.uhc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptservice.ultra_hardcore.classes.subcommand;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.languageUtil;
import scriptservice.ultra_hardcore.utils.playerUtil;

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
        for (String string: strings) {
            content.append(ChatColor.translateAlternateColorCodes('&', string)).append(" ");
        }

        // send it
        playerUtil.sendMessageToAll(languageUtil.getm("uhc-command-say", new Object[]{commandSender.getName(), content.toString()}));
    }
}
