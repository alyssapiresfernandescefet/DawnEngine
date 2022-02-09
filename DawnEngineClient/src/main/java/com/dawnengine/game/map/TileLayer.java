package com.dawnengine.game.map;

/**
 *
 * @author alyss
 */
public enum TileLayer {
    GROUND(0, "g"), MASK(1, "m"), MASK2(2, "m2"), FRINGE(3, "f"), FRINGE2(4, "f2");

    public final int index;
    public final String code;

    private TileLayer(int index, String code) {
        this.index = index;
        this.code = code;
    }

    public static TileLayer codeToLayer(String code) {
        for (TileLayer value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
