package scriptservice.ultra_hardcore.classes;

import lombok.Getter;

public enum scoreboardLines {
    DATE(1),
    PLAYERS(4),
    GAME_TIME(5),
    GROUPS(6),
    CYCLE(7),
    BORDER(9),
    EPISODE(10),
    KILLS(11),
    ASSISTS(12);

    @Getter private final int line;

    scoreboardLines(int line) {
        this.line = line;
    }
}
