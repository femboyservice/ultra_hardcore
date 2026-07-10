package scriptservice.ultra_hardcore.classes;

import lombok.Getter;

public enum scoreboardLines {
    PLAYERS(2, "Joueurs"),
    GAME_TIME(3, "Durée"),
    GROUPS(4, "Groupes"),
    KILLS(7, "Kills"),
    ASSISTS(8, "Assists"),
    BORDER(11, "Taille"),
    DATE(13, ""),

    // en uhc classique ya pas hein
    CYCLE(999+1, "Cycle"),
    EPISODE(999+2, "Épisode");


    @Getter private final int line;
    @Getter private final String prefix;

    scoreboardLines(int line, String prefix) {
        this.line = line;
        this.prefix = prefix;
    }
}
