package com.timewarp.engine.entities;

public abstract class Component {

    private boolean isActive = true;

    protected final GameObject gameObject;
    protected final Transform transform;

    public Component(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.transform;
    }

    public void awake() {}
    public void start() {}

    public void update() {}
    public void postUpdate() {}

    public void render() {}

    public void dispose() {}

    public final boolean isActive() {
        return this.isActive;
    }

    public final void setActive(boolean newState) {
        this.isActive = newState;
    }

}
