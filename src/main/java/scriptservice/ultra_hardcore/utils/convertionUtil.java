package scriptservice.ultra_hardcore.utils;

import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;

public class convertionUtil extends initManager {
    public convertionUtil(uhc plugin) {
        super(plugin);
    }

    // init
    @Override
    public void init(PluginManager pluginManager) {}

    // per-class methods
    public static double millisecondToTick(double millisecond) {
        return secondToTick(millisecond / 1_000);
    }

    public static double secondToTick(double second) {
        return (second * 20);
    }

    public static long secondToTick(int second) {
        return (second * 20L);
    }

    public static double minuteToTick(double minute) {
        return secondToTick(minute * 60);
    }

    public static double secondToMillisecond(double second) {
        return (second * 1_000);
    }

    public static double millisecondToSecond(double millisecond) {
        return (millisecond / 1_000);
    }

    public static double minuteToMillisecond(double minute) {
        return secondToMillisecond(minute * 60);
    }

    public static String IntegerToTime(int time) {
        // oui, c'est terriblement pas opti, mais j'en suis très fier

        final int hrsInt = (time / 3600);
        final int mnsInt = ((time / 60) - (hrsInt * 60));
        final int secInt = (time % 60);

        final String sec = String.valueOf(String.valueOf(secInt).length() == 1 ? "0" + secInt : secInt);
        final String mns = String.valueOf(String.valueOf(mnsInt).length() == 1 ? "0" + mnsInt : mnsInt);

        return (
                (hrsInt) == 0 ? ((mnsInt) == 0 ? (secInt) + "s" : (mnsInt) + "m" + (sec) + "s") : (hrsInt) + "h" + (mns) + "m" + (sec) + "s"
        );
    }

    public static double distance2D(Location a, Location b) {
        double dx = a.getX() - b.getX();
        double dz = a.getZ() - b.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }
}
