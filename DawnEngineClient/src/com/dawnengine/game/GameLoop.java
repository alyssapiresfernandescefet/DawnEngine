package com.dawnengine.game;

/**
 *
 * @author alyss
 */
public class GameLoop implements Runnable {

    private static final int UPS = 60;

    private final Thread gameThread;
    private boolean isRunning = false;
    private final GameEvents gameEvents;

    public GameLoop(GameEvents events) {
        gameThread = new Thread(this, "Game Loop");
        this.gameEvents = events;
    }

    @Override
    public void run() {
        final double TICK_TIME = 1.0 / UPS;

        long lastTime = System.currentTimeMillis();
        double deltaTime = 0.0;

        while (isRunning) {
            long now = System.currentTimeMillis();
            double timeElapsed = (now - lastTime) * 0.001f;
            lastTime = now;

            deltaTime += timeElapsed;
            while (deltaTime > TICK_TIME) {
                gameEvents.onUpdate(deltaTime);
                deltaTime -= TICK_TIME;
            }
            
            gameEvents.onHandleInput();
            gameEvents.onRender();

            double endTime = now + TICK_TIME * 1000;
            while (System.currentTimeMillis() < endTime) {
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
