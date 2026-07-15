package scriptservice.ultra_hardcore.classes;

import org.bukkit.entity.Player;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.apolloUtil;
import scriptservice.ultra_hardcore.utils.gameUtil;

public abstract class subcommand {
    // consts
    protected static uhc plugin;
    protected gameUtil gameUtil;
    protected apolloUtil apolloUtil;

    // constructor
    public subcommand(uhc plugin) {
        subcommand.plugin = plugin;
        this.gameUtil = plugin.getGameUtil();
        this.apolloUtil = plugin.getApolloUtil();
    }

    // methods
    public abstract void run(Player commandSender, String[] strings, Object[] objects);
}
