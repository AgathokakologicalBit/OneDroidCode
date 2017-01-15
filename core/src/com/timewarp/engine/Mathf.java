package com.timewarp.engine;

public class Mathf {

    /**
     * Indicate range between min(exclusive) and max(exclusive)
     */
    public static final byte RANGE_EXCLUSIVE = 0; // 00
    /**
     * Indicate range between min(inclusive) and max(inclusive)
     */
    public static final byte RANGE_INCLUSIVE = 3; // 11
    /**
     * Indicate range between min(exclusive) and max(inclusive)
     */
    public static final byte RANGE_MAX_INCLUSIVE = 1; // 01
    /**
     * Indicate range between min(inclusive) and max(exclusive)
     */
    public static final byte RANGE_MIN_INCLUSIVE = 2; // 10


    /**
     * Value of PI with `float` precision
     */
    public static final float PI = (float) Math.PI;


    /**
     * Returns clipped value, that is between min(inclusive) and max(inclusive)
     *
     * @param value   Value to clamp
     * @param range_a threshold value
     * @param range_b threshold value
     * @return clipped value
     */
    public static int clamp(int value, int range_a, int range_b) {
        if (range_a > range_b) {
            int tmp = range_a;
            range_a = range_b;
            range_b = tmp;
        }

        if (value > range_b) return range_b;
        if (value < range_a) return range_a;
        return value;
    }

    /**
     * Returns clipped value, that is between min(inclusive) and max(inclusive)
     *
     * @param value   Value to clamp
     * @param range_a threshold value
     * @param range_b threshold value
     * @return clipped value
     */
    public static float clamp(float value, float range_a, float range_b) {
        if (range_a > range_b) {
            float tmp = range_a;
            range_a = range_b;
            range_b = tmp;
        }

        if (value > range_b) return range_b;
        if (value < range_a) return range_a;
        return value;
    }

    /**
     * Returns clipped value, that is between min(inclusive) and max(inclusive)
     *
     * @param value   Value to clamp
     * @param range_a threshold value
     * @param range_b threshold value
     * @return clipped value
     */
    public static double clamp(double value, double range_a, double range_b) {
        if (range_a > range_b) {
            double tmp = range_a;
            range_a = range_b;
            range_b = tmp;
        }

        if (value > range_b) return range_b;
        if (value < range_a) return range_a;
        return value;
    }


    /**
     * Linear interpolation between `from` and `to`
     * Parameter `t` from 0(inclusive) to 1(inclusive) is interpolation value
     * t(0) = `from`, t(1) = `to`
     * Warning: If t is less than 0 or greater than 1 function will not clamp result
     *
     * @param from Star value
     * @param to   End value
     * @param t    Interpolation value
     * @return Interpolated value
     */
    public static float lerp(float from, float to, float t) {
        return from + (to - from) * t;
    }

    /**
     * Linear interpolation between `from` and `to`
     * Parameter `t` from 0(inclusive) to 1(inclusive) is interpolation value
     * t(0) = `from`, t(1) = `to`
     * Warning: If t is less than 0 or greater than 1 function will not clamp result
     *
     * @param from Star value
     * @param to   End value
     * @param t    Interpolation value
     * @return Interpolated value
     */
    public static double lerp(double from, double to, double t) {
        return from + (to - from) * t;
    }

    /**
     * Linear interpolation between `from` and `to`
     * Parameter `t` from 0(inclusive) to 1(inclusive) is interpolation value
     * t(0) = `from`, t(1) = `to`
     * Warning: If t is less than 0 or greater than 1 function will not clamp result
     *
     * @param from Star value
     * @param to   End value
     * @param t    Interpolation value
     * @return Interpolated vector
     */
    public static Vector2D lerp(Vector2D from, Vector2D to, float t) {
        return from.add(to.sub(from).mult(t));
    }


    /**
     * Checks if value is in given range, using given comparison mode
     *
     * @param value     Value to check
     * @param range_a   threshold value
     * @param range_b   threshold value
     * @param checkMode Range threshold values
     * @return is value is in range
     */
    public static boolean inRange(long value, long range_a, long range_b, byte checkMode) {
        if (range_a > range_b) {
            long tmp = range_a;
            range_a = range_b;
            range_b = tmp;
        }

        if (value == range_a && (checkMode & RANGE_MIN_INCLUSIVE) == 0) return false;
        if (value == range_b && (checkMode & RANGE_MAX_INCLUSIVE) == 0) return false;

        if (value < range_a) return false;
        if (value > range_b) return false;

        return true;
    }

    /**
     * Checks if value is in given range, using given comparison mode
     *
     * @param value     Value to check
     * @param range_a   threshold value
     * @param range_b   threshold value
     * @param checkMode Range threshold values
     * @return is value is in range
     */
    public static boolean inRange(double value, double range_a, double range_b, byte checkMode) {
        if (range_a > range_b) {
            double tmp = range_a;
            range_a = range_b;
            range_b = tmp;
        }

        if (value == range_a && (checkMode & RANGE_MIN_INCLUSIVE) == 0) return false;
        if (value == range_b && (checkMode & RANGE_MAX_INCLUSIVE) == 0) return false;

        if (value < range_a) return false;
        if (value > range_b) return false;

        return true;
    }

    /**
     * Checks if value is in given range(all inclusive)
     *
     * @param value   Value to check
     * @param range_a threshold value
     * @param range_b threshold value
     * @return is value is in range
     */
    public static boolean inRange(long value, long range_a, long range_b) {
        return inRange(value, range_a, range_b, RANGE_INCLUSIVE);
    }

    /**
     * Checks if value is in given range(all inclusive)
     *
     * @param value   Value to check
     * @param range_a threshold value
     * @param range_b threshold value
     * @return is value is in range
     */
    public static boolean inRange(double value, double range_a, double range_b) {
        return inRange(value, range_a, range_b, RANGE_INCLUSIVE);
    }


    /**
     * Checks if point is in rectangle
     * Includes borders
     *
     * @param point         Point position
     * @param rect_position Rectangle position
     * @param rect_size     Rectangle size
     * @return Is point is in Rectangle
     */
    public static boolean inRectangle(Vector2D point, Vector2D rect_position, Vector2D rect_size) {
        if (point.x < rect_position.x) return false;
        if (point.y < rect_position.y) return false;

        if (point.x > rect_position.x + rect_size.x) return false;
        if (point.y > rect_position.y + rect_size.y) return false;

        return true;

    }
}