package com.dawnengine.game.map;

import java.awt.Color;

/**
 *
 * @author alyss
 */
public enum TileAttribute {
    None("", Color.WHITE), Block("B", Color.RED)
    ;
    
    public final String abbreviation;
    public final Color color;

    private TileAttribute(String abbreviation, Color color) {
        this.abbreviation = abbreviation;
        this.color = color;
    }
    
}
