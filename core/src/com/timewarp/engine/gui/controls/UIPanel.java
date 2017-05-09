package com.timewarp.engine.gui.controls;

import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.entities.components.ui.Panel;

public class UIPanel extends GameObject {

    public Panel panel;

    @Override
    public void init() {
        super.init();

        this.panel = this.addComponent(Panel.class);
    }

    /**
     * Sets Panel's background color
     * @param newColor new background color
     */
    public void setBackgroundColor(Color newColor) {
        this.panel.setBackgroundColor(newColor);
    }

    /**
     * Gets Panel's background color
     * @return current background color
     */
    public Color getBackgroundColor() {
        return this.panel.getBackgroundColor();
    }

    /**
     * Sets Panel's border with (Can't be less than 0)
     * @param borderWidth target width
     */
    public void setBorderWidth(float borderWidth) {
        this.panel.setBorderWidth(borderWidth);
    }

    /**
     * Returns Panel's border width
     * @return current border width
     */
    public float getBorderWidth() {
        return this.panel.getBorderWidth();
    }

    /**
     * Sets Panel's border color
     * @param newColor new border color
     */
    public void setBorderColor(Color newColor) {
        this.panel.setBorderColor(newColor);
    }

    /**
     * Returns Panel's border color
     * @return current border color
     */
    public Color getBorderColor() {
        return this.panel.getBorderColor();
    }
}
