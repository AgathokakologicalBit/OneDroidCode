package com.timewarp.engine.gui.controls;

import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.Vector2D;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.GUIControl;


public class Textbox extends GUIControl {
    public String text = "";
    public float textSize = 1f;
    public Color foregroundColor = Color.WHITE;

    public Textbox() {
        super();
    }

    public Textbox(Vector2D position) {
        super(position);
    }

    public Textbox(Vector2D position, Vector2D scale) {
        super(position, scale);
    }

    @Override
    public void render() {
        super.render();

        GUI.DrawText(text, transform.position, transform.scale, textSize, foregroundColor);
    }
}
