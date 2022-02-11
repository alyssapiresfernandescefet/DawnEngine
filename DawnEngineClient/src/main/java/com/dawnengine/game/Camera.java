package com.dawnengine.game;

import com.dawnengine.entity.Transform;
import com.dawnengine.entity.Entity;
import com.dawnengine.game.map.Map;
import com.dawnengine.game.map.Tile;
import com.dawnengine.game.map.TileLayer;
import com.dawnengine.math.Vector2;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;

public class Camera {

    private final Canvas targetCanvas;
    private AffineTransform cameraTransform = new AffineTransform();

    protected Graphics2D g;
    private BufferStrategy bs;

    private static Camera main;

    public static Camera main() {
        return main;
    }

    public Camera(Canvas targetCanvas) {
        this.targetCanvas = targetCanvas;
        if (main != null) {
            throw new ExceptionInInitializerError("There is already an active camera.");
        }
        main = this;
    }

    public void begin() {
        bs = targetCanvas.getBufferStrategy();
        g = (Graphics2D) bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, targetCanvas.getWidth(), targetCanvas.getHeight());
        g.setTransform(cameraTransform);
    }

    public void end() {
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

    public void drawRect(Vector2 position, Vector2 size, float rotation, Vector2 scale) {
        g.setTransform(new AffineTransform(cameraTransform));
        g.translate(position.x + size.x / 2,
                position.y + size.y / 2);
        g.rotate(Math.toRadians(rotation));
        g.scale(scale.x, scale.y);
        g.translate(-(size.x / 2), -(size.y / 2));
        g.drawRect(0, 0, (int) size.x, (int) size.y);
        g.setTransform(cameraTransform);
    }

    public void fillRect(Vector2 position, Vector2 size, float rotation, Vector2 scale) {
        g.setTransform(new AffineTransform(cameraTransform));
        g.translate(position.x + size.x / 2,
                position.y + size.y / 2);
        g.rotate(Math.toRadians(rotation));
        g.scale(scale.x, scale.y);
        g.translate(-(size.x / 2), -(size.y / 2));
        g.fillRect(0, 0, (int) size.x, (int) size.y);
        g.setTransform(cameraTransform);
    }

    public void drawImage(Image img, Vector2 position, float rotation, Vector2 scale) {
        g.setTransform(new AffineTransform(cameraTransform));
        g.translate(position.x + img.getWidth(null) / 2,
                position.y + img.getHeight(null) / 2);
        g.rotate(Math.toRadians(rotation));
        g.scale(scale.x, scale.y);
        g.translate(-(img.getWidth(null) / 2), -(img.getHeight(null) / 2));
        g.drawImage(img, 0, 0, null);
        g.setTransform(cameraTransform);
    }

    public void drawString(String str, Vector2 position, float rotation, Vector2 scale) {
        g.setTransform(new AffineTransform(cameraTransform));
        Rectangle b = getStringBounds(str, position.x, position.y);
        g.translate(position.x + b.width / 2, position.y + b.height / 2);
        g.rotate(Math.toRadians(rotation));
        g.scale(scale.x, scale.y);
        g.translate(-(b.width / 2), -(b.height / 2));
        g.drawString(str, 0, 0);
        g.setTransform(cameraTransform);
    }

    public void drawEntity(Entity entity) {
        if (entity.sprite() == null) {
            Color old = g.getColor();
            g.setColor(Color.MAGENTA);
            fillRect(entity.transform().position(), new Vector2(50, 50),
                    entity.transform().rotation(), entity.transform().scale());
            g.setColor(old);
            return;
        }
        Transform trans = entity.transform();
        g.setTransform(new AffineTransform(cameraTransform));
        g.translate(trans.position().x + entity.getWidth() / 2,
                trans.position().y + entity.getHeight() / 2);
        g.rotate(Math.toRadians(trans.rotation()));
        g.scale(trans.scale().x, trans.scale().y);
        g.translate(-(entity.getWidth() / 2), -(entity.getHeight() / 2));
        g.drawImage(entity.sprite(), 0, 0, null);
        g.setTransform(cameraTransform);
    }

    public void drawMap(Map map) {
        if (map == null) {
            return;
        }

        for (TileLayer layer : TileLayer.values()) {
            var tiles = map.getTiles(layer);
            var sizeX = map.getTileCountX();
            for (int i = 0; i < tiles.size(); i++) {
                var tile = tiles.get(i);
                var img = tile.getSprite();
                if (img == null) {
                    continue;
                }
                g.drawImage(img,
                        Tile.SIZE_X * (tile.getIndexOnMap() % sizeX),
                        Tile.SIZE_Y * (tile.getIndexOnMap() / sizeX),
                        null);
            }
        }

    }

    public AffineTransform getCameraTransform() {
        return cameraTransform;
    }

    private Rectangle getStringBounds(String str, float x, float y) {
        FontRenderContext frc = g.getFontRenderContext();
        GlyphVector gv = g.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, x, y);
    }

}
