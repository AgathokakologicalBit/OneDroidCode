package com.timewarp.games.onedroidcode.editor;

import com.timewarp.games.onedroidcode.vsl.Node;

import java.lang.reflect.Field;

public class NodeIO {

    private Field field;
    private String fieldName;
    private String displayName;


    public NodeIO(Field field) {
        this.field = field;
        this.fieldName = field.getName();
        this.displayName = this.fieldName
                .replaceFirst("^(in|out)", "")
                .replaceAll("([A-Z])", " $1")
                .toLowerCase()
                .trim();
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void patchField(Node node, Object value) throws IllegalAccessException {
        field.set(node, value);
    }
}
