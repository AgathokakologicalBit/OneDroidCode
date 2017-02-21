package com.timewarp.engine;

/**
 * Class, that represent Vector with x and y coordinates
 * Coordinates stored as `float` values
 */
public class Vector2D {

    /**
     * Point's x coordinate
     */
    public float x;
    /**
     * Point's x coordinate
     */
    public float y;


    /**
     * Default constructor
     * Initializes coordinates as (0; 0)
     */
    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * standart vector constructor
     * Initializes coordinates as (x; y)
     */
    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates copy of given Vector2D
     *
     * @param v Original Vector2D
     */
    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }


    /**
     * Updates X and Y values of vector
     *
     * @param x new value of X
     * @param y new value of Y
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Updates X and Y values of vector
     *
     * @param vector vector to copy values from
     */
    public void set(Vector2D vector) {
        this.x = vector.x;
        this.y = vector.y;
    }


    /**
     * Translates current vector by given offset
     * Returns new instance of Vector2D
     *
     * @param v Offset
     * @return Translated vector
     */
    public Vector2D add(final Vector2D v) {
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    /**
     * Translates current vector by given offset
     * Returns new instance of Vector2D
     *
     * @param x Horizontal offset
     * @param y Vertical offset
     * @return Translated vector
     */
    public Vector2D add(float x, float y) {
        return new Vector2D(this.x + x, this.y + y);
    }

    /**
     * Translates current vector's X and Y coordinates by given offset
     * Returns new instance of Vector2D
     *
     * @param n Offset
     * @return Translated vector
     */
    public Vector2D add(float n) {
        return new Vector2D(this.x + n, this.y + n);
    }


    /**
     * Subtracts given vector from current
     * Returns new instance of Vector2D
     *
     * @param v Offset
     * @return Translated vector
     */
    public Vector2D sub(final Vector2D v) {
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    /**
     * Translates current vector by inverted offset
     * Returns new instance of Vector2D
     *
     * @param x Horizontal offset
     * @param y Vertical offset
     * @return Translated vector
     */
    public Vector2D sub(float x, float y) {
        return new Vector2D(this.x - x, this.y - y);
    }

    /**
     * Translates current vector's X and Y coordinates by given offset
     * Returns new instance of Vector2D
     *
     * @param n Offset
     * @return Translated vector
     */
    public Vector2D sub(float n) {
        return new Vector2D(this.x - n, this.y - n);
    }


    /**
     * Multiplies current vector by given one
     * Returns new instance of Vector2D
     *
     * @param v Multiplier
     * @return Multiplication of vectors
     */
    public Vector2D mult(final Vector2D v) {
        return new Vector2D(this.x * v.x, this.y * v.y);
    }

    /**
     * Multiplies current vector's coordinates by given values
     * Returns new instance of Vector2D
     *
     * @param x X multiplier
     * @param y Y multiplier
     * @return Multiplication of coordinates
     */
    public Vector2D mult(float x, float y) {
        return new Vector2D(this.x * x, this.y * y);
    }

    /**
     * Multiplies current vector by given value
     * Returns new instance of Vector2D
     *
     * @param n Multiplier
     * @return Vector increased by n times
     */
    public Vector2D mult(float n) {
        return new Vector2D(this.x * n, this.y * n);
    }


    /**
     * Divides current vector by given one
     * Returns new instance of Vector2D
     *
     * @param v Divisor
     * @return Division of vectors
     */
    public Vector2D div(final Vector2D v) {
        return new Vector2D(this.x / v.x, this.y / v.y);
    }

    /**
     * Divides current vector's coordinates by given values
     * Returns new instance of Vector2D
     *
     * @param x X Divisor
     * @param y Y Divisor
     * @return Division of coordinates
     */
    public Vector2D div(float x, float y) {
        return new Vector2D(this.x / x, this.y / y);
    }

    /**
     * Divides current vector by given value
     * Returns new instance of Vector2D
     *
     * @param n Divisor
     * @return Vector reduced by n times
     */
    public Vector2D div(float n) {
        return new Vector2D(this.x / n, this.y / n);
    }


    /**
     * Calculates vector's angle using arctangent function
     *
     * @return angle of current vector in radians
     */
    public float getAngle() {
        return (float) Math.atan2(this.y, this.x);
    }

    /**
     * Returns Euclidean length of vector
     *
     * @return Euclidean length of current vector
     */
    public float getLength() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Returns squared Euclidean length of vector
     * Doesn't calculates sqrt for faster performance
     *
     * @return Euclidean length of current vector
     */
    public float getLengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    /**
     * Creates a copy of current vector
     *
     * @return copy of current vector
     */
    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

    /**
     * Returns vector, rotated by given angle
     *
     * @param theta rotation in radians
     * @return vector, rotated by given angle
     */
    public Vector2D rotated(float theta) {
        final float angle = this.getAngle();
        final float length = this.getLength();
        final float TWO_PI = com.timewarp.engine.Math.Mathf.PI * 2;

        final float newAngle = (angle + theta % TWO_PI) % TWO_PI;

        return new Vector2D(
                (float) Math.cos(newAngle) * length,
                (float) Math.sin(newAngle) * length
        );
    }

    /**
     * Returns normalized vector
     * * Vector with Euclidean length of 1
     * Returns 0 length vector if current vector have 0 length
     *
     * @return normalized vector
     */
    public Vector2D normalized() {
        return this.normalized(1);
    }

    /**
     * Returns normalized vector to given length
     * * Vector with Euclidean length of 'targetLength'
     * Returns 0 length vector if current vector have 0 length
     *
     * @param targetLength Target vector length
     * @return normalized vector
     */
    public Vector2D normalized(float targetLength) {
        float length = getLength();
        if (length == 0) return new Vector2D();

        return new Vector2D(this.x / length * targetLength, this.y / length * targetLength);
    }

    @Override
    public String toString() {
        // Returns formatted vector position
        // Using Locale.US, to work with string using ASCII symbols
        return String.format(java.util.Locale.US, "(%.2f, %.2f)", this.x, this.y);
    }
}