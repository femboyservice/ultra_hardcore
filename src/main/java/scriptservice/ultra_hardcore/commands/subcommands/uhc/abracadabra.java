package scriptservice.ultra_hardcore.commands.subcommands.uhc;

import org.bukkit.entity.Player;
import scriptservice.ultra_hardcore.classes.subcommand;
import scriptservice.ultra_hardcore.uhc;

public class abracadabra extends subcommand {
    public abracadabra(uhc plugin) {
        super(plugin);
    }

    @Override
    public void run(Player commandSender, String[] strings, Object[] objects) {
        if (commandSender == null) {return;}
        final String arg = (strings.length > 0 ? strings[0] : "");


    }
}
