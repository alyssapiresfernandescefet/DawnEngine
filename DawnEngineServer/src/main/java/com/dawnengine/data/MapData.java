package com.dawnengine.data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author alyss
 */
public class MapData implements Serializable {

    private String name;
    private long lastRevision;
    private int sizeX, sizeY;
    private String tiles;

    public MapData() {
    }

    public MapData(String name, int sizeX, int sizeY) {
        this.name = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.lastRevision = new Date().getTime();
        var length = this.sizeX * this.sizeY;
        this.tiles = "";
        for (int i = 0; i < length - 1; i++) {
            this.tiles += "0x" + i + "x1x4_";
        }
        this.tiles += "0x" + (length - 1) + "x1x4";
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

    public void setLastRevision(Date lastRevision) {
        this.lastRevision = lastRevision.getTime();
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public String getTiles() {
        return tiles;
    }
}
