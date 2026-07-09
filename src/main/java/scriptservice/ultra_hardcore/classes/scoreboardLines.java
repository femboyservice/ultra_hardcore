package scriptservice.ultra_hardcore.classes;

import lombok.Getter;

public enum scoreboardLines {
    PLAYERS(2),
    GAME_TIME(3),
    GROUPS(4),
    KILLS(7),
    ASSISTS(8),
    BORDER(11),
    DATE(13),

    // en uhc classique ya pas hein
    CYCLE(999+1),
    EPISODE(999+2);


    @Getter private final int line;

    scoreboardLines(int line) {
        this.line = line;
    }
}
