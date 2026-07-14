package scriptservice.ultra_hardcore.commands.subcommands.uhc;

import org.bukkit.entity.Player;
import scriptservice.ultra_hardcore.classes.states;
import scriptservice.ultra_hardcore.classes.subcommand;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.languageUtil;

public class setgroup extends subcommand {
    public setgroup(uhc plugin) {
        super(plugin);
    }

    @Override
    public void run(Player commandSender, String[] strings, Object[] objects) {
        if (commandSender == null) {return;}
        final String arg = (strings.length > 0 ? strings[0] : "");

        // not op
        final boolean isOp = commandSender.isOp();
        if (!isOp) {
            languageUtil.sendS(commandSender, "noperms");
            return;
        }

        // check gameState
        if (plugin.getGameConfig().getGameState() == states.WAIT) {
            commandSender.sendMessage(languageUtil.gets("uhc-command-setgroup-game-not-started"));
            return;
        }

        // check arg & command logic
        try {
            gameUtil.setGroup(commandSender, Integer.parseInt(arg));
        } catch (Exception e) {
            commandSender.sendMessage(languageUtil.gets("global-command-arg-not-integer", new Object[]{arg}));
        }
    }
}
