package com.dawnengine.game.map;

import com.dawnengine.serializers.objects.MapData;
import java.awt.image.BufferedImage;

/**
 *
 * @author alyss
 */
public class Map {

    private String name;
    private long lastRevision;
    private final int tileCountX, tileCountY;
    private BufferedImage[][] tiles;
    private BufferedImage tilemap;

    public Map(MapData map) {
        this.name = map.getName();
        this.lastRevision = map.getLastRevision();
        this.tileCountX = map.getTileCountX();
        this.tileCountY = map.getTileCountY();
        this.tiles = new BufferedImage[Tile.LAYERS_NUM][];
        for (int i = 0; i < tiles.length; i++) {
            var layer = map.getTiles(i);
            this.tiles[i] = new BufferedImage[tileCountX * tileCountY];

            for (int j = 0; j < layer.length; j++) {
                var img = new BufferedImage(Tile.SIZE_X, Tile.SIZE_Y,
                        BufferedImage.TYPE_INT_ARGB);
                if (layer[j] != null) {
                    img.setRGB(0, 0, Tile.SIZE_X, Tile.SIZE_Y,
                            layer[j], 0, Tile.SIZE_X);
                }
                this.tiles[i][j] = img;
            }
        }
        bakeTilemap();
    }

    public Map(Map other) {
        this.name = other.name;
        this.lastRevision = other.lastRevision;
        this.tileCountX = other.tileCountX;
        this.tileCountY = other.tileCountY;
        this.tiles = new BufferedImage[other.tiles.length][];
        for (int x = 0; x < this.tiles.length; x++) {
            this.tiles[x] = new BufferedImage[other.tiles[x].length];
            for (int y = 0; y < this.tiles[x].length; y++) {
                this.tiles[x][y] = other.tiles[x][y];
            }
        }
        bakeTilemap();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastRevision() {
        return lastRevision;
    }

    public void setLastRevision(long lastRevision) {
        this.lastRevision = lastRevision;
    }

    public void setTile(int layer, int index, BufferedImage image) {
        tiles[layer][index] = image;
        bakeTilemap(index);
    }

    public int getTileCountX() {
        return tileCountX;
    }

    public int getTileCountY() {
        return tileCountY;
    }

    private void bakeTilemap() {
        tilemap = new BufferedImage(getTileCountX() * Tile.SIZE_X,
                getTileCountY() * Tile.SIZE_X, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < tiles.length; i++) {
            var layer = tiles[i];
            for (int j = 0; j < layer.length; j++) {
                var tile = layer[j];
                for (int y = 0; y < tile.getHeight(); y++) {
                    for (int x = 0; x < tile.getWidth(); x++) {
                        var rgb = tile.getRGB(x, y);
                        if (rgb != 0) {
                            tilemap.setRGB(x + j % getTileCountX() * Tile.SIZE_X,
                                    y + j / getTileCountX() * Tile.SIZE_Y, rgb);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Bakes a section of the tilemap.
     * @param index the index of the tile to be baked into the tilemap.
     */
    private void bakeTilemap(int index) {
        var posX = index % getTileCountX() * Tile.SIZE_X;
        var posY = index / getTileCountX() * Tile.SIZE_Y;

        tilemap.setRGB(posX, posY, Tile.SIZE_X, Tile.SIZE_Y,
                new int[Tile.SIZE_X * Tile.SIZE_Y], 0, Tile.SIZE_X);

        for (int i = 0; i < Tile.LAYERS_NUM; i++) {
            var img = tiles[i][index];

            if (img == null) {
                continue;
            }

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    var rgb = img.getRGB(x, y);
                    if (rgb != 0) {
                        tilemap.setRGB(x + posX, y + posY, rgb);
                    }
                }
            }
        }
    }

    public BufferedImage getTilemap() {
        return tilemap;
    }
}
