package scriptservice.ultra_hardcore.utils;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;

public class languageUtil extends initManager {
    public languageUtil(uhc plugin) {
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
        switch (codename) {
            case "no-perms":
            case "noperms":
            case "nopermission":
            case "no-permission":
                return new String[]{
                        gets("no-permission")
                };

            case "uhc-command-help-op":
                return new String[]{
                        getInfoPrefix() + (ChatColor.WHITE + "Toutes les commandes disponibles de ") + (ChatColor.DARK_AQUA + "/") + (ChatColor.AQUA + "uhc") + (ChatColor.DARK_AQUA + " ..."),
                        "",
                        (ChatColor.DARK_GREEN + "   > ") + (ChatColor.WHITE + "" + ChatColor.ITALIC + "null"),
                        (ChatColor.GREEN + "    > ") + (ChatColor.WHITE + "help"),
                        (ChatColor.DARK_GREEN + "   > ") + (ChatColor.WHITE + "start"),
                        (ChatColor.DARK_GREEN + "   > ") + (ChatColor.WHITE + "stop"),
                        (ChatColor.DARK_GREEN + "   > ") + (ChatColor.WHITE + "settings")
                };
            case "uhc-command-help-non-op":
                return new String[]{
                        getInfoPrefix() + (ChatColor.WHITE + "Toutes les commandes disponibles de ") + (ChatColor.DARK_AQUA + "/") + (ChatColor.AQUA + "uhc") + (ChatColor.DARK_AQUA + " ..."),
                        "",
                        (ChatColor.DARK_GREEN + "   > ") + (ChatColor.WHITE + "" + ChatColor.ITALIC + "null"),
                        (ChatColor.GREEN + "    > ") + (ChatColor.WHITE + "help")
                };

            case "scenarios-command-help":
                return new String[]{
                        getInfoPrefix() + (ChatColor.WHITE + "Toutes les commandes disponibles de ") + (ChatColor.DARK_AQUA + "/") + (ChatColor.AQUA + "scenarios") + (ChatColor.DARK_AQUA + " ..."),
                        "",
                        (ChatColor.DARK_GREEN + "   > ") + (ChatColor.WHITE + "" + ChatColor.ITALIC + "null"),
                        (ChatColor.GREEN + "    > ") + (ChatColor.WHITE + "help")
                };
        }

        return new String[]{codename + "-not-found"};
    }

    public final String[] getm(String codename) {
        return getm(codename, null);
    }

