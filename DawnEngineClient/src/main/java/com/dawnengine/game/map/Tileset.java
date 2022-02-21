package com.dawnengine.game.map;

import java.awt.image.BufferedImage;

/**
 *
 * @author alyss
 */
public class Tileset {

    private final int num;
    private BufferedImage tileset;

    public Tileset(int num, BufferedImage tileset) {
        this.num = num;
        this.tileset = tileset;
    }

    public int getNum() {
        return num;
    }

    public BufferedImage getImage() {
        return tileset;
    }

    public void setImage(BufferedImage image) {
        this.tileset = image;
    }

    public int getTileCountX() {
        return tileset.getWidth() / Tile.SIZE_X;
    }

    public int getTileCountY() {
        return tileset.getHeight() / Tile.SIZE_Y;
    }

    public int getWidth() {
        return tileset.getWidth();
    }

    public int getHeight() {
        return tileset.getHeight();
    }

    public BufferedImage getImageTile(int x, int y) {
        if (x < 0 || x >= getTileCountX() || y >= getTileCountY() || y < 0) {
            return null;
        }
        var img = tileset.getSubimage(Tile.SIZE_X * x,
                Tile.SIZE_Y * y,
                Tile.SIZE_X, Tile.SIZE_Y
        );
        return img;
    }

    public BufferedImage getImageTile(int index) {
        return getImageTile(index % getTileCountX(), index / getTileCountX());
    }

}
