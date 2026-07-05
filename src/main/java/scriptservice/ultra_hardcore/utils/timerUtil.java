package scriptservice.ultra_hardcore.utils;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.activePlayer;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.classes.scoreboardLines;
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
    private gameUtil gameUtil;

    @Override
    public void init(PluginManager pluginManager) {
        gameUtil = plugin.gameUtil;
    }

    // per-class vars
    private Timer countdownTimer;
    private TimerTask countdownTimerTask;

    private Timer ingameTimer;
    private TimerTask ingameTimerTask;

    private Timer episodeTimer;
    private TimerTask episodeTimerTask;

    private Timer cycleTimer;
    private TimerTask cycleTimerTask;

    private int gametimeInt = -1;

    final public double timeForEpisode = convertionUtil.secondToMillisecond(80); // 1m20s
    final public double timeForCycle = (timeForEpisode / 4); // 2x (5mns Jour / 5mns Nuit)

    // -- //  per-class methods
    // countdown
    public void startCountdown(final int countdown) {
        plugin.getGameConfig().setGameState(states.START);

        countdownTimer = new Timer();
        countdownTimerTask = new TimerTask() {
            int finalCounter = countdown;

            @Override
            public void run() {
                if (finalCounter > 0) {
                    // play sound
                    playerUtil.playSoundToAll(Sound.ORB_PICKUP);

                    // send message
                    playerUtil.sendActionTextToAll(languageUtil.gets("uhc-command-start-timer", new Object[]{finalCounter})); // "§fDémarrage de la partie dans §3" + finalCounter + "s§f."

                    // update
                    finalCounter -= 1;
                } else {
                    // play sound
                    playerUtil.playSoundToAll(Sound.CHICKEN_EGG_POP);

                    // start teleport phase
                    gameUtil.startTELEPORT();

                    // set to null
                    stopCountdown(false);
                }

            }
        };

        countdownTimer.scheduleAtFixedRate(countdownTimerTask, Calendar.getInstance().getTime(), (long) convertionUtil.secondToMillisecond(1));
    }

    public void stopCountdown(boolean sendMessage) {
        // cancel and set to null countdownTimer && countdownTimerTask
        if (countdownTimer != null) {
            countdownTimer.cancel();
            countdownTimer = null;
        }

        if (countdownTimerTask != null) {
            countdownTimerTask.cancel();
            countdownTimerTask = null;
        }

        // send message
        if (sendMessage) {
             playerUtil.sendMessageToAll(languageUtil.gets("uhc-command-stop"));
        }
    }

    // in-game
    public void startInGame() {
        // reset int
        gametimeInt = -1;

        // start new timer
        ingameTimer = new Timer();
        ingameTimerTask = new TimerTask() {
            @Override
            public void run() {
                gametimeInt = gametimeInt + 1;
                // update ingame time
                for (activePlayer activePlayer: gameUtil.getActivePlayers()) {
                    gameUtil.updateScoreboard(activePlayer, scoreboardLines.GAME_TIME, convertionUtil.IntegerToTime(gametimeInt));
                    gameUtil.updateScoreboard(activePlayer, scoreboardLines.BORDER, gameUtil.getWorldBorderSize());
                    // gameUtils.update("ingame-timer", new String[]{Integer.toString(gametimeInt)});
                }


            }
        };

        ingameTimer.scheduleAtFixedRate(ingameTimerTask, Calendar.getInstance().getTime(), (long) convertionUtil.secondToMillisecond(1));
    }

    public void stopInGame() {
        if (ingameTimer != null) {
            ingameTimer.cancel();
            ingameTimer = null;
        }

        if (ingameTimerTask != null) {
            ingameTimerTask.cancel();
            ingameTimerTask = null;
        }
    }

    // cycle
    public void startCycle() {
        // reset cycle
        plugin.getGameConfig().setCycle(false);

        // start new timer
        cycleTimer = new Timer();
        cycleTimerTask = new TimerTask() {
            @Override
            public void run() {
                plugin.getGameConfig().setCycle(!plugin.getGameConfig().isCycle());

                // update cycle
                for (activePlayer activePlayer: gameUtil.getActivePlayers()) {
                    gameUtil.updateScoreboard(activePlayer, scoreboardLines.CYCLE, (plugin.getGameConfig().isCycle() ? "Jour" : "Nuit"));
                }

                if (plugin.getGameConfig().isCycle()) {
                    playerUtil.sendMessageToAll(languageUtil.gets("uhc-cycle-day"));
                } else {
                    playerUtil.sendMessageToAll(languageUtil.gets("uhc-cycle-night"));
                }
            }
        };

        cycleTimer.scheduleAtFixedRate(cycleTimerTask, Calendar.getInstance().getTime(), (long) timeForCycle);
    }

    public void stopCycle() {
        if (cycleTimer != null) {
            cycleTimer.cancel();
            cycleTimer = null;
        }

        if (cycleTimerTask != null) {
            cycleTimerTask.cancel();
            cycleTimerTask = null;
        }
    }

    // episode
    public void startEpisode() {
        // reset ints
        plugin.getGameConfig().setGameEpisode(0);

        // start new timer
        episodeTimer = new Timer();
        episodeTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (plugin.getGameConfig().getGameEpisode() != 0) {
                    playerUtil.sendMessageToAll(ChatColor.AQUA + "---- Fin de l'épisode " + plugin.getGameConfig().getGameEpisode() + " ----");
                }

                // update episode
                plugin.getGameConfig().setGameEpisode(plugin.getGameConfig().getGameEpisode() + 1);
                for (activePlayer activePlayer: gameUtil.getActivePlayers()) {
                    gameUtil.updateScoreboard(activePlayer, scoreboardLines.EPISODE, plugin.getGameConfig().getGameEpisode());
                }

            }
        };

        episodeTimer.scheduleAtFixedRate(episodeTimerTask, Calendar.getInstance().getTime(), (long) timeForEpisode);
    }

    public void stopEpisode() {
        if (episodeTimer != null) {
            episodeTimer.cancel();
            episodeTimer = null;
        }

        if (episodeTimerTask != null) {
            episodeTimerTask.cancel();
            episodeTimerTask = null;
        }
    }

    public double getTimeLeftBeforeNextEpisode() {
        return (
                (       plugin.getGameConfig().getGameEpisode() // take current episode
                        * convertionUtil.millisecondToSecond(timeForEpisode) // multiply it by the time it takes (in seconds) for the episode
                ) // take that and
                        - gametimeInt // substract the current int timer
        ); // to give the time (in seconds) left before the new episode
    }
}
