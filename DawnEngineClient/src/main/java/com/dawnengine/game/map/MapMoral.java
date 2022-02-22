package com.dawnengine.game.map;

/**
 *
 * @author alyss
 */
public enum MapMoral {
    None("None"), SafeZone("Safe Zone");
    
    private final String formattedName;

    private MapMoral(String formattedName) {
        this.formattedName = formattedName;
    }

    @Override
    public String toString() {
        return formattedName;
    }
}
