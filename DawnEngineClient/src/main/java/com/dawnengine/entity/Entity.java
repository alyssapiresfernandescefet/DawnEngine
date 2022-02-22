package com.dawnengine.entity;

import com.dawnengine.math.Vector2;
import java.awt.Image;

public class Entity implements Sprite {
    
    private final int id;
    private Transform transform;
    private Image sprite;

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

    @Override
    public Image sprite() {
        return sprite;
    }

    @Override
    public void sprite(Image image) {
        this.sprite = image;
    }

    @Override
    public int getWidth() {
        return sprite.getWidth(null);
    }

    @Override
    public int getHeight() {
        return sprite.getHeight(null);
    }

    public void start() {
    }

    public void update(double dt) {
    }
    
    public void networkUpdate() {
        
    }

    public void onDestroy() {
    }
}
