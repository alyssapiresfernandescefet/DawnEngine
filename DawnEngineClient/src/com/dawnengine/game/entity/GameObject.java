package com.dawnengine.game.entity;

import com.dawnengine.graphics.CanvasDrawer;
import java.awt.Image;

public abstract class GameObject implements Entity {

    private final Transform transform;
    private Image image;

    public GameObject() {
        transform = new Transform();
    }

    public Transform transform() {
        return transform;
    }

    public Image image() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }

    @Override
    public void render(CanvasDrawer rend) {
        rend.drawGameObject(this);
    }

}
