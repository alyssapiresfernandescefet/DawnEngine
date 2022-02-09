package com.dawnengine.game.map;

/**
 *
 * @author alyss
 */
public class Map {
    
    private Tile[] tiles;

    public Map() {
        tiles = new Tile[0];
    }

    public Map(Tile[] tiles) {
        this.tiles = tiles;
    }

    public Tile[] getTiles() {
        return tiles;
    }
    
    
}
