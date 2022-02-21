package com.dawnengine.serializers.objects;

import com.dawnengine.game.map.Tile;
import com.dawnengine.game.map.Tileset;
import com.dawnengine.serializers.TilesetLoader;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author alyss
 */
public class MapData implements Serializable {

    private String name;
    private long lastRevision;
    private int tileCountX, tileCountY;
    private int[][][] tiles;

    private MapData() {
    }

    public MapData(String name, int tileCountX, int tileCountY,
            long lastRevision, String tiles) {
        this.name = name;
        this.lastRevision = lastRevision;
        this.tileCountX = tileCountX;
        this.tileCountY = tileCountY;

        String[] serializedTiles = tiles.split("_");

        this.tiles = new int[Tile.LAYERS_NUM][][];
        var tilesetsMap = new HashMap<Integer, Tileset>();

        for (int i = 0; i < this.tiles.length; i++) {
            this.tiles[i] = new int[tileCountX * tileCountY][];
        }

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

            var img = tileset.getImageTile(tileIndex);
            this.tiles[layerIndex][mapIndex] = img.getRGB(0, 0, img.getWidth(),
                    img.getHeight(), null, 0, img.getWidth());
        }
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

    public int getTileCountX() {
        return tileCountX;
    }

    public void setTileCountX(int tileCountX) {
        this.tileCountX = tileCountX;
    }

    public int getTileCountY() {
        return tileCountY;
    }

    public void setTileCountY(int tileCountY) {
        this.tileCountY = tileCountY;
    }

    public int[][] getTiles(int layer) {
        return tiles[layer];
    }
}
