package com.dawnengine.game.graphics;

import java.awt.image.BufferedImage;

/**
 *
 * @author alyss
 */
public class Character {
    private BufferedImage spritesheet;
    private int spriteSizeX, spriteSizeY;

    public Character(BufferedImage spritesheet, int spriteSizeX, int spriteSizeY) {
        this.spritesheet = spritesheet;
        this.spriteSizeX = spriteSizeX;
        this.spriteSizeY = spriteSizeY;
    }
    
    
}
