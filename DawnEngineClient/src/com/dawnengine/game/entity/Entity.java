package com.dawnengine.game.entity;

import com.dawnengine.game.Game;
import com.dawnengine.math.Vector2;
import java.awt.Image;

/**
 * Entities are interactive objects with a Transform and an Image to be
 * rendered. An entity can either be static or dynamic through event methods.
 *
 *
 * @author alyss
 */
public class Entity {
    
    private final int id;
    private Transform transform;
    private Image image;

    public Entity(int id, Vector2 position) {
        this.id = id;
        transform = new Transform(position);
    }

    public Entity(int id, Vector2 position, Vector2 scale, float rotation) {
        this.id = id;
        transform = new Transform(position, scale, rotation);
    }

    public int id() {
        return id;
    }

    public Transform transform() {
        return transform;
    }

    public Image image() {
        return image;
    }

    public void image(Image image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }

    public void start() {
    }

    public void update(double dt) {
    }

    public void networkUpdate() {
    }

    public void onDestroy() {
    }
    
    public static void destroy(Entity entity) {
        Game.removeEntity(entity);
    }
}
