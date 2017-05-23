package com.timewarp.games.onedroidcode;

public final class GameStats {

    public static int score;
    public static int steps;
    public static int maxSteps;
    public static int instructions;

    public static void reset() {
        score = 0;
        steps = 0;
        maxSteps = 0;
        instructions = 0;
    }
}
