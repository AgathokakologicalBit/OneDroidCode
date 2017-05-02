package com.timewarp.games.onedroidcode.vsl;

public class Value {

    public static final int TYPE_BOOLEAN = 1;
    public static final int TYPE_INTEGER = 2;
    public static final int TYPE_DECIMAL = 4;
    public static final int TYPE_STRING = 8;

    public static final int TYPE_ANY = 1023;


    public int type;
    public Object value;


    public Value(int type) {
        this.type = type;
    }

    public Value(int type, Object value) {
        this.type = type;
        this.value = value;
    }


    public void set(boolean value) {
        this.value = value;
        this.type = TYPE_BOOLEAN;
    }

    public void set(int value) {
        this.value = value;
        this.type = TYPE_INTEGER;
    }

    public void set(float value) {
        this.value = value;
        this.type = TYPE_DECIMAL;
    }

    public void set(String value) {
        this.value = value == null ? "" : value;
        this.type = TYPE_STRING;
    }

    public boolean toBoolean() {
        switch (type) {
            case TYPE_BOOLEAN:
                return (Boolean) value;
            case TYPE_INTEGER:
                return !value.equals(0);
            case TYPE_DECIMAL:
                return !value.equals(0f);
            case TYPE_STRING:
                return !value.equals("");
        }

        return false;
    }

    public int toInteger() {
        switch (type) {
            case TYPE_BOOLEAN:
                return value.equals(false) ? 0 : 1;
            case TYPE_INTEGER:
                return (Integer) value;
            case TYPE_DECIMAL:
                return (Integer) value;
            case TYPE_STRING:
                return value.equals("") ? 0 : 1;
        }

        return 0;
    }

    public String toString() {
        if (value == null) return "";
        return value.toString();
    }
}
