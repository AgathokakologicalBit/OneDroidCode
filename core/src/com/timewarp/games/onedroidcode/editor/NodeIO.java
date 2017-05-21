package com.timewarp.games.onedroidcode.editor;

import com.timewarp.games.onedroidcode.vsl.Node;

import java.lang.reflect.Field;

public class NodeIO {

    private Field field;
    private String fieldName;
    private String displayName;

    public Object value;


    public NodeIO(Field field) {
        this.field = field;
        this.fieldName = field.getName();
        this.displayName = this.fieldName
                .replaceFirst("^(in|out)\\d*", "")
                .replaceAll("([A-Z])", " $1")
                .toLowerCase()
                .trim();
    }

    private NodeIO(NodeIO obj) {
        this.field = obj.field;
        this.fieldName = obj.fieldName;
        this.displayName = obj.displayName;
    }


    public String getFieldName() {
        return fieldName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void patchField(Node node, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(node, value);
    }

    public NodeIO copy() {
        return new NodeIO(this);
    }

    public Class getType() {
        return field.getType();
    }
}
