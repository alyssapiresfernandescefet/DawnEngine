package com.dawnengine.math;

/**
 *
 * @author alyss
 */
public final class Mathf {

    public static final double clamp(double num, double min, double max) {
        if (num < min) {
            return min;
        }
        if (num > max) {
            return max;
        }
        return num;
    }
}
