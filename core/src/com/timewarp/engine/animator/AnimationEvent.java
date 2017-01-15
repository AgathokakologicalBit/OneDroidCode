package com.timewarp.engine.animator;

import com.timewarp.engine.Mathf;

public class AnimationEvent {

    /**
     * Event name
     */
    public final String name;

    private float time;


    public AnimationEvent(String name) {
        this(0, name);
    }

    public AnimationEvent(float time, String name) {
        this.time = Mathf.clamp(time, 0.00001f, Float.MAX_VALUE);
        this.name = name;
    }

    /**
     * Sets animation event triggering time
     *
     * @param time New event triggering time
     */
    public void setTime(float time) {
        this.time = Mathf.clamp(time, 0.000001f, Float.MAX_VALUE);
    }

    /**
     * Returns animation event triggering time
     *
     * @return event triggering time
     */
    public float getTime() {
        return this.time;
    }
}
