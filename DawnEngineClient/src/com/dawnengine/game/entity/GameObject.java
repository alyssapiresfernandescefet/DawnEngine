package com.dawnengine.game.entity;

import com.dawnengine.graphics.Renderer;
import com.dawnengine.math.Vector2;
import java.awt.geom.AffineTransform;

/**
 *
 * @author alyss
 */
public class GameObject implements Entity {

    private final AffineTransform transform;

    public GameObject() {
        transform = new AffineTransform();
    }

    public GameObject(AffineTransform transform) {
        this.transform = new AffineTransform(transform);
    }

    public GameObject(Vector2 position) {
        this();
        transform.translate(position.x, position.y);
    }

    @Override
    public void update(double dt) {
        
    }

    @Override
    public void render(Renderer rend) {
        
    }

    public AffineTransform transform() {
        return transform;
    }

}
