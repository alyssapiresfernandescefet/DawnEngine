package com.dawnengine.game.map;

import java.awt.image.BufferedImage;

/**
 *
 * @author alyss
 */
public class Tile {

    public static final int SIZE_X = 32, SIZE_Y = 32;

    public static final int GROUND = 0,
            MASK = 1,
            MASK2 = 2,
            FRINGE = 3,
            FRINGE2 = 4,
            LAYERS_NUM = 5;

    private BufferedImage sprite;
    private final int tileIndex, tilesetNum;

    public Tile() {
        this.tileIndex = 0;
        this.tilesetNum = 0;
    }

    public Tile(Tileset tileset, int tileIndex) {
        this.sprite = tileset.getImageTile(tileIndex);
        this.tileIndex = tileIndex;
        this.tilesetNum = tileset.getNum();
    }
    
    public Tile(Tileset tileset, int x, int y) {
        this.sprite = tileset.getImageTile(x, y);
        this.tileIndex = x + y * tileset.getTileCountX();
        this.tilesetNum = tileset.getNum();
    }

    public Tile(Tile other) {
        if (other.sprite != null) {
            this.sprite = new BufferedImage(other.getWidth(), other.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            var rgb = other.sprite.getRGB(0, 0, getWidth(), getHeight(), null, 0, getWidth());
            this.sprite.setRGB(0, 0, getWidth(), getHeight(), rgb, 0, getWidth());
        }
        this.tileIndex = other.tileIndex;
        this.tilesetNum = other.tilesetNum;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public int getTilesetNum() {
        return tilesetNum;
    }

    public int getWidth() {
        return sprite.getWidth();
    }

    public int getHeight() {
        return sprite.getHeight();
    }

    public int getRGB(int x, int y) {
        return sprite.getRGB(x, y);
    }
}
