package com.timewarp.engine.entities.components.ui;

import com.badlogic.gdx.graphics.Color;
import com.timewarp.engine.entities.Component;
import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.gui.GUI;

public final class Text extends Component {

    private Color textColor;
    private String text;
    private float textSize;
    private boolean centerHorizontally = false;

    protected Text(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void awake() {
        this.textColor = Color.WHITE;
        this.text = "";
        this.textSize = 0;
    }

    @Override
    public void render() {
        if (textSize == 0) {
            GUI.drawText(
                    text,
                    transform.position.x, transform.position.y,
                    transform.scale.x, transform.scale.y,
                    textColor, centerHorizontally
            );
        } else {
            GUI.drawText(
                    text,
                    transform.position.x, transform.position.y,
                    transform.scale.x, transform.scale.y,
                    textSize, textColor,
                    centerHorizontally
            );
        }
    }


    /**
     * Sets text that will be drawn
     * @param newText new text to draw
     */
    public void set(String newText) {
        if (newText == null) newText = "";
        this.text = newText;
    }

    /**
     * Return current text
     * @return current text
     */
    public String get() {
        return this.text;
    }

    /**
     * Sets text color (Default - white)
     * @param newColor new text color
     */
    public void setColor(Color newColor) {
        if (newColor == null) newColor = Color.WHITE;
        this.textColor = newColor;
    }

    /**
     * Return current text color
     * @return text color
     */
    public Color getColor() {
        return this.textColor;
    }

    /**
     * Sets text size
     * @param newSize new text size
     */
    public void setSize(float newSize) {
        if (newSize < 1) newSize = 1;
        this.textSize = newSize;
    }

    /**
     * Returns current text size
     * @return text size
     */
    public float getTextSize() {
        return this.textSize;
    }

    public void setAlignment(boolean centerHorizontally) {
        this.centerHorizontally = centerHorizontally;
    }
}
