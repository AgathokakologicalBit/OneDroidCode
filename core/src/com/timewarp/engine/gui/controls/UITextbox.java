package com.timewarp.engine.gui.controls;

import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.entities.components.ui.Text;


public class UITextbox extends GameObject {

    public Text text;

    @Override
    protected void init() {
        super.init();

        this.text = this.addComponent(Text.class);
    }
}
