package com.dawnengine.game;

import com.dawnengine.game.entity.Entity;
import com.dawnengine.game.entity.LocalPlayer;
import com.dawnengine.graphics.Camera;
import com.dawnengine.math.Vector2;
import java.awt.Canvas;
import java.util.ArrayList;
import java.util.HashMap;

public class Game extends Canvas implements GameEvents {

    private final GameLoop loop;
    private final Camera mainCamera;
    private final Input input;

    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static final HashMap<Integer, Entity> entitiesMap = new HashMap<>();

    private LocalPlayer lp;

    public Game(int playerID) {
        input = new Input();
        this.addKeyListener(input);
        this.addMouseListener(input);
        this.addFocusListener(input);

        loop = new GameLoop(this);
        mainCamera = new Camera(this);
        lp = new LocalPlayer(playerID, Vector2.zero());
        addEntity(lp);
    }

    public void start() {
        this.createBufferStrategy(3);
        loop.start();
    }

    public void stop() {
        loop.stop();
    }

    @Override
    public void onStart() {
        for (Entity entity : entities) {
            entity.start();
        }
    }

    @Override
    public void onUpdate(double dt) {
        for (Entity entity : entities) {
            entity.update(dt);
        }
    }

    @Override
    public void onNetworkUpdate() {
        for (Entity entity : entities) {
            entity.networkUpdate();
        }
        Input.update();
    }

    @Override
    public void onRender() {
        mainCamera.begin();
        for (Entity entity : entities) {
            mainCamera.drawEntity(entity);
        }
        mainCamera.end();
    }

    public static boolean addEntity(Entity e) {
        return entities.add(e) && entitiesMap.put(e.id(), e) == null;
    }

    public static boolean removeEntity(Entity e) {
        return entities.remove(e) && entitiesMap.remove(e.id(), e);
    }

    public static Entity findEntityByID(int id) {
        return entitiesMap.get(id);
    }

}
