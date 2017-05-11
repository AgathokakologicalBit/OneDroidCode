package com.timewarp.engine.entities;

import com.timewarp.engine.Math.Mathf;
import com.timewarp.engine.SceneManager;
import com.timewarp.engine.Time;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.animator.AnimationStepData;
import com.timewarp.engine.animator.Animator;
import com.timewarp.engine.gui.GUI;

import java.lang.reflect.Constructor;
import java.util.ArrayList;


public class GameObject {

    public final Transform transform = new Transform(this);
    public final GameObject gameObject = this;

    private ArrayList<Component> components;
    private ArrayList<Component> componentsInitList;

    private ArrayList<GameObject> childObjects;

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

    public GameObject() {
    }

    public void init() {
        this.isActive = true;

        this.touchStart = new Vector2D();

        this.timeSinceTouchStart = 0;
        this.isLastPressed = false;
        this.isLastLongClicked = false;

        this.isPressed = false;
        this.isClicked = false;
        this.isLongClicked = false;

        this.animator = new Animator(this.transform);

        this.components = new ArrayList<Component>(2);
        this.componentsInitList = new ArrayList<Component>(2);

        this.childObjects = new ArrayList<GameObject>(5);
    }


    /**
     * Runs every frame if Entity is active
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

        for (int componentId = this.componentsInitList.size() - 1; componentId >= 0; --componentId)
        {
            final Component component = this.componentsInitList.get(componentId);
            if (!component.isActive()) continue;

            this.components.add(component);
            this.componentsInitList.remove(componentId);
            component.start();
        }

        for (Component component : this.components)
            if (component.isActive())
                component.update();
    }

    public final void postUpdate() {
        for (Component component : this.components)
            if (component.isActive())
                component.postUpdate();
    }

    private void updateAnimationState(float deltaTime) {
        // Run one frame of animation
        final AnimationStepData data = animator.step(deltaTime);
        if (data == null) return;

        // update control size and position
        this.transform.moveTo(data.position);
        this.transform.setScale(data.size);
        this.transform.setRotation(data.rotation);
    }

    public final void render() {
        for (Component component : this.components) {
            if (component.isActive())
                component.render();
        }
    }

    public final void dispose() {
        for (Component component : this.components)
            component.dispose();
    }


    /**
     * Returns current control state
     * @return is control is is active
     */
    public boolean isActive() {
        return  this.isActive
                && (
                       transform.parent == null
                    || transform.parent.gameObject.isActive()
                );
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
     * Returns gameobject's component by specified class
     * Returns NULL if component wasn't found on object
     * @param component_class Target component class
     * @return Object's component
     */
    @SuppressWarnings("unchecked")
    public final <T extends Component> T getComponent(Class<T> component_class) {
        for (Component c : this.components) {
            if (c.getClass().equals(component_class))
                return (T) c;
        }

        return null;
    }

    /**
     * Adds component to GameObject's components list.
     *
     * Executes 'awake' method of component on addition
     * @param componentClass Component's class that should be added
     * @return true if component was successfully added
     */
    public final <T extends Component> T addComponent(Class<T> componentClass) {
        if (null != gameObject.getComponent(componentClass))
            return null;

        try {
            Constructor<T> constructor = componentClass.getDeclaredConstructor(GameObject.class);
            constructor.setAccessible(true);
            T component = constructor.newInstance(this);

            this.componentsInitList.add(component);

            component.awake();

            return component;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns list of child objects
     * @return ArrayList of child objects
     */
    public final ArrayList<GameObject> getChildObjects() {
        return this.childObjects;
    }

    /**
     * Creates new GameObject with given type and adds it to current scene
     * @param gameobjectType Class of gameobject, that will be instantiated
     * @param <T> Classes instance type
     * @return Created GameObject or null on error
     */
    public static <T extends GameObject> T instantiate(Class<T> gameobjectType) {
        try {
            final Constructor<T> constructor = gameobjectType.getConstructor();
            constructor.setAccessible(true);
            T obj = constructor.newInstance();
            SceneManager.instance.addGameObject(obj);

            obj.init();

            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
