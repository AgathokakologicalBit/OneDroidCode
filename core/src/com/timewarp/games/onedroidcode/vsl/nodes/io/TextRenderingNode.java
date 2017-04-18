package com.timewarp.games.onedroidcode.vsl.nodes.io;

import com.timewarp.engine.gui.controls.UITextbox;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TextRenderingNode extends Node {

    private UITextbox textbox;

    public TextRenderingNode(Node next, UITextbox textbox) {
        super(next);

        this.textbox = textbox;

        this.inputs.add(new Value("string"));
    }

    @Override
    public Node execute(CodeRunner runner) {
        Logger.getAnonymousLogger().log(Level.INFO, "RENDERING [TRY]");
        if (textbox == null) return next;
        Logger.getAnonymousLogger().log(Level.INFO, "RENDERING [SET](" + inputs.get(0).value.toString() + ")");
        textbox.text.set(inputs.get(0).toString());
        return next;
    }

    @Override
    public void reset() {

    }

    @Override
    public String toString() {
        return "OUT";
    }
}
