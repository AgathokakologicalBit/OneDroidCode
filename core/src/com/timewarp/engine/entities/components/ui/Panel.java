package com.timewarp.engine.entities.components.ui;

import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.entities.Component;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;


public final class Panel extends Component {

    private final Color defaultBackgroundColor = new Color(0, 0, 0, 0.5f);

    private Color backgroundColor;
    private Color borderColor;
    private float borderWidth;

    protected Panel(GameObject gameObject) {
        super(gameObject);
    }


    @Override
    public void awake() {
        this.borderColor = Color.BLACK;
        this.borderWidth = 1;
        this.backgroundColor = defaultBackgroundColor;
    }

    @Override
    public void render() {
        if (this.borderWidth == 0)
            GUI.DrawRectangle(transform.position, transform.scale, borderWidth, borderColor);

        GUI.DrawPanel(transform.position, transform.scale, backgroundColor);
    }


    /**
     * Sets Panel's background color
     * @param newColor new background color
     */
    public void setBackgroundColor(Color newColor) {
        if (newColor == null) newColor = defaultBackgroundColor;
        this.backgroundColor = newColor;
    }

    /**
     * Gets Panel's background color
     * @return current background color
     */
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
     * Sets Panel's border with (Can't be less than 0)
     * @param borderWidth target width
     */
    public void setBorderWidth(float borderWidth) {
        this.borderWidth = Math.max(borderWidth, 0);
    }

    /**
     * Returns Panel's border width
     * @return current border width
     */
    public float getBorderWidth() {
        return this.borderWidth;
    }

    /**
     * Sets Panel's border color
     * @param newColor new border color
     */
    public void setBorderColor(Color newColor) {
        if (newColor == null) newColor = Color.BLACK;
        this.borderColor = newColor;
    }

    /**
     * Returns Panel's border color
     * @return current border color
     */
    public Color getBorderColor() {
        return this.borderColor;
    }
}
