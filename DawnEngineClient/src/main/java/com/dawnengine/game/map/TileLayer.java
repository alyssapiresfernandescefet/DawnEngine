package com.dawnengine.game.map;

/**
 *
 * @author alyss
 */
public enum TileLayer {
    GROUND(0), MASK(1), MASK2(2), FRINGE(3), FRINGE2(4);
    
    public final int order;

    private TileLayer(int order) {
        this.order = order;
    }

    public static TileLayer get(String layer) {
        switch (layer) {
            case "g":
                return GROUND;
            case "m":
                return MASK;
            case "m2":
                return MASK2;
            case "f":
                return FRINGE;
            case "f2":
                return FRINGE2;
            default:
                return null;
        }
    }
}
