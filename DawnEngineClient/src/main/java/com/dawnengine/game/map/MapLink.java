package com.dawnengine.game.map;

/**
 *
 * @author alyss
 */
public class MapLink {
    public static final int SIDES = 4;
    public static final int UP = 0, DOWN = 1, RIGHT = 2, LEFT = 3;
    
    private final int direction;
    private int mapIndex;

    public MapLink(int mapIndex, int direction) {
        this.mapIndex = mapIndex;
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public int getMapIndex() {
        return mapIndex;
    }

    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }
    
    
}
