package com.dawnengine.serializers.objects;

import com.dawnengine.game.map.Map;
import com.dawnengine.game.map.MapLink;
import java.io.Serializable;

/**
 *
 * @author alyss
 */
public class MapData implements Serializable {
    
    private final String name;
    private final long lastRevision;
    private final int moral;
    private final int linkUp, linkDown, linkRight, linkLeft;
    private final int tileCountX, tileCountY;
    private final String tiles;

    private MapData() {
        this.name = null;
        this.lastRevision = 0;
        this.tileCountX = 0;
        this.tileCountY = 0;
        this.moral = 0;
        this.linkUp = 0;
        this.linkDown = 0;
        this.linkRight = 0;
        this.linkLeft = 0;
        this.tiles = null;
    }
    
    public MapData(Map map) {
        this.name = map.getName();
        this.lastRevision = map.getLastRevision();
        this.tileCountX = map.getTileCountX();
        this.tileCountY = map.getTileCountY();
        this.moral = map.getMoral().ordinal();
        this.linkUp = map.getLink(MapLink.UP).getMapIndex();
        this.linkDown = map.getLink(MapLink.DOWN).getMapIndex();
        this.linkRight = map.getLink(MapLink.RIGHT).getMapIndex();
        this.linkLeft = map.getLink(MapLink.LEFT).getMapIndex();
        this.tiles = map.getSerializedTiles();
    }

    public MapData(String name, int tileCountX, int tileCountY, int moral,
            int linkUp, int linkDown, int linkRight, int linkLeft,
            long lastRevision, String tiles) {
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

    public long getLastRevision() {
        return lastRevision;
    }

    public int getTileCountX() {
        return tileCountX;
    }

    public int getTileCountY() {
        return tileCountY;
    }

    public int getMoral() {
        return moral;
    }

    public int getLinkUp() {
        return linkUp;
    }

    public int getLinkDown() {
        return linkDown;
    }

    public int getLinkRight() {
        return linkRight;
    }

    public int getLinkLeft() {
        return linkLeft;
    }

    public String getTiles() {
        return tiles;
    }

}
