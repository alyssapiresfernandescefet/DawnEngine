package com.dawnengine.game;

import com.dawnengine.graphics.Camera;
import java.awt.Canvas;
import java.awt.event.KeyEvent;

public class Game extends Canvas implements GameEvents {

    private final GameLoop loop;
    private final Camera mainCamera;
    private final Input input;

    public Game() {
        input = new Input();
        this.addKeyListener(input);
        this.addMouseListener(input);

        loop = new GameLoop(this);
        mainCamera = new Camera(this, true);
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
        Input.update();
    }

    @Override
    public void onUpdate(double dt) {
        if (Input.getKey(KeyEvent.VK_A)) {
            mainCamera.getCameraTransform().translate(10, 0);
        } else if (Input.getKey(KeyEvent.VK_D)) {
            mainCamera.getCameraTransform().translate(-10, 0);
        }

        if (Input.getKey(KeyEvent.VK_W)) {
            mainCamera.getCameraTransform().translate(0, 10);
        } else if (Input.getKey(KeyEvent.VK_S)) {
            mainCamera.getCameraTransform().translate(0, -10);
        }
    }

    @Override
    public void onRender() {
        mainCamera.begin();
        mainCamera.end();
    }

}
