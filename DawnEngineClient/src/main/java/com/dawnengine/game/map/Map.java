package com.dawnengine.game.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author alyss
 */
public class Map implements Serializable {

    private String name;
    private long lastRevision;
    private int tileCountX, tileCountY;
    private ArrayList<Tile>[] tiles;

    public Map() {
        lastRevision = new Date().getTime();
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

    public int getWidth() {
        return getTileCountX() * Tile.TILE_SIZE_X;
    }

    public int getHeight() {
        return getTileCountY() * Tile.TILE_SIZE_Y;
    }

    public ArrayList<Tile> getTiles(TileLayer layer) {
        return tiles[layer.index];
    }

    public void loadTilesFromString(String tiles) {
        String[] tilesArr = tiles.split("_");
        this.tiles = new ArrayList[]{
            new ArrayList(),
            new ArrayList(),
            new ArrayList(),
            new ArrayList(),
            new ArrayList()
        };

        var tilesetMap = new HashMap<Integer, Tileset>();
        for (int i = 0; i < tilesArr.length; i++) {
            var split = tilesArr[i].split("x");
            var layer = TileLayer.codeToLayer(split[0]);
            var mapLocationIndex = Integer.parseInt(split[1]);
            var tilesetIndex = Integer.parseInt(split[2]);
            var tileIndex = Integer.parseInt(split[3]);

            Tileset tileset;
            if (!tilesetMap.containsKey(tilesetIndex)) {
                tileset = Tileset.load(tilesetIndex);
                tilesetMap.put(tilesetIndex, tileset);
            } else {
                tileset = tilesetMap.get(tilesetIndex);
            }
            var tile = new Tile(layer, tilesetIndex, tileIndex, mapLocationIndex,
                    tileset.getImageTile(tileIndex));
            this.tiles[layer.index].add(tile);
        }
    }

    public String convertTilesToString() {
        String str = "";
        for (int i = 0; i < tiles.length; i++) {
            var layer = tiles[i];
            for (int j = 0; j < layer.size(); j++) {
                var tile = layer.get(i);
                str += tile.getLayer().code + "x"
                        + tile.getIndexOnMap() + "x"
                        + tile.getTilesetIndex() + "x"
                        + tile.getIndexOnTileset() + "_";
            }
        }
        return str.substring(0, str.length() - 1);
    }

}
