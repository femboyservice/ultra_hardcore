package scriptservice.ultra_hardcore.classes;

import lombok.Getter;

public enum scoreboardLines {
    PLAYERS(2, "Joueurs"),
    GROUPS(3, "Groupes"),
    GAME_TIME(4, "Durée"),
    KILLS(7, "Kills"),
    ASSISTS(8, "Assists"),
    BORDER(11, "Taille"),
    BORDER_DISTANCE(12, "Distance"),

    // en uhc classique ya pas hein
    DATE(999+3, "Date"),
    CYCLE(999+2, "Cycle"),
    EPISODE(999+1, "Épisode");


    @Getter private final int line;
    @Getter private final String prefix;

    scoreboardLines(int line, String prefix) {
        this.line = line;
        this.prefix = prefix;
    }
}
