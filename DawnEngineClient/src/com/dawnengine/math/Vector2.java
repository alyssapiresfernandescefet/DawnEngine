package com.dawnengine.math;

/**
 *
 * @author alyss
 */
public class Vector2 {

    public float x, y;

    public static Vector2 zero() {
        return new Vector2(0, 0);
    }

    public static Vector2 one() {
        return new Vector2(1, 1);
    }

    public static Vector2 up() {
        return new Vector2(0, 1);
    }

    public static Vector2 down() {
        return new Vector2(0, -1);
    }

    public static Vector2 right() {
        return new Vector2(1, 0);
    }

    public static Vector2 left() {
        return new Vector2(-1, 0);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
    }

}
