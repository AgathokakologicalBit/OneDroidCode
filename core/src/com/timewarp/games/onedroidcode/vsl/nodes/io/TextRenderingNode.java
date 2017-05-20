package com.timewarp.games.onedroidcode.vsl.nodes.io;

import com.timewarp.engine.gui.controls.UITextbox;
import com.timewarp.games.onedroidcode.vsl.CodeRunner;
import com.timewarp.games.onedroidcode.vsl.Node;
import com.timewarp.games.onedroidcode.vsl.Value;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TextRenderingNode extends Node {

    public Value inText;
    private UITextbox textbox;

    public TextRenderingNode(Node next, UITextbox textbox) {
        super(next);

        this.textbox = textbox;

        inText = new Value(Value.TYPE_ANY);
    }

    @Override
    public Node execute(CodeRunner runner) {
        Logger.getAnonymousLogger().log(Level.INFO, "RENDERING [TRY]");
        if (textbox == null) return next;

        Logger.getAnonymousLogger().log(Level.INFO, "RENDERING [SET](" + inText.value.toString() + ")");
        textbox.text.set(inText.toString());

        return next;
    }

    @Override
    public void reset() {

    }
}
