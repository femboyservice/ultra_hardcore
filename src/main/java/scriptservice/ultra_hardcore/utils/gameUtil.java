package scriptservice.ultra_hardcore.utils;

import org.bukkit.Sound;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.classes.states;
import scriptservice.ultra_hardcore.uhc;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class gameUtil extends initManager {
    public gameUtil(uhc plugin) {
        super(plugin);
    }

    // init
    private timerUtil timerUtil;
    private playerUtil playerUtil;
    private languageUtil languageUtil;

    @Override
    public void init(PluginManager pluginManager) {
        timerUtil = plugin.timerUtil;
        playerUtil = plugin.playerUtil;
        languageUtil = plugin.languageUtil;
    }

    // per-class vars


    // per-class methods
    // starts
    public void startSTART() {
        // set state
        plugin.getGameConfig().setGameState(states.START);

        // start timer to tp
        timerUtil.uhcStartTimer();
    }

    public void startTELEPORT() {
        // set state
        plugin.getGameConfig().setGameState(states.TELEPORT);

        // message
        playerUtil.sendActionTextToAll(languageUtil.gets("uhc-command-start-teleport")); // "§fTéléportation des joueurs."

        // teleport players
        // TODO

        // start PREGAME
        startPREGAME();
    }

    public void startPREGAME() {
        // TODO
    }

    public void startGAME() {
        // TODO
    }

    public void startEND() {
        // TODO
    }

    // stops
    public void stopSTART() {
        // set state
        plugin.getGameConfig().setGameState(states.WAIT);


        // stop timers
        timerUtil.uhcStopTimer(true, true);
    }
}
