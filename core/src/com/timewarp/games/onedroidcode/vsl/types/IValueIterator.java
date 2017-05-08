package com.timewarp.games.onedroidcode.vsl.types;

import com.timewarp.games.onedroidcode.vsl.Value;

public interface IValueIterator {


    /**
     * @return ID of value type
     */
    int getValueType();

    /**
     * Gets next iteration value
     *
     * @return next value or null
     */
    Value next();

    /**
     * Checks whether iterator has next value or not
     *
     * @return true - if next value is available
     */
    boolean hasNext();


    /**
     * Resets iterator
     */
    void reset();
}
