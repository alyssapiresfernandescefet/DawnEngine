package com.dawnengine.game.graphics;

import com.dawnengine.entity.Transform;
import com.dawnengine.entity.Entity;
import com.dawnengine.game.map.Tile;
import com.dawnengine.math.Vector2;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

public class Camera {

    private static final Font DEFAULT_FONT = new Font("Verdana", Font.PLAIN, 24);

    private final Canvas targetCanvas;
    private final AffineTransform transform = new AffineTransform();

    private Graphics2D g;
    private BufferStrategy bs;

    private StringRenderPosition srt;

    public Camera(Canvas targetCanvas) {
        this.targetCanvas = targetCanvas;
        srt = StringRenderPosition.Leading;
    }

    public void begin() {
        bs = targetCanvas.getBufferStrategy();
        g = (Graphics2D) bs.getDrawGraphics();

//        HashMap<Key, Object> rh = new HashMap<>(7);
//        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        rh.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
//        rh.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
//        rh.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        g.addRenderingHints(rh);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, targetCanvas.getWidth(), targetCanvas.getHeight());
        var trans = g.getTransform();
        var x = transform.getTranslateX();
        var y = transform.getTranslateY();
        transform.setToScale(trans.getScaleX(), trans.getScaleY());
        transform.translate(x, y);
        g.setTransform(transform);
        g.setFont(DEFAULT_FONT);
    }

    public void end() {
        g.dispose();
        bs.show();
    }

    public void setColor(Color c) {
        g.setColor(c);
    }

    public void setFont(Font font) {
        g.setFont(font);
    }

    public void setStroke(Stroke s) {
        g.setStroke(s);
    }

    public void setStringRenderPosition(StringRenderPosition srt) {
        if (srt == null) {
            srt = StringRenderPosition.Center;
        }
        this.srt = srt;
    }

    public void drawRect(Vector2 position, Vector2 size) {
        drawRect(position, size, 0, Vector2.one());
    }

    public void drawRect(Vector2 position, Vector2 size, float rotation) {
        drawRect(position, size, rotation, Vector2.one());
    }

    public void drawRect(Vector2 position, Vector2 size, float rotation, Vector2 scale) {
        g.setTransform(new AffineTransform(transform));
        g.translate(position.x + size.x / 2,
                position.y + size.y / 2);
        g.rotate(Math.toRadians(rotation));
        g.scale(scale.x, scale.y);
        g.translate(-(size.x / 2), -(size.y / 2));
        g.drawRect(0, 0, (int) size.x, (int) size.y);
        g.setTransform(transform);
    }

    public void fillRect(Vector2 position, Vector2 size) {
        fillRect(position, size, 0, Vector2.one());
    }

    public void fillRect(Vector2 position, Vector2 size, float rotation) {
        fillRect(position, size, rotation, Vector2.one());
    }

    public void fillRect(Vector2 position, Vector2 size, float rotation, Vector2 scale) {
        g.setTransform(new AffineTransform(transform));
        g.translate(position.x + size.x / 2,
                position.y + size.y / 2);
        g.rotate(Math.toRadians(rotation));
        g.scale(scale.x, scale.y);
        g.translate(-(size.x / 2), -(size.y / 2));
        g.fillRect(0, 0, (int) size.x, (int) size.y);
        g.setTransform(transform);
    }

    public void drawImage(Image img, Vector2 position) {
        drawImage(img, position, 0, Vector2.one());
    }

    public void drawImage(Image img, Vector2 position, float rotation) {
        drawImage(img, position, rotation, Vector2.one());
    }

    public void drawImage(Image img, Vector2 position, float rotation, Vector2 scale) {
        g.setTransform(new AffineTransform(transform));
        g.translate(position.x + img.getWidth(null) / 2,
                position.y + img.getHeight(null) / 2);
        g.rotate(Math.toRadians(rotation));
        g.scale(scale.x, scale.y);
        g.translate(-(img.getWidth(null) / 2), -(img.getHeight(null) / 2));
        g.drawImage(img, 0, 0, null);
        g.setTransform(transform);
    }

    public void drawString(String str, Vector2 position) {
        drawString(str, position, 0, Vector2.one());
    }

    public void drawString(String str, Vector2 position, float rotation) {
        drawString(str, position, rotation, Vector2.one());
    }

    public void drawString(String str, Vector2 position, float rotation, Vector2 scale) {
        int x = 0;
        Rectangle b = getStringBounds(str, position.x, position.y);
        switch (srt) {
            case Center:
                g.setTransform(new AffineTransform(transform));
                x -= b.width;
                break;
            case Leading:
                g.setTransform(new AffineTransform(transform));
                x -= b.width / 2;
                break;
            case Center_Fixed:
                g.setTransform(new AffineTransform());
                x -= b.width;
                break;
            case Leading_Fixed:
                g.setTransform(new AffineTransform());
                x -= b.width / 2;
                break;
        }
        g.translate(position.x + b.width / 2, position.y + b.height / 2);
        g.rotate(Math.toRadians(rotation));
        g.scale(scale.x, scale.y);
        g.translate(x, 0);
        g.drawString(str, 0, 0);
        g.setTransform(transform);
    }

    public void drawEntity(Entity entity) {
        if (entity.sprite() == null) {
            Color old = g.getColor();
            g.setColor(Color.MAGENTA);

            var sizeX = Tile.SIZE_X;
            var sizeY = Tile.SIZE_Y;

            var pos = new Vector2(entity.transform().position());
            pos.x -= sizeX / 2;
            pos.y -= sizeY / 2;
            fillRect(pos, new Vector2(sizeX, sizeY),
                    entity.transform().rotation(), entity.transform().scale());
            g.setColor(old);
            return;
        }
        Transform trans = entity.transform();
        g.setTransform(new AffineTransform(transform));
        g.translate(trans.position().x + entity.getWidth() / 2,
                trans.position().y + entity.getHeight() / 2);
        g.rotate(Math.toRadians(trans.rotation()));
        g.scale(trans.scale().x, trans.scale().y);
        g.translate(-(entity.getWidth() / 2), -(entity.getHeight() / 2));
        g.drawImage(entity.sprite(), 0, 0, null);
        g.setTransform(transform);
    }

    public Vector2 position() {
        return new Vector2(transform.getTranslateX(), transform.getTranslateY());
    }

    private Rectangle getStringBounds(String str, float x, float y) {
        FontRenderContext frc = g.getFontRenderContext();
        GlyphVector gv = g.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, x, y);
    }

    public void follow(Vector2 position) {
        transform.setToTranslation(targetCanvas.getWidth() * 0.5f - position.x,
                targetCanvas.getHeight() * 0.5f - position.y);
    }

    public Vector2 center() {
        return new Vector2(transform.getScaleX() * targetCanvas.getWidth() * 0.5f,
                transform.getScaleY() * targetCanvas.getHeight() * 0.5f);
    }

}
