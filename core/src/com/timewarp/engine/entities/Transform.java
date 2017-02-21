package com.timewarp.engine.entities;

import com.timewarp.engine.Vector2D;

public class Transform {

    public final GameObject gameObject;

    public final Vector2D position;
    public final Vector2D scale;
    public float rotation;

    // Have no support at this moment
    public Transform parent;

    Transform(Entity gameObject) {
        if (gameObject instanceof GameObject)
            this.gameObject = (GameObject) gameObject;
        else this.gameObject = null;

        this.position = new Vector2D();
        this.scale = new Vector2D();
        this.rotation = 0;

        this.parent = null;
    }

    public void moveTo(Vector2D position) {
        this.position.set(position.x, position.y);
    }

    public void moveBy(Vector2D distance) {
        this.position.set(
                this.position.x + distance.x,
                this.position.y + distance.y
        );
    }

    public void setScale(Vector2D scale) {
        this.scale.set(scale.x, scale.y);
    }

    public void increaseScale(Vector2D scale) {
        this.scale.set(
                this.scale.x + scale.x,
                this.scale.y + scale.y
        );
    }
}
