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
    private int moral;
    private int linkUp, linkDown, linkRight, linkLeft;
    private int tileCountX, tileCountY;
    private String tiles;

    private MapData() {
    }

    public MapData(String name, int tileCountX, int tileCountY) {
        this.name = name;
        this.lastRevision = new Date().getTime();
        this.tileCountX = tileCountX;
        this.tileCountY = tileCountY;
        this.tiles = "";
        var length = tileCountX * tileCountY;
        for (int i = 0; i < length - 1; i++) {
            this.tiles += "0x" + i + "x1x1_";
        }
        tiles += "0x" + (length - 1) + "x1x1";
    }

    public MapData(String name, int tileCountX, int tileCountY, long lastRevision,
            int moral, int linkUp, int linkDown,
            int linkRight, int linkLeft, String tiles) {
        this.name = name;
        this.lastRevision = lastRevision;
        this.tileCountX = tileCountX;
        this.tileCountY = tileCountY;
        this.moral = moral;
        this.linkUp = linkUp;
        this.linkDown = linkDown;
        this.linkRight = linkRight;
        this.linkLeft = linkLeft;
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

    public int getMoral() {
        return moral;
    }

    public void setMoral(int moral) {
        this.moral = moral;
    }

    public int getLinkUp() {
        return linkUp;
    }

    public void setLinkUp(int linkUp) {
        this.linkUp = linkUp;
    }

    public int getLinkDown() {
        return linkDown;
    }

    public void setLinkDown(int linkDown) {
        this.linkDown = linkDown;
    }

    public int getLinkRight() {
        return linkRight;
    }

    public void setLinkRight(int linkRight) {
        this.linkRight = linkRight;
    }

    public int getLinkLeft() {
        return linkLeft;
    }

    public void setLinkLeft(int linkLeft) {
        this.linkLeft = linkLeft;
    }

    public String getTiles() {
        return tiles;
    }

    public void setTiles(String tiles) {
        this.tiles = tiles;
    }

}
