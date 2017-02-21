package com.timewarp.engine;

import java.util.HashMap;

public class Time {
    private static HashMap<String, Timer> timers;

    public static void init() {
        Time.timers = new HashMap<String, Timer>();
    }

    public static void update() {
        for (Timer timer : Time.timers.values())
            timer.update();
    }

    public static int getFps() {
        return (int) (1 / Time.getDeltaTime());
    }

    public static float getDeltaTime() {
        return (float) Time.timers.get("delta_time").getTime();
    }

    public static float getRunTime() {
        return (float) Time.timers.get("running_time").getTime();
    }


    public static boolean addTimer(String name) {
        return Time.addTimer(name, new Timer(Timer.MODE_TIMER));
    }

    public static boolean addTimer(String name, Timer timer) {
        if (Time.timers.containsKey(name)) return false;
        if (timer == null) return false;

        Time.timers.put(name, timer);
        timer.start();
        return true;
    }

    public static boolean addCountdown(String name, double seconds) {
        return Time.addTimer(
                name,
                new Timer(
                        Timer.MODE_COUNTDOWN,
                        Timer.AUTOREPEAT_DISABLED,
                        seconds
                )
        );
    }

    public static boolean addCountdownRepeated(String name, double seconds) {
        return Time.addTimer(
                name,
                new Timer(
                        Timer.MODE_COUNTDOWN,
                        Timer.AUTOREPEAT_ENABLED,
                        seconds
                )
        );
    }

    public static boolean addTimer(String name, double seconds) {
        return Time.addTimer(
                name,
                new Timer(
                        Timer.MODE_TIMER,
                        Timer.AUTOREPEAT_DISABLED,
                        seconds
                )
        );
    }

    public static boolean addTimerRepeated(String name, double secods) {
        return Time.addTimer(
                name,
                new Timer(
                        Timer.MODE_TIMER,
                        Timer.AUTOREPEAT_ENABLED,
                        secods
                )
        );
    }

    public static boolean isTimerActivated(String name) {
        if (!Time.timers.containsKey(name)) return false;
        return Time.timers.get(name).isTimerActivated();
    }

    public static double getTime(String timerName) {
        if (!Time.timers.containsKey(timerName)) return 0;
        return Time.timers.get(timerName).getTime();
    }

    public static double stopTimer(String timerName) {
        if (!Time.timers.containsKey(timerName)) return 0;
        return Time.timers.get(timerName).stop();
    }

    public static double resetTimer(String timerName) {
        if (!Time.timers.containsKey(timerName)) return 0;
        return Time.timers.get(timerName).reset();
    }
}
