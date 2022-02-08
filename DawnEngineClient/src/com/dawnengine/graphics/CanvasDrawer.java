package com.dawnengine.graphics;

import com.dawnengine.game.entity.Entity;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;

/**
 *
 * @author alyss
 */
public abstract class CanvasDrawer {

    private final Canvas targetCanvas;

    protected Graphics2D g;
    private BufferStrategy bs;

    protected CanvasDrawer(Canvas targetCanvas) {
        this.targetCanvas = targetCanvas;
    }

    public void begin() {
        bs = targetCanvas.getBufferStrategy();
        g = (Graphics2D) bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, targetCanvas.getWidth(), targetCanvas.getHeight());
        onDrawBegin();
    }

    public void end() {
        onDrawEnd();
        g.dispose();
        bs.show();
    }

    public void drawString(String str, int x, int y) {
        g.drawString(str, x, y);
    }

    public void fillRect(int x, int y, int width, int height) {
        g.fillRect(x, y, width, height);
    }

    public void drawRect(int x, int y, int width, int height) {
        g.drawRect(x, y, width, height);
    }

    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return g.drawImage(img, x, y, observer);
    }

    public void setColor(Color c) {
        g.setColor(c);
    }

    public void setFont(Font font) {
        g.setFont(font);
    }
    
    public abstract void drawEntity(Entity entity);

    protected void onDrawBegin() {
    }

    protected void onDrawEnd() {
    }
}
