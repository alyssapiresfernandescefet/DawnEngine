package com.dawnengine.game.map;

import com.dawnengine.entity.Sprite;
import java.awt.Image;

/**
 *
 * @author alyss
 */
public class Tile implements Sprite {

    public static final int TILE_SIZE_X = 32, TILE_SIZE_Y = 32;

    private Image sprite;

    public Tile(Image sprite) {
        this.sprite = sprite;
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
        return TILE_SIZE_X;
    }

    @Override
    public int getHeight() {
        return TILE_SIZE_Y;
    }
}
