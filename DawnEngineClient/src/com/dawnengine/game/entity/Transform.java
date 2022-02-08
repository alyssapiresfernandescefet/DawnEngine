package com.dawnengine.game.entity;

import com.dawnengine.math.Vector2;

/**
 *
 * @author alyss
 */
public class Transform {

    private Vector2 position, scale;
    private float rotation;

    public Transform() {
        this(Vector2.zero(), Vector2.one(), 0);
    }

    public Transform(Vector2 position) {
        this(position, Vector2.one(), 0);
    }

    public Transform(Vector2 position, Vector2 scale, float rotation) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
    }

    public Vector2 position() {
        return position;
    }

    public void position(Vector2 position) {
        this.position = new Vector2(position);
    }

    public void position(double x, double y) {
        this.position = new Vector2(x, y);
    }

    public Vector2 scale() {
        return scale;
    }

    public void scale(Vector2 scale) {
        this.scale = new Vector2(scale);
    }

    public float rotation() {
        return rotation;
    }

    public void rotation(float rotation) {
        this.rotation = rotation;
    }

}