    public final String gets(String codename, Object[] objects) {
        switch (codename) {
            case "no-perms":
            case "noperms":
            case "nopermission":
            case "no-permission":
                return getErrorPrefix() + (ChatColor.RED+"Vous n'avez pas la permission d'utiliser cette commande.");

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



            case "enchantlimiter-too-high":
                if (objects.length != 3) {return (codename+"-not-enough-args");}
                return getErrorPrefix() +
                        (ChatColor.RED + "L'enchantement ") +
                        (ChatColor.AQUA + objects[0].toString()) +
                        (ChatColor.RED + " a un niveau trop haut ") +
                        (ChatColor.DARK_GRAY + "(") +
                        (ChatColor.DARK_RED + objects[1].toString()) +
                        (ChatColor.DARK_GRAY + ")") +
                        (ChatColor.RED + ", il a été abaissé à sa valeur maximale de ") +
                        (ChatColor.AQUA + objects[2].toString()) +
                        (ChatColor.RED+".");
            case "enchantlimiter-removed":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return getErrorPrefix() + (ChatColor.RED + "L'enchantement ") +
                        (ChatColor.AQUA + objects[0].toString()) +
                        (ChatColor.RED + " a été supprimé car il est désactivé.");



            case "quiver-arrow-limit-reached":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return getInfoPrefix() + "Vous venez d'atteindre la limite de flèches. " +
                        (ChatColor.DARK_GRAY + "(max: ") +
                        (ChatColor.DARK_AQUA + objects[0].toString()) +
                        (ChatColor.DARK_GRAY + ")");
            case "quiver-pickup-feather-blocked":
                return getErrorPrefix() + (ChatColor.RED + "Vous ne pouvez plus ramasser de plumes, vous avez déjà des flèches dans votre inventaire.");
            case "quiver-pickup-arrow-blocked":
                return getErrorPrefix() + (ChatColor.RED + "Vous ne pouvez plus ramasser de flèches, vous avez déjà des plumes dans votre inventaire.");
            case "quiver-arrow-blocked":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return getErrorPrefix() + ChatColor.RED + "Vous ne pouvez pas avoir plus de " + objects[0].toString() + " flèches.";
            case "quiver-shift-click":
                return getErrorPrefix() + ChatColor.RED + "Veuillez crafter vos flèches sans utiliser shift-click.";



            case "player-join":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return (ChatColor.DARK_GRAY + "» ") + (ChatColor.GOLD + "" + ChatColor.BOLD + objects[0].toString()) + (ChatColor.YELLOW + " a rejoint la partie."); // [VERT-»] Darkness6115 (VERT-28/VERT-30)
            case "player-leave":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return (ChatColor.DARK_GRAY + "» ") + (ChatColor.GOLD + "" + ChatColor.BOLD + objects[0].toString()) + (ChatColor.YELLOW + " a quitté la partie."); // [ROUGE-«] Darkness6115 (ROUGE-27/ROUGE-30)


            case "uhc-command-start-already":
                return getErrorPrefix() + (ChatColor.RED + "La partie a déjà commencé");
            case "uhc-command-stop-nothing":
                return getErrorPrefix() + (ChatColor.RED + "La partie n'a pas encore commencé");
            case "uhc-command-stop":
                return getInfoPrefix() + ("Annulation du lancement de la partie.");
            case "uhc-command-start-teleport": // utilisation de "§" car c'est dans l'action bar
                return "§f" + "Téléportation des joueurs.";
            case "uhc-command-start-timer": // utilisation de "§" car c'est dans l'action bar
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return "§f" + "Démarrage de la partie dans " +
                        "§3" + objects[0].toString() + "s" +
                        "§f" + ".";



            case "cannot-interact-block":
                return getErrorPrefix() + (ChatColor.RED + "Vous ne pouvez pas interagir avec ce bloc.");
            case "cannot-interact-item":
                return getErrorPrefix() + (ChatColor.RED + "Vous ne pouvez pas interagir avec cette item.");



            case "scenarios-command-no-arg":
                return (getErrorPrefix() + "Il manque le nom du scenario.");
            case "scenarios-command-scenario-null":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return getErrorPrefix() + (ChatColor.WHITE + "Scénario ") + (ChatColor.AQUA + objects[0].toString()) + (ChatColor.WHITE + " introuvable ..?");
            case "scenarios-disabled":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return getInfoPrefix() + (ChatColor.WHITE + "Le scénario ") + (ChatColor.AQUA + "" + ChatColor.BOLD + objects[0].toString()) + (ChatColor.WHITE + " a été ") + (ChatColor.RED + "désactivé") + (ChatColor.WHITE + ".");
            case "scenarios-enabled":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
            return getInfoPrefix() + (ChatColor.WHITE + "Le scénario ") + (ChatColor.AQUA + "" + ChatColor.BOLD + objects[0].toString()) + (ChatColor.WHITE + " a été ") + (ChatColor.GREEN + "activé") + (ChatColor.WHITE + ".");

        }

        return (codename + "-not-found");
    }

    public final String gets(String codename) {
        return gets(codename, null);
    }

    // null player safe-method
    public final void sendS(Player player, String codename, Object[] objects) {
        if (player == null) {return;}
        player.sendMessage(gets(codename, objects));
    }

    public final void sendS(Player player, String codename) {
        if (player == null) {return;}
        player.sendMessage(gets(codename, null));
    }

    public final void sendM(Player player, String codename, Object[] objects) {
        if (player == null) {return;}
        player.sendMessage(getm(codename, objects));
    }

    public final void sendM(Player player, String codename) {
        if (player == null) {return;}
        player.sendMessage(getm(codename, null));
    }
}
