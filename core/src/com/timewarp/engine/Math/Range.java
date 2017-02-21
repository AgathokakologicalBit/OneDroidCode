package com.timewarp.engine.Math;


public class Range<T extends Number & Comparable<? super T>> {

    private enum ThresholdType {
        Inclusive,
        Exclusive,
        Infinite
    }

    public static final ThresholdType infinity = ThresholdType.Infinite;
    public static final ThresholdType exclusive = ThresholdType.Exclusive;
    public static final ThresholdType inclusive = ThresholdType.Inclusive;

    private static class Threshold<T extends Number & Comparable<? super T>> {
        private ThresholdType type;
        private T value;

        Threshold(ThresholdType type) {
            this.type = type;
        }

        Threshold(ThresholdType type, T value) {
            this.type = type;
            this.value = value;
        }

        boolean containsAfter(T number) {
            switch (this.type) {
                case Exclusive:
                    return number.compareTo(this.value) > 0;
                case Inclusive:
                    return number.compareTo(this.value) >= 0;
                case Infinite:
                    return true;
            }

            return false;
        }

        boolean containsBefore(T number) {
            switch (this.type) {
                case Exclusive:
                    return number.compareTo(this.value) < 0;
                case Inclusive:
                    return number.compareTo(this.value) <= 0;
                case Infinite:
                    return true;
            }

            return false;
        }
    }

    private Threshold<T> from;
    private Threshold<T> to;

    /**
     * Creates new Range (from; to)(infinity; infinity)
     */
    public Range() {
        this.from = new Threshold<T>(infinity);
        this.to = new Threshold<T>(infinity);
    }

    private Range(Threshold<T> from, Threshold<T> to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Checks whether given point(number) is in specified range
     * @param value Target point
     * @return Is range contains give point
     */
    public boolean contains(T value) {
        return this.from.containsAfter(value) && this.to.containsBefore(value);
    }

    /**
     * Sets left boundary of range
     * @param thresholdType Threshold type
     * @return Range, starting from given point
     */
    public Range<T> from(ThresholdType thresholdType) {
        return new Range<T>(new Threshold<T>(thresholdType), this.to);
    }

    /**
     * Sets left boundary of range
     * @param thresholdType Threshold type
     * @param value Threshold value
     * @return Range, starting from given point
     */
    public Range<T> from(ThresholdType thresholdType, T value) {
        return new Range<T>(new Threshold<T>(thresholdType, value), this.to);
    }

    /**
     * Sets right boundary of range
     * @param thresholdType Threshold type
     * @return Range, ending on given point
     */
    public Range<T> to(ThresholdType thresholdType) {
        return new Range<T>(this.from, new Threshold<T>(thresholdType));
    }

    /**
     * Sets left boundary of range
     * @param thresholdType Threshold type
     * @param value Threshold value
     * @return Range, starting from given point
     */
    public Range<T> to(ThresholdType thresholdType, T value) {
        return new Range<T>(this.from, new Threshold<T>(thresholdType, value));
    }
}
