package com.dawnengine.game.map;

import com.dawnengine.serializers.TilesetLoader;
import com.dawnengine.serializers.objects.MapData;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 *
 * @author alyss
 */
public class Map {
    public static final int IGNORED_COLOR = 0xFFFF00FF;
    
    private final int index;
    private String name;
    private long lastRevision;
    private int tileCountX, tileCountY;
    private final Tile[][] tiles;
    private BufferedImage tilemap;

    public Map(int index, MapData map) {
        this.index = index;
        this.name = map.getName();
        this.lastRevision = map.getLastRevision();
        this.tileCountX = map.getTileCountX();
        this.tileCountY = map.getTileCountY();
        this.tiles = new Tile[Tile.LAYERS_NUM][this.tileCountX * this.tileCountY];

        String[] serializedTiles = map.getTiles().split("_");
        var tilesetsMap = new HashMap<Integer, Tileset>(1);

        for (int i = 0; i < serializedTiles.length; i++) {
            var split = serializedTiles[i].split("x");
            var layerIndex = Integer.parseInt(split[0]);
            var mapIndex = Integer.parseInt(split[1]);

            var tilesetNum = Integer.parseInt(split[2]);
            var tileIndex = Integer.parseInt(split[3]);

            Tileset tileset;
            if (tilesetsMap.containsKey(tilesetNum)) {
                tileset = tilesetsMap.get(tilesetNum);
            } else {
                tileset = TilesetLoader.load(tilesetNum);
                tilesetsMap.put(tilesetNum, tileset);
            }
            this.tiles[layerIndex][mapIndex] = new Tile(tileset, tileIndex);
        }

        for (int i = 0; i < Tile.LAYERS_NUM; i++) {
            var layer = this.tiles[i];
            for (int j = 0; j < layer.length; j++) {
                var tile = layer[j];
                if (tile == null) {
                    layer[j] = new Tile();
                }
            }
        }

        bakeTilemap();
    }

    public Map(Map other) {
        this.index = other.index;
        this.name = other.name;
        this.lastRevision = other.lastRevision;
        this.tileCountX = other.tileCountX;
        this.tileCountY = other.tileCountY;
        this.tiles = new Tile[other.tiles.length][this.tileCountX * this.tileCountY];
        for (int x = 0; x < this.tiles.length; x++) {
            for (int y = 0; y < this.tiles[x].length; y++) {
                this.tiles[x][y] = new Tile(other.tiles[x][y]);
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

    public void setTile(int layer, int index, Tile tile) {
        tiles[layer][index] = tile;
        bakeTilemap(index);
    }

    public void setTiles(int layer, Tile tile) {
        var layerTiles = tiles[layer];
        for (int i = 0; i < layerTiles.length; i++) {
            layerTiles[i] = tile;
            bakeTilemap(i);
        }
    }

    public int getTileCountX() {
        return tileCountX;
    }

    public int getTileCountY() {
        return tileCountY;
    }

    private void bakeTilemap() {
        tilemap = new BufferedImage(getTileCountX() * Tile.SIZE_X,
                getTileCountY() * Tile.SIZE_X, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < tiles.length; i++) {
            var layer = tiles[i];
            for (int j = 0; j < layer.length; j++) {
                var tile = layer[j];
                if (tile.getSprite() == null) {
                    continue;
                }
                for (int y = 0; y < tile.getHeight(); y++) {
                    for (int x = 0; x < tile.getWidth(); x++) {
                        var rgb = tile.getRGB(x, y);
                        if (rgb != 0 && rgb != IGNORED_COLOR) {
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
     *
     * @param index the index of the tile to be baked into the tilemap.
     */
    private void bakeTilemap(int index) {
        var posX = index % getTileCountX() * Tile.SIZE_X;
        var posY = index / getTileCountX() * Tile.SIZE_Y;

        tilemap.setRGB(posX, posY, Tile.SIZE_X, Tile.SIZE_Y,
                new int[Tile.SIZE_X * Tile.SIZE_Y], 0, Tile.SIZE_X);

        for (int i = 0; i < Tile.LAYERS_NUM; i++) {
            var tile = tiles[i][index];

            if (tile.getSprite() == null) {
                continue;
            }

            for (int y = 0; y < tile.getHeight(); y++) {
                for (int x = 0; x < tile.getWidth(); x++) {
                    var rgb = tile.getRGB(x, y);
                    if (rgb != 0 && rgb != IGNORED_COLOR) {
                        tilemap.setRGB(x + posX, y + posY, rgb);
                    }
                }
            }
        }
    }

    public BufferedImage getTilemap() {
        return tilemap;
    }

    public int getIndex() {
        return index;
    }

    public String getSerializedTiles() {
        String str = "";
        for (int i = 0; i < tiles.length; i++) {
            var layer = tiles[i];
            for (int j = 0; j < layer.length; j++) {
                var tile = layer[j];
                if (tile.getSprite() == null) {
                    continue;
                }
                str += i + "x"
                        + j + "x"
                        + tile.getTilesetNum() + "x"
                        + tile.getTileIndex() + "_";
            }
        }
        return str.substring(0, str.length() - 1);
    }
}
