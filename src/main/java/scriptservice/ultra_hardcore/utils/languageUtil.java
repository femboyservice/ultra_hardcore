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
    private final static String resetEscape = (ChatColor.RESET + "\n");
    @Getter private final static String infoPrefix = ((ChatColor.DARK_GRAY + "{") + (ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "ultra_hardcore") + (ChatColor.DARK_GRAY + "} ")) + ChatColor.RESET;
    @Getter private final static String validPrefix = ((ChatColor.DARK_GRAY + "{") + (ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "ultra_hardcore") + (ChatColor.DARK_GRAY + "} ")) + ChatColor.RESET;
    @Getter private final static String errorPrefix = ((ChatColor.DARK_GRAY + "{") + (ChatColor.RED + "" + ChatColor.BOLD + "ultra_hardcore") + (ChatColor.DARK_GRAY + "} ")) + ChatColor.RESET;

    // init
    @Override
    public void init(PluginManager pluginManager) {}

    // per-class methods ⓤⓘ
    public static String[] getm(String codename, Object[] objects) {
        // return new String[]{};
        switch (codename) {
            case "no-perms":
            case "noperms":
            case "nopermission":
            case "no-permission":
                return new String[]{
                        gets("no-permission")
                };

            case "uhc-command-help":
                return new String[]{
                        getInfoPrefix() + (ChatColor.WHITE + "Toutes les commandes disponibles de ") + (ChatColor.DARK_AQUA + "/") + (ChatColor.AQUA + "uhc") + (ChatColor.DARK_AQUA + " ..."),
                        (ChatColor.DARK_GREEN + "   > ") + (ChatColor.WHITE + "" + ChatColor.ITALIC + "help"),
                        (ChatColor.GREEN + "   > ") + (ChatColor.WHITE + "start"),
                        (ChatColor.GREEN + "   > ") + (ChatColor.WHITE + "stop"),
                        (ChatColor.GREEN + "   > ") + (ChatColor.WHITE + "settings"),
                        (ChatColor.GREEN + "   > ") + (ChatColor.WHITE + "setgroup")
                };
            case "scenarios-command-help":
                return new String[]{
                        getInfoPrefix() + (ChatColor.WHITE + "Toutes les commandes disponibles de ") + (ChatColor.DARK_AQUA + "/") + (ChatColor.AQUA + "scenarios") + (ChatColor.DARK_AQUA + " ..."),
                        (ChatColor.DARK_GREEN + "   > ") + (ChatColor.WHITE + "" + ChatColor.ITALIC + "null"),
                        (ChatColor.GREEN + "    > ") + (ChatColor.WHITE + "help")
                };
            case "uhc-command-help-help":
                return new String[]{
                        getInfoPrefix() + (ChatColor.WHITE + "Description de la commande") + (ChatColor.DARK_GRAY + " » ") + (ChatColor.DARK_AQUA + "/uhc ") + (ChatColor.AQUA + "help"),
                        (ChatColor.DARK_GRAY + "[") + (ChatColor.AQUA+"ⓘ") + (ChatColor.DARK_GRAY + "]") + (ChatColor.WHITE + " Vraiment besoin d'aide sur comment avoir de l'aide ..?"),
                        (ChatColor.DARK_GRAY + "[") + (ChatColor.AQUA+"ⓤ") + (ChatColor.DARK_GRAY + "]") + (ChatColor.DARK_AQUA + " /uhc help ") + (ChatColor.AQUA+"<command>")
                };
            case "uhc-command-start-help":
                return new String[]{
                        getInfoPrefix() + (ChatColor.WHITE + "Description de la commande") + (ChatColor.DARK_GRAY + " » ") + (ChatColor.DARK_AQUA + "/uhc ") + (ChatColor.AQUA + "start"),
                        (ChatColor.DARK_GRAY + "[") + (ChatColor.AQUA+"ⓘ") + (ChatColor.DARK_GRAY + "]") + (ChatColor.WHITE + " Permet de commencer la partie."),
                        (ChatColor.DARK_GRAY + "[") + (ChatColor.AQUA+"ⓤ") + (ChatColor.DARK_GRAY + "]") + (ChatColor.DARK_AQUA + " /uhc ") + (ChatColor.AQUA+"start")
                };
            case "uhc-command-stop-help":
                return new String[]{
                        getInfoPrefix() + (ChatColor.WHITE + "Description de la commande") + (ChatColor.DARK_GRAY + " » ") + (ChatColor.DARK_AQUA + "/uhc ") + (ChatColor.AQUA + "stop"),
                        (ChatColor.DARK_GRAY + "[") + (ChatColor.AQUA+"ⓘ") + (ChatColor.DARK_GRAY + "]") + (ChatColor.WHITE + " Permet d'arrêter la partie en cours."),
                        (ChatColor.DARK_GRAY + "[") + (ChatColor.AQUA+"ⓤ") + (ChatColor.DARK_GRAY + "]") + (ChatColor.DARK_AQUA + " /uhc ") + (ChatColor.AQUA+"stop")
                };
            case "uhc-command-setgroup-help":
                return new String[]{
                        getInfoPrefix() + (ChatColor.WHITE + "Description de la commande") + (ChatColor.DARK_GRAY + " » ") + (ChatColor.DARK_AQUA + "/uhc ") + (ChatColor.AQUA + "setgroup"),
                        (ChatColor.DARK_GRAY + "[") + (ChatColor.AQUA+"ⓘ") + (ChatColor.DARK_GRAY + "]") + (ChatColor.WHITE + " Permet de changer la limite de groupe."),
                        (ChatColor.DARK_GRAY + "[") + (ChatColor.AQUA+"ⓤ") + (ChatColor.DARK_GRAY + "]") + (ChatColor.DARK_AQUA + " /uhc setgroup ") + (ChatColor.AQUA+"<nombre>")
                };
        }

        return new String[]{codename + "-not-found-multiple"};
    }

    public static String[] getm(String codename) {
        return getm(codename, null);
    }

    public static String gets(String codename, Object[] objects) {
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
            case "general-command-introuvable":
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



            case "uhc-command-setgroup-new-group":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return getInfoPrefix() + (ChatColor.WHITE + "Nouvelle limite de groupe mise à ") + (ChatColor.DARK_GREEN+objects[0].toString()) + (ChatColor.WHITE + ".");
            case "uhc-command-setgroup-game-not-started":
                return getErrorPrefix() + (ChatColor.RED + "Vous ne pouvez pas changer les groupes tant que la partie n'a pas encore commencé.");

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
                        "§2" + objects[0].toString() + "s" +
                        "§f" + ".";



            case "global-command-arg-not-integer":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return getErrorPrefix() + (ChatColor.AQUA + objects[0].toString() + ChatColor.RED + " n'est pas un nombre valide.");



            case "cannot-interact-block":
                return getErrorPrefix() + (ChatColor.RED + "Vous ne pouvez pas interagir avec ce bloc.");
            case "cannot-interact-item":
                return getErrorPrefix() + (ChatColor.RED + "Vous ne pouvez pas interagir avec cette item.");
            case "cannot-pickup-item":
                return getErrorPrefix() + (ChatColor.RED + "Vous ne pouvez pas ramasser cette item.");
            case "cannot-craft-item":
                return getErrorPrefix() + (ChatColor.RED + "Vous ne pouvez pas crafter cette item.");
            case "cannot-use-item":
                return getErrorPrefix() + (ChatColor.RED + "Vous ne pouvez pas utiliser cette item.");



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



            case "uhc-cycle-day":
                return (
                        (ChatColor.GOLD+"☀"+ChatColor.YELLOW+" LE JOUR SE LEVE "+ChatColor.GOLD+"☀")
                );
            case "uhc-cycle-night":
                return (
                        (ChatColor.DARK_BLUE+"☾"+ChatColor.BLUE+" LA NUIT TOMBE "+ChatColor.DARK_BLUE+"☽")
                );



            case "uhc-end-title":
                return (
                        "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "FIN DE LA PARTIE"
                        );
            case "uhc-end-subtitle":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return (
                        (ChatColor.WHITE + "Victoire de ") + (ChatColor.GREEN + objects[0].toString()) + (ChatColor.WHITE + ".")
                );
            case "uhc-end-closing":
                return (
                        getValidPrefix() + (ChatColor.WHITE + "Fermeture de la partie dans ") + (ChatColor.DARK_GREEN + "1 minute") + (ChatColor.WHITE + ".")
                        );



            case "uhc-chat-enabled":
                return (ChatColor.GREEN+"Le chat est activé."); // alors, faudra me dire quand je l'utiliserai mais je vois pas trop ^^'
            case "uhc-chat-disabled":
                return (ChatColor.RED+"Le chat est désactivé.");
            case "uhc-chat-now-enabled":
                return (ChatColor.GREEN+"Le chat est de nouveau activé.");
            case "uhc-chat-now-disabled":
                return (ChatColor.RED+"Le chat a été désactivé.");



            case "uhc-invincibility-start":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return getInfoPrefix() + (ChatColor.WHITE + "Vous devenez invulnérable pendant ") + (ChatColor.GREEN + objects[0].toString() + " secondes") + (ChatColor.WHITE+".");
            case "uhc-invincibility-end":
                return getInfoPrefix() + (ChatColor.WHITE+"Vous redevenez vulnérables aux dégâts.");


            case "uhc-teleport":
                if (objects.length != 1) {return (codename+"-not-enough-args");}
                return getInfoPrefix() + (ChatColor.WHITE + "Téléportation de ") + (ChatColor.GREEN + objects[0].toString()) + (ChatColor.WHITE + ".");
        }

        return (codename + "-not-found-single");
    }

    public static String gets(String codename) {
        return gets(codename, null);
    }

    // null player safe-method
    public static void sendS(Player player, String codename, Object[] objects) {
        if (player == null) {return;}
        player.sendMessage(gets(codename, objects));
    }

    public static void sendS(Player player, String codename) {
        if (player == null) {return;}
        player.sendMessage(gets(codename, null));
    }
}
