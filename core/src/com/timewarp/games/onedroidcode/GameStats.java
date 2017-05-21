package com.timewarp.games.onedroidcode;

public final class GameStats {

    public static int score;

    public static void setScore(int score) {
        GameStats.score = score;
    }

    public static int getScore() {
        return GameStats.score;
    }

    public static void addScore(int score) {
        GameStats.score += score;
    }

    public static void reset() {
        score = 0;
    }
}
