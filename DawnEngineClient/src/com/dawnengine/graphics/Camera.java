package com.dawnengine.graphics;

import com.dawnengine.game.entity.GameObject;
import com.dawnengine.game.entity.Transform;
import com.dawnengine.math.Vector2;
import java.awt.Canvas;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

public class Camera extends CanvasDrawer {

    private AffineTransform cameraTransform = new AffineTransform();

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

    @Override
    public void drawGameObject(GameObject go) {
        if (go.image() == null) {
            return;
        }

        Transform trans = go.transform();
        g.setTransform(new AffineTransform(cameraTransform));
        g.translate(trans.position().x + go.getWidth() / 2,
                trans.position().y + go.getHeight() / 2);
        g.rotate(Math.toRadians(trans.rotation()));
        g.scale(trans.scale().x, trans.scale().y);
        g.translate(-(go.getWidth() / 2), -(go.getHeight() / 2));
        g.drawImage(go.image(), 0, 0, null);
        g.setTransform(cameraTransform);
    }

    public AffineTransform getCameraTransform() {
        return cameraTransform;
    }

    @Override
    protected void onDrawBegin() {
        g.setTransform(cameraTransform);
    }

    private Rectangle getStringBounds(String str, float x, float y) {
        FontRenderContext frc = g.getFontRenderContext();
        GlyphVector gv = g.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, x, y);
    }

}
