package com.timewarp.engine.entities;

import com.timewarp.engine.Vector2D;

import java.util.ArrayList;

public class Transform {

    /**
     * Reference to transform holder(GameObject)
     */
    public final GameObject gameObject;

    public final Vector2D position;
    public final Vector2D scale;
    public float rotation;

    public final Vector2D localPosition;
    public final Vector2D localScale;
    public float localRotation;

    // Have no support at this moment
    public Transform parent;
    public final ArrayList<Transform> childs;

    Transform (GameObject gameObject) {
        this.gameObject = gameObject;

        this.position = new Vector2D();
        this.scale = new Vector2D();
        this.rotation = 0;

        this.localPosition = new Vector2D();
        this.localScale = new Vector2D();
        this.localRotation = 0;

        this.parent = null;
        this.childs = new ArrayList<Transform>();
    }

    public final void moveTo (Vector2D position) {
        final Vector2D diff = this.position.sub(this.localPosition);
        this.position.set(position);
        this.localPosition.set(this.position.sub(diff));
    }

    public final void moveBy (Vector2D distance) {
        this.position.set(this.position.add(distance));
        this.localPosition.set(this.localPosition.add(distance));
    }

    public final void moveToLocal(Vector2D position) {
        final Vector2D diff = this.localPosition.sub(this.position);
        this.localPosition.set(position);
        this.position.set(this.localPosition.sub(diff));
    }

    public final void setScale (Vector2D scale) {
        final Vector2D diff = this.scale.sub(this.localScale);
        this.scale.set(scale);
        this.localScale.set(this.scale.sub(diff));
    }

    public final void increaseScale (Vector2D scale) {
        this.scale.set(this.scale.add(scale));
        this.localScale.set(this.localScale.add(scale));
    }

    public void setRotation(float rotation) {
        final float diff = this.rotation - this.localRotation;
        this.rotation = rotation;
        this.localRotation = this.rotation - diff;
    }

    public void rotateBy(float rotation) {
        this.rotation += rotation;
        this.localRotation += rotation;
    }



    public final void addChild (Transform child) {
        child.removeParent();
        this.childs.add(child);
        child.parent = this;
    }

    public final void setParent (Transform parent) {
        final Vector2D oldPos = this.parent == null ? new Vector2D() : this.parent.position;
        final float oldRotation = this.parent == null ? 0 : this.parent.rotation;

        parent.addChild(this);

        this.moveBy(this.parent.position.sub(oldPos));
        this.localScale.set(this.scale.div(this.parent.scale));
        this.rotateBy(this.parent.rotation - oldRotation);
    }

    public final void removeChild (Transform child) {
        if (!this.childs.remove(child)) return;

        child.localPosition.set(position);
        child.parent = null;
    }

    public final void removeParent () {
        if (this.parent == null) return;
        this.parent.removeChild(this);
    }
}
