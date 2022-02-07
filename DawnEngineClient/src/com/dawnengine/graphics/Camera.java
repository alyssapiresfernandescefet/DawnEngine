package com.dawnengine.graphics;

import java.awt.Canvas;

public class Camera extends Renderer {

    private static Camera main;

    public static Camera main() {
        return main;
    }

    public Camera(Canvas targetCanvas, boolean isMainCamera) {
        super(targetCanvas);
        if (isMainCamera) {
            main = this;
        }
    }

    public void render() {

    }
}
