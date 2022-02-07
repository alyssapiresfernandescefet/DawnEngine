package com.dawnengine.game;

/**
 *
 * @author alyss
 */
public interface GameEvents {

    public void onHandleInput();

    public void onUpdate(double dt);

    public void onRender();
}
