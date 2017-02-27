package com.timewarp.engine.gui.controls;


import com.timewarp.engine.entities.GameObject;
import com.timewarp.engine.entities.components.ui.Panel;
import com.timewarp.engine.entities.components.ui.Text;

public class UIButton extends GameObject {

    public Text text;
    public Panel panel;

    @Override
    protected void init() {
        super.init();

        this.text = this.addComponent(Text.class);
        this.panel = this.addComponent(Panel.class);
    }
}