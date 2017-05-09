package com.timewarp.engine.animator;

import com.timewarp.engine.Math.Mathf;
import com.timewarp.engine.Math.Range;

import java.util.ArrayList;

public class Animation {
    public static final boolean FROM_START_POINT = true;
    public static final boolean FROM_CUSTOM_POINT = false;

    public static final boolean MODE_RELATIVE = true;
    public static final boolean MODE_ABSOLUTE = false;


    /**
     * Animation name
     * Can be used for searching
     */
    public final String name;

    // List of animation frames
    public ArrayList<AnimationStepData> steps;
    // List of animation events
    private ArrayList<AnimationEvent> events;

    /**
     * Defines whether animation uses RELATIVE or ABSOLUTE transformations
     */
    private final boolean isRelative;


    public Animation(String name) {
        this(name, FROM_START_POINT, MODE_ABSOLUTE);
    }

    public Animation(String name, boolean startFromBeginning, boolean isRelative) {
        this.name = name;
        this.isRelative = isRelative;

        this.steps = new ArrayList<AnimationStepData>(50);
        if (startFromBeginning)
            this.steps.add(new AnimationStepData());

        this.events = new ArrayList<AnimationEvent>(10);
    }

    /**
     * Returns duration of animation
     * @return duration of animation
     */
    public float getDuration() {
        return steps.get(steps.size() - 1).time;
    }

    /**
     * Calculates new animation step for given time
     * @param time Step time
     * @return Animation step date
     */
    AnimationStepData getStepData(float time) {
        // Create local data variable
        AnimationStepData data = new AnimationStepData();
        data.relative = this.isRelative;

        // Get current animation frame
        int stepIndex = getStepIdByTime(time);
        if (stepIndex < 1) stepIndex = 1;

        // Get two keyframes for lerping
        AnimationStepData left = steps.get(stepIndex - 1);
        AnimationStepData right = steps.get(stepIndex);

        // Lerp values
        final float step = (time - left.time) / (right.time - left.time);

        // Calculate new state
        data.position = Mathf.lerp(left.position, right.position, step);
        data.size = Mathf.lerp(left.size, right.size, step);
        data.rotation = Mathf.lerp(left.rotation, right.rotation, step);

        return data;
    }

    private int getStepIdByTime(float time) {
        final int length = steps.size();

        for (int index = 0; index < length; ++index)
            if (steps.get(index).time >= time)
                return index;

        return -1;
    }

    /**
     * Returns list of events, that occurred in given time interval
     * fromTime is exclusive
     * @param fromTime start time
     * @param toTime end time
     * @return List of occurred events
     */
    public ArrayList<AnimationEvent> getOccurredEvents(float fromTime, float toTime) {
        ArrayList<AnimationEvent> events = new ArrayList<AnimationEvent>(2);

        Range<Float> frame_time = new Range<Float>()
                .from(Range.exclusive, fromTime)
                .to(Range.inclusive, toTime);

        for (AnimationEvent event : this.events) {
            if (frame_time.contains(event.getTime()))
                events.add(event);
        }

        return events;
    }


    /**
     * Adds new keyframe to animation
     * @param time keyframe end time
     * @param data Animation step data
     */
    public void addStep(float time, AnimationStepData data) {
        data.time = time;
        steps.add(data);
    }

    /**
     * Adds new event with given name at specified time
     * @param time Event time
     * @param eventName Event name
     */
    public void addEvent(float time, String eventName) {
        this.events.add(new AnimationEvent(time, eventName));
    }

    public boolean isRelative() {
        return this.isRelative;
    }

    public Animation copy() {
        final Animation anim = new Animation(this.name, FROM_CUSTOM_POINT, this.isRelative);

        for (AnimationStepData step : this.steps) {
            anim.addStep(step.time, step.copy());
        }

        return anim;
    }
}
