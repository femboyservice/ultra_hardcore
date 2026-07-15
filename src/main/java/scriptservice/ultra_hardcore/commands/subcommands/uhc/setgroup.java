package scriptservice.ultra_hardcore.commands.subcommands.uhc;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import scriptservice.ultra_hardcore.classes.scoreboardLines;
import scriptservice.ultra_hardcore.classes.states;
import scriptservice.ultra_hardcore.classes.subcommand;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.languageUtil;
import scriptservice.ultra_hardcore.utils.playerUtil;

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
            final int group = Integer.parseInt(arg);

            // set config
            plugin.getGameConfig().setGameGroups(group);

            // set line
            gameUtil.updateGlobalLine(scoreboardLines.GROUPS, group);

            // send title
            playerUtil.sendTitleToAll(
                    ChatColor.GOLD+""+ChatColor.BOLD + "⚠" + ChatColor.WHITE + " Groupes de " + ChatColor.DARK_GREEN + group + " " + ChatColor.GOLD + ChatColor.BOLD + "⚠"
                    ,
                    ChatColor.GREEN + "Veuillez respecter la limite de groupe.",
                    3, 20, 5
            );

            // play sound
            playerUtil.playSoundToAll(Sound.ENDERDRAGON_HIT, 1.2f, 1.0f);

            // send message to commandSender
            commandSender.sendMessage(languageUtil.gets("uhc-command-setgroup-new-group", new Object[]{group}));
        } catch (Exception e) {
            commandSender.sendMessage(languageUtil.gets("global-command-arg-not-integer", new Object[]{arg}));
        }
    }
}
