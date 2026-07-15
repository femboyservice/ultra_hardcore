package scriptservice.ultra_hardcore.commands.subcommands.uhc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptservice.ultra_hardcore.classes.states;
import scriptservice.ultra_hardcore.classes.subcommand;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.languageUtil;

public class stop extends subcommand {
    public stop(uhc plugin) {
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

        // command logic
        switch (plugin.getGameConfig().getGameState()) {
            case WAIT:
                // nothing to stop (srupid)
                languageUtil.sendS(commandSender, "uhc-command-stop-nothing");
                break;

            case START:
                // stop start timers ?
                gameUtil.stop(states.START);
                break;

            case GAME:
                // clear player inventories, effects, extra health, remove scoreboard
                gameUtil.stop(states.GAME);
                break;

            case END:
                commandSender.sendMessage(ChatColor.RED + "pas fait ;L");
                break;
        }
    }
}
