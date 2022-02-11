package com.dawnengine.game.map;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author alyss
 */
public class Tile implements Serializable {

    public static final int SIZE_X = 32, SIZE_Y = 32;

    private final TileLayer layer;
    private final int tilesetIndex, tileIndex;
    private final int mapLocationIndex;
    private transient BufferedImage sprite;
    private final int[] spriteArray;

    public Tile() {
        this.layer = null;
        this.tilesetIndex = 0;
        this.tileIndex = 0;
        this.mapLocationIndex = 0;
        this.sprite = null;
        this.spriteArray = null;
    }

    public Tile(TileLayer layer, int tilesetIndex, int tileIndex,
            int mapLocationIndex, BufferedImage sprite) {
        this.layer = layer;
        this.tilesetIndex = tilesetIndex;
        this.tileIndex = tileIndex;
        this.mapLocationIndex = mapLocationIndex;
        this.sprite = sprite;
        this.spriteArray = this.sprite.getRGB(0, 0, this.sprite.getWidth(), 
                this.sprite.getHeight(), null, 0, this.sprite.getWidth());
    }

    public Image getSprite() {
        if (sprite == null) {
            sprite = new BufferedImage(SIZE_X, SIZE_Y, BufferedImage.TYPE_INT_RGB);
            sprite.setRGB(0, 0, SIZE_X, SIZE_Y, spriteArray, 0, SIZE_X);
        }
        return sprite;
    }

    /**
     * Gets the Tile index relative to the Map.
     */
    public int getIndexOnMap() {
        return mapLocationIndex;
    }

    public TileLayer getLayer() {
        return layer;
    }

    /**
     * Gets the Tile index relative to its Tileset.
     */
    public int getIndexOnTileset() {
        return tileIndex;
    }

    /**
     * Gets the parent Tileset index.
     */
    public int getTilesetIndex() {
        return tilesetIndex;
    }

}
