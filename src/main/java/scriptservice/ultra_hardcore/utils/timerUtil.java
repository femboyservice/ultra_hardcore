package scriptservice.ultra_hardcore.utils;

import org.bukkit.Sound;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.classes.states;
import scriptservice.ultra_hardcore.uhc;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class timerUtil extends initManager {
    public timerUtil(uhc plugin) {
        super(plugin);
    }

    // init
    private playerUtil playerUtil;
    private languageUtil languageUtil;
    private gameUtil gameUtil;

    @Override
    public void init(PluginManager pluginManager) {
        playerUtil = plugin.playerUtil;
        languageUtil = plugin.languageUtil;
        gameUtil = plugin.gameUtil;
    }

    // per-class vars
    public Timer startTimer;
    public TimerTask startTimerTask;

    // per-class methods
    public void uhcStartTimer() {
        plugin.getGameConfig().setGameState(states.START);

        startTimer = new Timer();
        startTimerTask = new TimerTask() {
            int finalCounter = 10;

            @Override
            public void run() {
                playerUtil.playSoundToAll(Sound.ORB_PICKUP);

                if (finalCounter > 0) {
                    playerUtil.sendActionTextToAll(languageUtil.gets("uhc-command-start-timer", new Object[]{finalCounter})); // "§fDémarrage de la partie dans §3" + finalCounter + "s§f."

                    finalCounter -= 1;
                } else {
                    // start teleport phase
                    gameUtil.startTELEPORT();

                    // set to null
                    uhcStopTimer(false, false);
                }

            }
        };

        startTimer.scheduleAtFixedRate(startTimerTask, Calendar.getInstance().getTime(), (1000));
    }

    public void uhcStopTimer(boolean setState, boolean sendMessage) {
        // cancel
        startTimer.cancel();
        startTimerTask.cancel();

        // null
        startTimer = null;
        startTimerTask = null;

        // set state
        if (setState) {
            plugin.getGameConfig().setGameState(states.WAIT);
        }

        // send message
        if (sendMessage) {
             playerUtil.sendMessageToAll(languageUtil.gets("uhc-command-stop"));
        }
    }
}
