package com.timewarp.games.onedroidcode.vsl;

public class Value {

    public String type;
    public Object value;

    public Value(String type) {
        this.type = type;
    }

    public void set(boolean value) {
        this.value = value;
        this.type = "boolean";
    }

    public void set(int value) {
        this.value = value;
        this.type = "integer";
    }

    public void set(float value) {
        this.value = value;
        this.type = "decimal";
    }

    public void set(String value) {
        this.value = value == null ? "" : value;
        this.type = "string";
    }

    public boolean toBoolean() {
        switch (type.charAt(0)) {
            case 'b': return (Boolean) value;
            case 'i': return !value.equals(0);
            case 'd': return !value.equals(0f);
            case 's': return !value.equals("");
        }

        return false;
    }

    public int toInteger() {
        switch (type.charAt(0)) {
            case 'b': return value.equals(false) ? 0 : 1;
            case 'i': return (Integer)value;
            case 'd': return (Integer)value;
            case 's': return value.equals("") ? 0 : 1;
        }

        return 0;
    }

    public String toString() {
        if (value == null) return "";
        return value.toString();
    }
}
