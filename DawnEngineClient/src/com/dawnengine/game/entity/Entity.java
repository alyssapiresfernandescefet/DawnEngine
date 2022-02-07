package com.dawnengine.game.entity;

import com.dawnengine.graphics.CanvasDrawer;

/**
 *
 * @author alyss
 */
public interface Entity {

    public void start();

    public void update(double dt);

    public void render(CanvasDrawer rend);
    
    public void destroy();
}
