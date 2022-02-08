package com.dawnengine.game;

/**
 *
 * @author alyss
 */
public interface GameEvents {
    
    public void onStart();

    public void onUpdate(double dt);
    
    public void onNetworkUpdate();

    public void onRender();
}
