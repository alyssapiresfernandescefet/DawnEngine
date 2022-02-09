package com.dawnengine.utils;

/**
 *
 * @author alyss
 */
public class Utils {

    public static boolean isJSONObject(String serialized) {
        return serialized.startsWith("{") && serialized.endsWith("}");
    }

    public static boolean isJSONArray(String serialized) {
        return serialized.startsWith("[") && serialized.endsWith("]");
    }
}
