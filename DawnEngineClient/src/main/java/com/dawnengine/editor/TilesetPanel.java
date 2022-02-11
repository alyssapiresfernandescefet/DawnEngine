package com.dawnengine.editor;

import com.dawnengine.game.map.Tile;
import com.dawnengine.game.map.Tileset;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author alyss
 */
public class TilesetPanel extends JPanel {

    private int mouseX, mouseY;
    private Tileset tileset;
    private int selectionStart, selectionEnd;
    private MapEditor editor;

    public TilesetPanel(MapEditor editor) {
        this.editor = editor;
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                onMouseMoved(e);
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                onMouseMoved(e);
                if (tileset == null) {
                    return;
                }
                var x = mouseX / Tile.SIZE_X;
                var y = mouseY / Tile.SIZE_Y;
                selectionEnd = x + y * tileset.getTileCountX();
                repaint();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (tileset == null) {
                    return;
                }
                var x = mouseX / Tile.SIZE_X;
                var y = mouseY / Tile.SIZE_Y;
                selectionStart = x + y * tileset.getTileCountX();
                selectionEnd = selectionStart;
                repaint();
            }
        });
    }

    private void onMouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (tileset == null) {
            return;
        }
        if (mouseX >= tileset.getWidth()) {
            mouseX = tileset.getWidth() - Tile.SIZE_X;
        }
        if (mouseY >= tileset.getHeight()) {
            mouseY = tileset.getHeight() - Tile.SIZE_Y;
        }
    }

    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
        this.setPreferredSize(new Dimension(tileset.getTileset().getWidth(null),
                tileset.getTileset().getHeight(null)));
    }

    @Override
    protected void paintComponent(Graphics oldG) {
        super.paintComponent(oldG);
        Graphics2D g = (Graphics2D) oldG;
        g.setStroke(new BasicStroke(2));
        if (this.tileset != null) {
            g.drawImage(tileset.getTileset(), 0, 0, null);
            g.setColor(Color.RED);

            var startX = selectionStart % tileset.getTileCountX();
            var startY = selectionStart / tileset.getTileCountX();
            var endX = selectionEnd % tileset.getTileCountX();
            var endY = selectionEnd / tileset.getTileCountX();
            g.drawRect(startX * Tile.SIZE_X,
                    startY * Tile.SIZE_Y,
                    (endX - startX + 1) * Tile.SIZE_X,
                    (endY - startY + 1) * Tile.SIZE_Y);
        }

        g.setColor(Color.BLUE);
        g.drawRect(mouseX / Tile.SIZE_X * Tile.SIZE_X,
                mouseY / Tile.SIZE_Y * Tile.SIZE_Y, Tile.SIZE_X, Tile.SIZE_Y);
    }

    public BufferedImage getSelectedImage() {
        var startX = selectionStart % tileset.getTileCountX();
        var startY = selectionStart / tileset.getTileCountX();
        var endX = selectionEnd % tileset.getTileCountX();
        var endY = selectionEnd / tileset.getTileCountX();
        var x = startX * Tile.SIZE_X;
        var y = startY * Tile.SIZE_Y;
        var width = (endX - startX + 1) * Tile.SIZE_X;
        var height = (endY - startY + 1) * Tile.SIZE_Y;
        return tileset.getTileset().getSubimage(x, y, width, height);
    }

}
