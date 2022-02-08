package com.dawnengine.game;

/**
 *
 * @author alyss
 */
public class GameLoop implements Runnable {

    private static final int UPS = 60;
    private static final int NUPS = 10;

    private final Thread gameThread;
    private boolean isRunning = false;
    private final GameEvents gameEvents;

    public GameLoop(GameEvents events) {
        gameThread = new Thread(this, "Game Loop");
        this.gameEvents = events;
    }

    @Override
    public void run() {
        final double UPDATE_TIME = 1.0 / UPS;
        final double NETWORK_UPDATE_TIME = 1.0 / NUPS;

        long lastTime = System.currentTimeMillis();
        double deltaTime = 0.0;
        double nDeltaTime = 0.0;

        gameEvents.onStart();
        while (isRunning) {
            long now = System.currentTimeMillis();
            double timeElapsed = (now - lastTime) * 0.001f;
            lastTime = now;

            deltaTime += timeElapsed;
            nDeltaTime += timeElapsed;
            boolean updated = false;
            while (deltaTime > UPDATE_TIME) {
                gameEvents.onUpdate(deltaTime);
                deltaTime -= UPDATE_TIME;
                updated = true;
            }

            if (updated) {
                if (nDeltaTime >= NETWORK_UPDATE_TIME) {
                    gameEvents.onNetworkUpdate();
                    nDeltaTime = 0;
                }
                gameEvents.onRender();
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    public void start() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        gameThread.start();
    }

    public void stop() {
        isRunning = false;
    }
}
