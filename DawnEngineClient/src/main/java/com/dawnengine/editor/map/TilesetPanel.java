package com.dawnengine.editor.map;

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
import javax.swing.JPanel;

/**
 *
 * @author alyss
 */
public class TilesetPanel extends JPanel {

    private int mouseX, mouseY;
    private Tileset tileset;
    private int selectionStartX, selectionStartY, selectionEndX, selectionEndY;

    public TilesetPanel() {
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
                selectionEndX = Math.max(mouseX / Tile.SIZE_X, selectionStartX);
                selectionEndY = Math.max(mouseY / Tile.SIZE_Y, selectionStartY);
                repaint();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (tileset == null) {
                    return;
                }

                selectionStartX = mouseX / Tile.SIZE_X;
                selectionStartY = mouseY / Tile.SIZE_Y;
                selectionEndX = selectionStartX;
                selectionEndY = selectionStartY;
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

    public void setTileset(Tileset originalTileset) {
        this.tileset = originalTileset;
        this.setPreferredSize(new Dimension(originalTileset.getWidth(),
                originalTileset.getHeight()));
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics oldG) {
        super.paintComponent(oldG);
        Graphics2D g = (Graphics2D) oldG;
        g.setStroke(new BasicStroke(2));

        if (this.tileset != null) {
            g.drawImage(tileset.getImage(), 0, 0, null);
            g.setColor(Color.RED);
            g.drawRect(selectionStartX * Tile.SIZE_X,
                    selectionStartY * Tile.SIZE_Y,
                    (selectionEndX - selectionStartX + 1) * Tile.SIZE_X,
                    (selectionEndY - selectionStartY + 1) * Tile.SIZE_Y);
        }

        g.setColor(Color.BLUE);
        g.drawRect(mouseX / Tile.SIZE_X * Tile.SIZE_X,
                mouseY / Tile.SIZE_Y * Tile.SIZE_Y, Tile.SIZE_X, Tile.SIZE_Y);
    }

    public Tileset getTileset() {
        return tileset;
    }

    public Tile[][] getSelectedTiles() {
        int lengthX = selectionEndX - selectionStartX + 1;
        int lengthY = selectionEndY - selectionStartY + 1;

        var tiles = new Tile[lengthX][lengthY];

        for (int y = selectionStartY; y <= selectionEndY; y++) {
            for (int x = selectionStartX; x <= selectionEndX; x++) {
                tiles[x - selectionStartX][y - selectionStartY]
                        = new Tile(tileset, x, y);
            }
        }
        return tiles;
    }
}
