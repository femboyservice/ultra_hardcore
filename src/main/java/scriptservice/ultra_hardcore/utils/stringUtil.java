package scriptservice.ultra_hardcore.utils;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;

public class stringUtil extends initManager {
    public stringUtil(uhc plugin) {
        super(plugin);
    }

    // globals
    @Getter private final String resetEscape = (ChatColor.RESET + "\n");
    @Getter private final String infoPrefix = ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "ultra_hardcore";
    @Getter private final String errorPrefix = ChatColor.RED + "" + ChatColor.BOLD + "ultra_hardcore";

    // init
    @Override
    public void init(PluginManager pluginManager) {}

    // per-class methods
    public final String[] getm(String codename, Object[] objects) {
        // return new String[]{};
        return new String[]{codename + "-not-found"};
    }

    public final String getm(String codename) {
        return gets(codename, null);
    }

    public final String gets(String codename, Object[] objects) {
        switch (codename) {
            case "player-kicked-not-using-lunar":
                return " " + getResetEscape() +
                        (ChatColor.DARK_GRAY + "« ") + getInfoPrefix() + (ChatColor.DARK_GRAY + " »") + getResetEscape() +
                        ChatColor.RED + "Veuillez vous connectez en utilisant " +
                        ChatColor.AQUA + "Lunar Client" +
                        ChatColor.RED + ".";
            case "player-join":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return (ChatColor.DARK_GRAY + "» ") + (ChatColor.GOLD + "" + ChatColor.BOLD + objects[0].toString()) + (ChatColor.YELLOW + " a rejoint la partie."); // [VERT-»] Darkness6115 (VERT-28/VERT-30)
            case "player-leave":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return (ChatColor.DARK_GRAY + "» ") + (ChatColor.GOLD + "" + ChatColor.BOLD + objects[0].toString()) + (ChatColor.YELLOW + " a quitté la partie."); // [ROUGE-«] Darkness6115 (ROUGE-27/ROUGE-30)
        }

        return (codename + "-not-found");
    }

    public final String gets(String codename) {
        return gets(codename, null);
    }
}
