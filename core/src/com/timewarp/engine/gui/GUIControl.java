package com.timewarp.engine.gui;

import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.entities.Entity;
import com.timewarp.engine.Vector2D;

import java.util.ArrayList;

public abstract class GUIControl extends Entity {

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

    public ArrayList<GUIControl> controls;


    @Override
    protected void init(Vector2D position, Vector2D size) {
        super.init(position, size);

        this.showBorder = false;
        this.borderColor = Color.BLACK;
        this.borderWidth = 1;
        this.backgroundColor = new Color(0, 0, 0, 0.5f);

        this.controls = new ArrayList<GUIControl>(2);
    }


    /**
     * Creates new control at default position(0; 0) with default scale(0; 0)
     */
    public GUIControl() {
        init(new Vector2D(), new Vector2D());
    }

    /**
     * Creates new control at given position with default scale(0; 0)
     * @param x Horizontal position
     * @param y Vertical position
     */
    public GUIControl(float x, float y) {
        init(new Vector2D(x, y), new Vector2D());
    }

    /**
     * Creates new control at given position with specified scale
     * @param x Horizontal position
     * @param y Vertical position
     * @param width Control width
     * @param height Control height
     */
    public GUIControl(float x, float y, float width, float height) {
        init(new Vector2D(x, y), new Vector2D(width, height));
    }

    /**
     * Creates new control at given position with default scale(0; 0)
     * @param position Control position
     */
    public GUIControl(Vector2D position) {
        init(position, new Vector2D());
    }

    /**
     * Creates new cotnrol at given position with specified scale
     * @param position Control position
     * @param size Control size
     */
    public GUIControl(Vector2D position, Vector2D size) {
        init(position, size);
    }


    /**
     * Runs every frame if Control is active
     * Renders it on screen
     */
    public void render() {
        GUI.DrawPanel(transform.position, transform.scale, backgroundColor);

        if (showBorder)
            GUI.DrawRectangle(transform.position, transform.scale, borderWidth, borderColor);
    }

    /**
     * Unloads all used assets
     * And finishes control work
     */
    public void dispose() {}
}