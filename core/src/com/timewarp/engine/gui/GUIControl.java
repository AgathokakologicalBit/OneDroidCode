package com.timewarp.engine.gui;

import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.Mathf;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.animator.AnimationStepData;
import com.timewarp.engine.animator.Animator;

public abstract class GUIControl {

    // Is current control is active.
    // If false then control does not renders and updates
    private boolean isActive;


    /**
     * Background color of control
     */
    public Color backgroundColor;
    /**
     * Border color of control
     */
    public Color borderColor;
    /**
     * Border thickness in pixels
     */
    public float borderWidth;
    /**
     * Indicates whether border should be displayed or not
     */
    public boolean showBorder;


    /**
     * Control position from top-left corner
     * Uses floating point values for animations
     */
    public Vector2D position;
    /**
     * Control size in pixels
     * x - width, y - height
     * Uses floating point values for animations
     */
    public Vector2D size;


    // Control mouse state
    private Vector2D touchStart;
    private boolean isLastPressed;
    private boolean isLastLongClicked;
    private float timeSinceTouchStart;

    private boolean isPressed;
    private boolean isClicked;
    private boolean isLongClicked;

    // Animator to hold all animations
    /**
     * Holds all animations and animation queue
     * Used to modify animation state of control
     */
    public Animator animator;


    private void Init(Vector2D position, Vector2D size) {
        this.isActive = true;

        this.position = position;
        this.size = size;

        this.touchStart = new Vector2D();

        this.timeSinceTouchStart = 0;
        this.isLastPressed = false;
        this.isLastLongClicked = false;

        this.isPressed = false;
        this.isClicked = false;
        this.isLongClicked = false;

        this.showBorder = false;
        this.borderColor = Color.BLACK;
        this.borderWidth = 1;
        this.backgroundColor = new Color(0, 0, 0, 0.5f);

        this.animator = new Animator();
    }


    /**
     * Creates new control at default position(0; 0) with default scale(0; 0)
     */
    public GUIControl() {
        Init(new Vector2D(), new Vector2D());
    }

    /**
     * Creates new control at given position with default scale(0; 0)
     * @param x Horizontal position
     * @param y Vertical position
     */
    public GUIControl(float x, float y) {
        Init(new Vector2D(x, y), new Vector2D());
    }

    /**
     * Creates new control at given position with specified scale
     * @param x Horizontal position
     * @param y Vertical position
     * @param width Control width
     * @param height Control height
     */
    public GUIControl(float x, float y, float width, float height) {
        Init(new Vector2D(x, y), new Vector2D(width, height));
    }

    /**
     * Creates new control at given position with default scale(0; 0)
     * @param position Control position
     */
    public GUIControl(Vector2D position) {
        Init(position, new Vector2D());
    }

    /**
     * Creates new cotnrol at given position with specified scale
     * @param position Control position
     * @param size Control size
     */
    public GUIControl(Vector2D position, Vector2D size) {
        Init(position, size);
    }


    /**
     * Runs every frame if Control is active
     * @param deltaTime Time passed between last and current frames
     */
    public void update(float deltaTime) {
        this.updateAnimationState(deltaTime);

        // Recalculate control mouse state
        if (isPressed) timeSinceTouchStart += deltaTime;
        else timeSinceTouchStart = 0;


        isLastPressed = isPressed;
        isPressed = Mathf.inRectangle(GUI.touchPosition, position, size);

        if (isPressed && !isLastPressed) touchStart = GUI.touchPosition.copy();

        final boolean touchStartAtCurrent = Mathf.inRectangle(touchStart, position, size);

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

        // update control size and position
        if (data.isRelative()) {
            this.position = this.position.add(data.position);
            this.size = this.size.add(data.size);
        } else {
            this.position = data.position;
            this.size = data.size;
        }
    }

    /**
     * Runs every frame if Control is active
     * Renders it on screen
     */
    public void render() {
        GUI.DrawPanel(this.position, this.size, backgroundColor);

        if (showBorder)
            GUI.DrawRectangle(this.position, this.size, borderWidth, borderColor);
    }

    /**
     * Unloads all used assets
     * And finishes control work
     */
    public void dispose() {}


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