package com.dawnengine.game.map;

import java.io.Serializable;
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
    private Tile[] tiles;

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

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

    public void setTilesFromString(String tiles) {
        String[] tilesArr = tiles.split("_");
        
        System.out.println(tiles);
        
        var size = tileCountX * tileCountY;
        if (tilesArr.length != size) {
            throw new IllegalArgumentException("tiles");
        }

        this.tiles = new Tile[size];

        var tilesetMap = new HashMap<Integer, Tileset>();
        for (int i = 0; i < tilesArr.length; i++) {
            var split = tilesArr[i].split("x");
            var tilesetIndex = Integer.parseInt(split[0]);
            var tileIndex = Integer.parseInt(split[1]);
            if (!tilesetMap.containsKey(tilesetIndex)) {
                tilesetMap.put(tilesetIndex, Tileset.load(tilesetIndex));
            }
            var tileset = tilesetMap.get(tilesetIndex);

            this.tiles[i] = new Tile(tileset.getImageTile(tileIndex));
        }
    }

}
