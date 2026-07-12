package scriptservice.ultra_hardcore.classes;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import scriptservice.ultra_hardcore.utils.convertionUtil;

public class gameConfig {
    // pvp related
    @Getter @Setter private int critPercentage = 20;
    @Getter @Setter private int strengthPercentage = 50;
    @Getter @Setter private boolean thunderOnDeath = true;

    @Getter private final double strengthMultiplier = (((8.00/15.075) + (7/13.2) + (6.0/11.31) + (5.0/9.42) + (5.0/9.42)) / 5); // pour chaque épée = (normal/strength), tristement pas constant, donc je fait la moyenne (x ~= 0.5306...)
    @Getter private final double critMultipler = (1.5); // oui bon, j'ai la flemme de faire comme l'autre mais c'est la meme, et la c'est constant à: x = 1.5

    @Getter @Setter private int assistDelayWindow = (int) convertionUtil.secondToMillisecond(20);

    // inventory related
    @Getter private final Material strengthMaterial = Material.RED_ROSE;
    @Getter private final Material critMaterial = Material.GOLD_SWORD;

    // game related
    @Getter @Setter private states gameState = states.WAIT;
    @Getter @Setter private states chatState = states.CHAT_ENABLED;
    @Getter @Setter private states cycle = states.NIGHT;

    @Getter @Setter private double borderSize = 200;

    @Getter @Setter private int gameEpisode = 0;
    @Getter @Setter private int gameGroups = 1;
    @Getter @Setter private boolean sendEpisodeMessage = false;
    @Getter @Setter private boolean sendCycleMessage = false;
}
