package scriptservice.ultra_hardcore.classes;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

public class gameConfig {
    // pvp related
    @Getter @Setter private int critPercentage = 20;
    @Getter @Setter private int strengthPercentage = 50;
    @Getter @Setter private boolean thunderOnDeath = true;

    @Getter private final double strengthMultiplier = (((8.00/15.075) + (7/13.2) + (6.0/11.31) + (5.0/9.42) + (5.0/9.42)) / 5); // pour chaque épée = (normal/strength), tristement pas constant, donc je fait la moyenne (x ~= 0.5306...)
    @Getter private final double critMultipler = (1.5); // oui bon, j'ai la flemme de faire comme l'autre mais c'est la meme, et la c'est constant à: x = 1.5

    // inventory related
    @Getter private final Material strengthMaterial = Material.RED_ROSE;
    @Getter private final Material critMaterial = Material.GOLD_SWORD;

    // TODO :: CHANGE THIS TO PER-PLAYER !!
    // items related
    @Getter @Setter private boolean pearlEnabled = false;
    @Getter @Setter private boolean lavaEnabled = false;
    @Getter @Setter private boolean waterEnabled = true;
    @Getter @Setter private int maxArrows = 24;
    @Getter @Setter private int maxDiamondArmor = 3;

    // enchantements related
    @Getter @Setter private int diamondProtectionMax = 2;
    @Getter @Setter private int othersProtectionMax = 3;

    @Getter @Setter private int diamondSharpnessMax = 3;
    @Getter @Setter private int othersSharpnessMax = 3;

    @Getter @Setter private int powerMax = 2;

    @Getter @Setter private int fireAspectMax = 0;
    @Getter @Setter private int flameMax = 0;

    @Getter @Setter private int knockbackMax = 0;
    @Getter @Setter private int punchMax = 0;

    // game related
    @Getter @Setter private states gameState = states.WAIT;
    @Getter @Setter private states chatState = states.CHAT_ENABLED;

    @Getter @Setter private int gameEpisode = 0;
    @Getter @Setter private int gameGroups = 1;

    @Getter @Setter boolean cycle = false; // true = Jour, false = Nuit
}
