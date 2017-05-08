package com.timewarp.games.onedroidcode.vsl.types;

import com.timewarp.games.onedroidcode.vsl.Value;

public class NumberIterator implements IValueIterator {

    public int from;
    public int to;
    public int step;

    private int value;


    public NumberIterator(int from, int to, int step) {
        this.from = from;
        this.to = to;
        this.step = step;
    }

    @Override
    public int getValueType() {
        return Value.TYPE_INTEGER;
    }

    @Override
    public Value next() {
        final Value result = new Value(Value.TYPE_INTEGER, value);
        value += step;
        return result;
    }

    @Override
    public boolean hasNext() {
        return step != 0 &&
                step > 0
                ? from < to && value < to
                : from > to && value > from;
    }

    @Override
    public void reset() {
        value = from;
    }
}
