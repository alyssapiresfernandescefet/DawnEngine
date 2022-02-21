package com.dawnengine.serializers.objects;

import java.io.Serializable;

/**
 *
 * @author alyss
 */
public class MapData implements Serializable {

    private String name;
    private long lastRevision;
    private int tileCountX, tileCountY;
    private String tiles;

    private MapData() {
    }

    public MapData(String name, int tileCountX, int tileCountY,
            long lastRevision, String tiles) {
        this.name = name;
        this.lastRevision = lastRevision;
        this.tileCountX = tileCountX;
        this.tileCountY = tileCountY;
        this.tiles = tiles;
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

    public String getTiles() {
        return tiles;
    }
    
}
