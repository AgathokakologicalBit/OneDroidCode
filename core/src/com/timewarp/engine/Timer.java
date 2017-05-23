package com.timewarp.engine;

public final class Timer {
    private long startTime = 0;
    private long lastUpdateTime = 0;
    private boolean isStarted = false;
    private final boolean timerMode;
    private final boolean autoRepeatEnabled;

    private double targetTime = 0;
    private boolean isTimerActivated = false;

    public static final boolean MODE_TIMER = true;
    public static final boolean MODE_COUNTDOWN = false;

    public static final boolean AUTOREPEAT_ENABLED = true;
    public static final boolean AUTOREPEAT_DISABLED = false;

    public Timer(boolean timerMode) {
        this.timerMode = timerMode;
        this.autoRepeatEnabled = AUTOREPEAT_ENABLED;
    }

    public Timer(boolean timerMode, boolean autoRepeatEnabled, double repeatTime) {
        this.timerMode = timerMode;
        this.autoRepeatEnabled = autoRepeatEnabled;
        this.targetTime = repeatTime;
    }

    public void start() {
        this.isStarted = true;
        this.startTime = System.nanoTime();
        this.lastUpdateTime = this.startTime;
    }

    public void start(double countdownSeconds) {
        this.targetTime = countdownSeconds;
        this.start();
    }

    public double stop() {
        long passed = this.getTimeNano();
        this.startTime += passed;
        this.isStarted = false;

        return passed / 1000000000.0; // 1000000000 - count of nanoseconds in second
    }

    public double reset() {
        if (!this.isStarted) {
            this.start();
            return 0;
        }

        double passed = this.getTimeNano();
        this.startTime += passed;
        return passed;
    }

    /**
     * Returns how much time is passed since start. Or amount of time left on countdown
     * @return passed/remaining time
     */
    public double getTime() {
        // 1000000000 - count of nanoseconds in second
        double time = this.getTimeNano() / 1000000000.0;

        if (this.timerMode == MODE_COUNTDOWN) {
            double timeLeft = this.targetTime - time;
            if (timeLeft < 0) return 0;
            return timeLeft;
        }

        return time;
    }

    private long getTimeNano() {
        if (!this.isStarted) return 0;
        return this.lastUpdateTime - this.startTime;
    }

    /**
     * Checks if given time is passed
     * @return True if countdown is finished
     */
    public boolean isTimerActivated() {
        return this.isTimerActivated;
    }

    public void update() {
        if (!this.isStarted) return;

        this.lastUpdateTime = System.nanoTime();

        double time = this.getTime();

        if (this.timerMode == MODE_TIMER) this.isTimerActivated = time >= this.targetTime;
        else this.isTimerActivated = time <= 0;

        this.isTimerActivated &= this.targetTime != 0;

        if (this.isTimerActivated) {
            this.isStarted = this.autoRepeatEnabled;
            this.startTime = this.lastUpdateTime;
        }
    }
}
