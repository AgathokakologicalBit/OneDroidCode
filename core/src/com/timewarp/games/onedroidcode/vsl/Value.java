package com.timewarp.games.onedroidcode.vsl;

import com.timewarp.games.onedroidcode.vsl.types.IValueIterator;

public class Value {

    // ===--- BASIC TYPES ---===
    public static final int TYPE_BOOLEAN = 1;
    public static final int TYPE_INTEGER = 2;
    public static final int TYPE_DECIMAL = 4;
    public static final int TYPE_STRING = 8;

    // ===--- COMPLEX TYPES ---===
    public static final int TYPE_ITERABLE = 16;


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

    public void set(IValueIterator value) {
        this.value = value;
        this.type = TYPE_ITERABLE;
    }

    public void set(Value val) {
        this.type = val.type;
        this.value = val.value;
    }


    public boolean toBoolean() {
        if (value == null) return false;

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
        if (value == null) return 0;

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

    public static Object getDefaultFor(int type) {
        switch (type) {
            case TYPE_BOOLEAN:
                return false;
            case TYPE_INTEGER:
                return 0;
            case TYPE_DECIMAL:
                return 0d;
            case TYPE_STRING:
                return "";
        }

        return null;
    }
}
