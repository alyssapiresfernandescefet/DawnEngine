package com.dawnengine.entity;

import java.awt.Image;

/**
 *
 * @author alyss
 */
public interface Sprite {
    public Image sprite();
    public void sprite(Image image);
    public int getWidth();
    public int getHeight();
}
