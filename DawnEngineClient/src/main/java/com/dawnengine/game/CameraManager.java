package com.dawnengine.game;

import com.dawnengine.game.graphics.Camera;
import com.dawnengine.math.Vector2;

/**
 *
 * @author alyss
 */
public class CameraManager {
    private final Camera camera;

    public CameraManager(Camera camera) {
        this.camera = camera;
    }

    public Vector2 position() {
        return camera.position();
    }

    public void follow(Vector2 position) {
        camera.follow(position);
    }

    public Vector2 center() {
        return camera.center();
    }
}
