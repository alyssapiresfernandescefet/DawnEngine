package com.dawnengine.game;

import com.dawnengine.graphics.Camera;
import java.awt.Canvas;
import java.awt.Color;

public class Game extends Canvas implements GameEvents {

    private final GameLoop loop;
    private final Camera camera;

    public Game() {
        loop = new GameLoop(this);
        camera = new Camera(this, true);
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
        camera.begin();
        camera.getInternalGraphics().setColor(Color.WHITE);
        camera.getInternalGraphics().drawRect(40, 40, 50, 50);
        camera.getInternalGraphics().drawRect(100, 100, 50, 50);
        camera.getInternalGraphics().drawRect(160, 160, 50, 50);
        camera.getInternalGraphics().drawRect(220, 220, 50, 50);
        camera.getInternalGraphics().drawRect(280, 280, 50, 50);
        camera.getInternalGraphics().drawRect(340, 340, 50, 50);
        camera.getInternalGraphics().drawRect(400, 400, 50, 50);
        camera.getInternalGraphics().drawRect(460, 340, 50, 50);
        camera.getInternalGraphics().drawRect(520, 280, 50, 50);
        camera.getInternalGraphics().drawRect(580, 220, 50, 50);
        camera.getInternalGraphics().drawRect(640, 160, 50, 50);
        camera.getInternalGraphics().drawRect(700, 100, 50, 50);
        camera.getInternalGraphics().drawRect(760, 40, 50, 50);
        camera.end();
    }

}
