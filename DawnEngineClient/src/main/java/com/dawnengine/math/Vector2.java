package com.dawnengine.math;

import java.io.Serializable;

/**
 *
 * @author alyss
 */
public class Vector2 implements Serializable {

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

    public Vector2() {
        this(0, 0);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public Vector2(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Float.floatToIntBits(this.x);
        hash = 37 * hash + Float.floatToIntBits(this.y);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vector2 other = (Vector2) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Vector2{" + "x=" + x + ", y=" + y + '}';
    }

}
