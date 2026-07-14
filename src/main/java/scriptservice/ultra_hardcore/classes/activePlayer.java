package scriptservice.ultra_hardcore.classes;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import scriptservice.ultra_hardcore.uhc;

import java.util.UUID;

public class activePlayer {
    // -- // LIMITS
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

    // statuses
    @Getter @Setter private boolean invincible = false;

    // -- // INFORMATIONS
    private final uhc plugin;

    @Getter @Setter private Player player;
    @Getter @Setter private UUID UUID;
    @Getter @Setter private boolean alive;
    @Getter @Setter private boolean connected;
    @Getter @Setter scoreboardSign scoreboard;

    @Getter private int kills = 0;
    @Getter private int assists = 0;

    // constructor
    public activePlayer(uhc plugin, UUID uuid) {
        // plugin
        this.plugin = plugin;

        // player info
        this.UUID = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.alive = true;
        this.connected = true;

        if (player == null) {
            System.out.println("[ultra_hardcore] couldn't get player from following uuid: " + uuid);
        }
    }

    // methods
    public void destroy() {
        setPlayer(null);
        setUUID(null);

        if (getScoreboard() != null) {
            getScoreboard().destroy();
        }

        setScoreboard(null);
    }

    public void update(Player newPlayer) {
        setPlayer(newPlayer);
        setUUID(newPlayer.getUniqueId());
    }

    public void addKill() {
       kills++;
    }

    public void addAssist() {
        assists++;
    }
}
