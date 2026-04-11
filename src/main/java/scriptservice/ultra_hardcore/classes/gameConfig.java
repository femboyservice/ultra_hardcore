package scriptservice.ultra_hardcore.classes;

import lombok.Getter;
import lombok.Setter;

public class gameConfig {
    // pvp related
    @Getter @Setter private int critPercentage = 20;
    @Getter @Setter private boolean thunderOnDeath = true;

    // items related
    @Getter @Setter private boolean pearlEnabled = false;
    @Getter @Setter private boolean lavaEnabled = false;
    @Getter @Setter private boolean waterEnabled = true;
    @Getter @Setter private int maxArrow = 64;

    // enchantements related
    @Getter @Setter private int diamondProtectionMax = 2;
    @Getter @Setter private int ironProtectionMax = 3;

    @Getter @Setter private int diamondSharpnessMax = 3;
    @Getter @Setter private int ironSharpnessMax = 4;

    @Getter @Setter private int powerMax = 2;
}
