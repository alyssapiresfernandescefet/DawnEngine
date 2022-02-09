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
    private String[] tiles;

    public MapData() {
    }

    public MapData(String name, int sizeX, int sizeY) {
        this.name = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.lastRevision = new Date().getTime();

        this.tiles = new String[this.sizeX * this.sizeY];
        for (int i = 0; i < tiles.length; i++) {
            this.tiles[i] = "1x1";
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

    public String[] getTiles() {
        return tiles;
    }

    public String getTile(int index) {
        return tiles[index];
    }

    public void setTile(int index, String tile) {
        tiles[index] = tile;
    }

    public String getTilesAsString() {
        String str = "";
        for (int i = 0; i < tiles.length - 1; i++) {
            str += tiles[i] + "_";
        }
        str += tiles[tiles.length - 1];
        return str;
    }

    @Override
    public String toString() {
        return "MapData{" + "mapName=" + name + ", lastRevision="
                + lastRevision + ", mapSizeX=" + sizeX + ", mapSizeY="
                + sizeY + '}';
    }

}
