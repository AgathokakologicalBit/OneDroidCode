package com.timewarp.engine.gui.controls;


import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.gui.GUI;
import com.timewarp.engine.gui.GUIControl;

public class Button extends GUIControl {

    public String text = "";
    public float textSize = 1f;
    public Color foregroundColor = Color.WHITE;

    public Button() {
        super();
    }

    public Button(float x, float y) {
        super(x, y);
    }

    public Button(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void render() {
        super.render();

        GUI.DrawText(text, position, size, textSize, foregroundColor);
    }
}