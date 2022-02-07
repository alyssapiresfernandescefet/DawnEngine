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
        this(Vector2.zero(), Vector2.one(), 0f);
    }

    public Transform(Vector2 position) {
        this(position, Vector2.one(), 0f);
    }

    public Transform(Vector2 position, Vector2 scale) {
        this(position, scale, 0f);
    }

    public Transform(Vector2 position, float rotation) {
        this(position, Vector2.one(), rotation);
    }

    public Transform(Vector2 position, Vector2 scale, float rotation) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
    }

    public Vector2 position() {
        return position;
    }

    public Vector2 scale() {
        return scale;
    }

    public float rotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

}
