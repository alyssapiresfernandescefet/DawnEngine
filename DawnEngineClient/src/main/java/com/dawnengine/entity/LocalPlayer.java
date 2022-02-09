package com.dawnengine.entity;

import com.dawnengine.game.Input;
import com.dawnengine.math.Vector2;
import java.awt.event.KeyEvent;

/**
 *
 * @author alyss
 */
public class LocalPlayer extends NetworkEntity {

    private static LocalPlayer instance;

    public static LocalPlayer get() {
        return instance;
    }

    public LocalPlayer(int id, Vector2 position) {
        super(id, position);
        if (instance != null) {
            throw new ExceptionInInitializerError("There is already an active local player.");
        }
        instance = this;
    }

    @Override
    public void update(double dt) {
        double motion = dt * 100;
        Vector2 pos = transform().position();

        if (Input.getKey(KeyEvent.VK_W)) {
            pos.y -= motion;
            invalidateTransform();
        } else if (Input.getKey(KeyEvent.VK_S)) {
            pos.y += motion;
            invalidateTransform();
        }

        if (Input.getKey(KeyEvent.VK_A)) {
            pos.x -= motion;
            invalidateTransform();
        } else if (Input.getKey(KeyEvent.VK_D)) {
            pos.x += motion;
            invalidateTransform();
        }
    }

}
