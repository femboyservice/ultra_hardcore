package scriptservice.ultra_hardcore.commands.subcommands.uhc;

import org.bukkit.entity.Player;
import scriptservice.ultra_hardcore.classes.states;
import scriptservice.ultra_hardcore.classes.subcommand;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.languageUtil;

public class start extends subcommand {
    public start(uhc plugin) {
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

        // already started
        if (plugin.getGameConfig().getGameState() != states.WAIT) {
            languageUtil.sendS(commandSender, "uhc-command-start-already");
            return;
        }

        // set main world
        gameUtil.setWorldName(commandSender.getWorld().getName());

        // launch
        gameUtil.start(states.START);
    }
}
