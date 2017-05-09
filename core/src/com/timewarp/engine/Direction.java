package com.timewarp.engine;


import com.timewarp.engine.Math.Mathf;

public enum Direction {
    EMPTY(-1),

    UP(0),
    UP_RIGHT(1),
    RIGHT(2),
    DOWN_RIGHT(3),
    DOWN(4),
    DOWN_LEFT(5),
    LEFT(6),
    UP_LEFT(7);


    private static final Direction[] directions = values();
    protected int code;

    Direction(int code) {
        this.code = code;
    }

    public int toInteger() {
        return this.code;
    }

    public Direction rotatedBy(Direction dir) {
        if (dir == EMPTY) return this;
        return directions[(this.code + dir.code) % 8 + 1];
    }

    public Vector2D getVector() {
        switch (this.code) {
            case 0:
                return new Vector2D(0, -1);
            case 1:
                return new Vector2D(1, -1);
            case 2:
                return new Vector2D(1, 0);
            case 3:
                return new Vector2D(1, 1);
            case 4:
                return new Vector2D(0, 1);
            case 5:
                return new Vector2D(-1, 1);
            case 6:
                return new Vector2D(-1, 0);
            case 7:
                return new Vector2D(-1, -1);

            default:
                return new Vector2D(0, 0);
        }
    }

    @Override
    public String toString() {
        switch (this.code) {
            case -1:
                return "none";

            case 0:
                return "up";
            case 1:
                return "up_right";
            case 2:
                return "right";
            case 3:
                return "down_right";
            case 4:
                return "down";
            case 5:
                return "down_left";
            case 6:
                return "left";
            case 7:
                return "up_left";
        }

        return "undefined";
    }

    public static Direction fromInteger(int dir) {
        return directions[(8 + dir % 8) % 8 + 1];
    }

    public static Direction fromVector(int x, int y) {
        if (x == 0 && y == 0) return Direction.EMPTY;

        if (x < 0 && y < 0) return Direction.UP_LEFT;
        if (x == 0 && y < 0) return Direction.UP;
        if (x > 0 && y < 0) return Direction.UP_RIGHT;
        if (x > 0 && y == 0) return Direction.RIGHT;
        if (x > 0 && y > 0) return Direction.DOWN_RIGHT;
        if (x == 0 && y > 0) return Direction.DOWN;
        if (x < 0 && y > 0) return Direction.DOWN_LEFT;
        if (x < 0 && y == 0) return Direction.LEFT;

        return Direction.EMPTY;
    }

    public float toDegrees() {
        if (this.code < 0) return 0;
        if (this.code > 8) return 0;

        return -this.code * 45;
    }

    public float toRadians() {
        return this.toDegrees() / 180 * Mathf.PI;
    }
}
