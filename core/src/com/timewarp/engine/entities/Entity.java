package com.timewarp.engine.entities;


import com.timewarp.engine.Math.Mathf;
import com.timewarp.engine.Time;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.animator.AnimationStepData;
import com.timewarp.engine.animator.Animator;
import com.timewarp.engine.gui.GUI;

public abstract class Entity {

    public final Transform transform = new Transform(this);

    // Is current control is active.
    // If false then control does not renders and updates
    private boolean isActive;


    // Entity mouse state
    private Vector2D touchStart;
    private boolean isLastPressed;
    private boolean isLastLongClicked;
    private float timeSinceTouchStart;

    private boolean isPressed;
    private boolean isClicked;
    private boolean isLongClicked;

    /**
     * Holds all animations and animation queue
     * Used to modify animation state of control
     */
    public Animator animator;


    protected void init(Vector2D position, Vector2D scale) {
        this.isActive = true;

        this.transform.moveTo(position);
        this.transform.setScale(scale);

        this.touchStart = new Vector2D();

        this.timeSinceTouchStart = 0;
        this.isLastPressed = false;
        this.isLastLongClicked = false;

        this.isPressed = false;
        this.isClicked = false;
        this.isLongClicked = false;

        this.animator = new Animator();
    }

    /**
     * Runs every frame if Control is active
     */
    public void update() {
        final float deltaTime = Time.getDeltaTime();

        this.updateAnimationState(deltaTime);

        // Recalculate control mouse state
        if (this.isPressed) timeSinceTouchStart += deltaTime;
        else timeSinceTouchStart = 0;


        isLastPressed = isPressed;
        isPressed = Mathf.inRectangle(
                GUI.touchPosition,
                transform.position,
                transform.scale
        );

        if (isPressed && !isLastPressed)
            touchStart = GUI.touchPosition.copy();

        final boolean touchStartAtCurrent = Mathf.inRectangle(
                touchStart,
                transform.position,
                transform.scale
        );

        isClicked = isLastPressed && touchStartAtCurrent && !GUI.isTouched;
        isLongClicked = isPressed && timeSinceTouchStart >= 0.5f && !isLastLongClicked;

        if (isLongClicked) isLastLongClicked = true;

        final Vector2D movement = touchStart.sub(GUI.touchPosition);
        isLongClicked &= movement.getLengthSquared() <= 100;

        if (!isPressed) {
            isLastLongClicked = false;
            touchStart.set(-1, -1);
        }
    }

    private void updateAnimationState(float deltaTime) {
        // Run one frame of animation
        final AnimationStepData data = animator.step(deltaTime);
        if (data == null) return;

        // update control size and position
        if (data.isRelative()) {
            this.transform.moveBy(data.position);
            this.transform.increaseScale(data.size);
        } else {
            this.transform.moveTo(data.position);
            this.transform.setScale(data.size);
        }
    }

    public abstract void render();


    /**
     * Returns current control state
     * @return is control is is active
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * Sets new control state
     * @param state new control state
     */
    public void setActive(boolean state) {
        this.isActive = state;

        if (this.isActive) return;

        this.isPressed = false;
        this.isClicked = false;
        this.isLongClicked = false;
        this.isLastLongClicked = false;
        this.isLastPressed = false;
    }


    /**
     * Returns true if user is hovering cursor/finger on control
     * @return Is control is pressed
     */
    public boolean isPressed() {
        return this.isPressed;
    }

    /**
     * Returns true if user clicked on control
     * @return Is control was clicked
     */
    public boolean isClicked() {
        return this.isClicked;
    }

    /**
     * Returns true if user holds control for specified amount of time
     * @return Is control was long clicked
     */
    public boolean isLongClicked() {
        return this.isLongClicked;
    }
}
