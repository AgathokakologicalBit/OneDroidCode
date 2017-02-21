package com.timewarp.engine.entities;

public abstract class Component {
    private boolean isActive = true;

    protected GameObject gameObject;
    protected  Transform transform;

    public void awake() {}
    public void start() {}

    public void update() {}
    public void postUpdate() {}

    public void render() {}

    public final boolean isActive() {
        return this.isActive;
    }

    public final void setActive(boolean new_state) {
        this.isActive = new_state;
    }
}
