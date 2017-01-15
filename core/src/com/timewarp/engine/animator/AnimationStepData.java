package com.timewarp.engine.animator;

import com.timewarp.engine.Vector2D;

public class AnimationStepData {
    public float time;

    public Vector2D position;
    public Vector2D size;

    boolean relative;


    public AnimationStepData() {
        this(new Vector2D(), new Vector2D(), 0);
    }

    public AnimationStepData(float x, float y, float width, float height) {
        this(new Vector2D(x, y), new Vector2D(width, height), 0);
    }

    public AnimationStepData(float x, float y, float width, float height, float time) {
        this(new Vector2D(x, y), new Vector2D(width, height), time);
    }

    public AnimationStepData(Vector2D position, Vector2D size, float time) {
        this.time = time;

        this.position = position;
        this.size = size;

        this.relative = Animation.MODE_ABSOLUTE;
    }

    public boolean isRelative() {
        return this.relative;
    }
}