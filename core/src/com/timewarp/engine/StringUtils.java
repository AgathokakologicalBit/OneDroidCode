package com.timewarp.engine;

public class StringUtils {

    public static String join(String delimiter, String[] array) {
        if (array.length == 0) return "";

        StringBuilder builder = new StringBuilder(array.length * 2 - 1);
        builder.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            builder.append(delimiter);
            builder.append(array[i]);
        }

        return builder.toString();
    }
}
