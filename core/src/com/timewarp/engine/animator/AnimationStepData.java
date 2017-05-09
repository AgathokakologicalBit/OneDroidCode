package com.timewarp.engine.animator;

import com.timewarp.engine.Vector2D;

public class AnimationStepData {
    public float time;

    public Vector2D position;
    public Vector2D size;
    public float rotation;

    boolean relative;


    public AnimationStepData() {
        this(new Vector2D(), new Vector2D());
    }

    public AnimationStepData(float x, float y, float width, float height) {
        this(new Vector2D(x, y), new Vector2D(width, height), 0);
    }

    public AnimationStepData(Vector2D position, Vector2D size, float rotation) {
        this.position = position;
        this.size = size;
        this.rotation = rotation;

        this.relative = Animation.MODE_ABSOLUTE;
    }

    public AnimationStepData(Vector2D position, Vector2D size) {
        this(position, size, 0);
    }

    public boolean isRelative() {
        return this.relative;
    }

    public AnimationStepData copy() {
        return new AnimationStepData(this.position.copy(), this.size.copy(), this.rotation);
    }
}