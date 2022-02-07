package com.dawnengine.game.entity;

import com.dawnengine.graphics.Renderer;

/**
 *
 * @author alyss
 */
public interface Entity {
    public void update(double dt);
    public void render(Renderer rend);
}
