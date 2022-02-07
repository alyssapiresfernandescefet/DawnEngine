package com.dawnengine.game;

import com.dawnengine.graphics.Renderer;
import java.awt.Canvas;

public class Game extends Canvas implements GameEvents {

    private final GameLoop loop;
    private final Renderer renderer;

    public Game() {
        loop = new GameLoop(this);
        renderer = new Renderer(this);
    }

    public void start() {
        this.createBufferStrategy(3);
        loop.start();
    }

    public void stop() {
        loop.stop();
    }

    @Override
    public void onHandleInput() {
        
    }

    @Override
    public void onUpdate(double dt) {
    }

    @Override
    public void onRender() {
        renderer.begin();

        renderer.end();
    }

}
