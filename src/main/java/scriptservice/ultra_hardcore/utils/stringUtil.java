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
    private final String resetEscape = (ChatColor.RESET + "\n");
    @Getter private final String infoPrefix = ((ChatColor.DARK_GRAY + "{") + (ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "ultra_hardcore") + (ChatColor.DARK_GRAY + "} ")) + ChatColor.RESET;
    @Getter private final String errorPrefix = ((ChatColor.DARK_GRAY + "{") + (ChatColor.RED + "" + ChatColor.BOLD + "ultra_hardcore") + (ChatColor.DARK_GRAY + "} ")) + ChatColor.RESET;

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
                return " " + resetEscape +
                        (ChatColor.DARK_GRAY + "«") + getInfoPrefix() + (ChatColor.DARK_GRAY + "»") + resetEscape +
                        ChatColor.RED + "Veuillez vous connectez en utilisant " +
                        ChatColor.AQUA + "Lunar Client" +
                        ChatColor.RED + ".";

            case "general-command-too-many-args":
                if (objects.length != 2) {return (codename+"-not-enough-args");}
                return (getErrorPrefix() + "Trop d'arguments donné dans la commande.") + ((ChatColor.DARK_GRAY + " (max: ") + (ChatColor.DARK_AQUA + objects[0].toString()) + (ChatColor.DARK_GRAY + ", given: ") + (ChatColor.DARK_AQUA + objects[1].toString()) + (ChatColor.DARK_GRAY + ")"));
            case "general-command-infindable":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return getErrorPrefix() + (ChatColor.WHITE + "Sous-commande ") + (ChatColor.AQUA + objects[0].toString()) + (ChatColor.WHITE + " introuvable ..?");

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
