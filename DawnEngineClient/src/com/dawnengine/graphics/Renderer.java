package com.dawnengine.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

/**
 *
 * @author alyss
 */
public class Renderer {

    private final Canvas targetCanvas;

    private Graphics2D g;
    private BufferStrategy bs;
    private boolean begin = false;

    public Renderer(Canvas targetCanvas) {
        this.targetCanvas = targetCanvas;
    }

    public void begin() {
        bs = targetCanvas.getBufferStrategy();
        g = (Graphics2D) bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, targetCanvas.getWidth(), targetCanvas.getHeight());
        begin = true;
    }

    public void end() {
        g.dispose();
        bs.show();
        begin = false;
    }

    public Graphics2D getInternalGraphics() {
        return begin ? g : null;
    }
}
