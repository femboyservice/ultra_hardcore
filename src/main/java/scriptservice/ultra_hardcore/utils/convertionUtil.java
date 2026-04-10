package scriptservice.ultra_hardcore.utils;

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
    public double millisecondToTick(double millisecond) {
        return secondToTick(millisecond / 1_000);
    }

    public double secondToTick(double second) {
        return (second * 20);
    }

    public double minuteToTick(double minute) {
        return secondToTick(minute * 60);
    }

    public double secondToMillisecond(double second) {
        return (second * 1_000);
    }

    public double millisecondToSecond(double millisecond) {
        return (millisecond / 1_000);
    }

    public double minuteToMillisecond(double minute) {
        return secondToMillisecond(minute * 60);
    }

}
