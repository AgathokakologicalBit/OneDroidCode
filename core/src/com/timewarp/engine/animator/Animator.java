package com.timewarp.engine.animator;

import com.timewarp.engine.Math.Mathf;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Animator {
    private boolean isPlaying = false;

    private static ArrayList<Animation> animations;
    private Queue<Animation> animationQueue;
    private ArrayList<AnimationEvent> occurredEvents;

    private AnimationStepData lastStep;

    private float playTime = 0;

    /**
     * Initializes Animator with empty animations list
     */
    public static void Init() {
        animations = new ArrayList<Animation>(10);
    }

    public Animator() {
        this.animationQueue = new LinkedList<Animation>();
        this.occurredEvents = new ArrayList<AnimationEvent>(1);

        this.lastStep = new AnimationStepData();
    }

    /**
     * Removes all animations from queue
     * And starts new animation by given index
     * @param index Animation index
     * @return Is animation was added to queue
     */
    public boolean setAnimation(int index) {
        this.stop();
        return this.playAnimation(index);
    }

    /**
     * Removes all animations from queue
     * And starts new animation by given name
     * @param name Animation name
     * @return Is animation was added to queue
     */
    public boolean setAnimation(String name) {
        this.stop();
        return this.playAnimation(name);
    }


    /**
     * Adds animation by index to play queue
     * @param index Animation index
     * @return Is animation was added to queue
     */
    public boolean playAnimation(int index) {
        if (index < 0) return false;
        if (Animator.animations.size() >= index) return false;

        this.animationQueue.offer(Animator.animations.get(index));
        this.isPlaying = true;

        return true;
    }

    /**
     * Adds animation by name to play queue
     * @param name Animation name
     * @return Is animation was added to queue
     */
    public boolean playAnimation(String name) {
        for (Animation anim : animations) {
            if (anim.name.equals(name)) {
                animationQueue.offer(anim);
                this.isPlaying = true;

                return true;
            }
        }

        return false;
    }

    /**
     * Pauses animations playing
     */
    public void pause() {
        this.isPlaying = false;
    }

    /**
     * Resumes animations playing
     */
    public void resume() {
        if (this.animationQueue.size() != 0)
            this.isPlaying = true;
    }

    /**
     * Stops playing all animations
     * And resets animator state
     */
    public void stop() {
        this.animationQueue.clear();
        this.playTime = 0;
        this.isPlaying = false;

        this.lastStep = new AnimationStepData();
    }

    /**
     * Performs animation step with given delta time
     * @param deltaTime Time passed since last update
     * @return Animation step data
     */
    public AnimationStepData step(float deltaTime) {
        if (!this.isPlaying) return null;
        if (animationQueue.size() == 0) return null;

        final Animation animation = animationQueue.peek();

        // Get current animation duration
        final float duration = animation.getDuration();
        this.playTime = Mathf.clamp(this.playTime + deltaTime, 0, duration);

        // Get current animation step data
        AnimationStepData data = animation.getStepData(this.playTime);
        if (data.isRelative()) {
            data.position = data.position.sub(lastStep.position);
            data.size = data.size.sub(lastStep.size);
        }

        // Get all events that occurred on this step
        this.occurredEvents = animation.getOccurredEvents(this.playTime - deltaTime, this.playTime);

        if (this.playTime == duration)
            endCurrentAnimation();

        return data;
    }

    private void endCurrentAnimation() {
        // Remove current animation from queue
        animationQueue.poll();

        // Reset animation play time
        this.playTime = 0;

        // Check if there is no more animations to play
        if (animationQueue.size() == 0)
            this.stop();
    }

    // Return true event with given name has occurred on current step

    /**
     * Returns true if event with given was triggered in last update
     * @param name event name
     * @return Is event was triggered
     */
    public boolean isEventOccurred(String name) {
        for (AnimationEvent event : this.occurredEvents) {
            if (event.name.equals(name))
                return true;
        }

        return false;
    }

    /**
     * Returns current animator state
     * @return is animation is playing
     */
    public boolean isPlaying() {
        return this.isPlaying;
    }


    /**
     * Adds new animation to animations list
     * @param animation Animation to add
     */
    public static void add(Animation animation) {
        animations.add(animation);
    }
}