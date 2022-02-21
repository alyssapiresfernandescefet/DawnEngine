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

    private Color ignoreColor = new Color(255, 0, 255);
    private int mouseX, mouseY;
    private Tileset originalTileset, editingTileset;
    private int selectionStart, selectionEnd;
    private MapEditor editor;

    public TilesetPanel() {
    }

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
                if (originalTileset == null) {
                    return;
                }
                var x = mouseX / Tile.SIZE_X;
                var y = mouseY / Tile.SIZE_Y;
                selectionEnd = Math.max(x + y * originalTileset.getTileCountX(),
                        selectionStart);
                repaint();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (originalTileset == null) {
                    return;
                }
                var x = mouseX / Tile.SIZE_X;
                var y = mouseY / Tile.SIZE_Y;
                selectionStart = x + y * originalTileset.getTileCountX();
                selectionEnd = selectionStart;
                repaint();
            }
        });
    }

    private void onMouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (originalTileset == null) {
            return;
        }
        if (mouseX >= originalTileset.getWidth()) {
            mouseX = originalTileset.getWidth() - Tile.SIZE_X;
        }
        if (mouseY >= originalTileset.getHeight()) {
            mouseY = originalTileset.getHeight() - Tile.SIZE_Y;
        }
    }

    public Tileset getTileset() {
        return editingTileset;
    }

    public void setTileset(Tileset originalTileset) {
        this.originalTileset = originalTileset;
        this.setPreferredSize(new Dimension(originalTileset.getWidth(),
                originalTileset.getHeight()));
        this.updateImage(ignoreColor);
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics oldG) {
        super.paintComponent(oldG);
        Graphics2D g = (Graphics2D) oldG;
        g.setStroke(new BasicStroke(2));

        if (this.originalTileset != null) {
            g.drawImage(originalTileset.getImage(), 0, 0, null);
            g.setColor(Color.RED);

            var startX = selectionStart % originalTileset.getTileCountX();
            var startY = selectionStart / originalTileset.getTileCountX();
            var endX = selectionEnd % originalTileset.getTileCountX();
            var endY = selectionEnd / originalTileset.getTileCountX();
            g.drawRect(startX * Tile.SIZE_X,
                    startY * Tile.SIZE_Y,
                    (endX - startX + 1) * Tile.SIZE_X,
                    (endY - startY + 1) * Tile.SIZE_Y);
        }

        g.setColor(Color.BLUE);
        g.drawRect(mouseX / Tile.SIZE_X * Tile.SIZE_X,
                mouseY / Tile.SIZE_Y * Tile.SIZE_Y, Tile.SIZE_X, Tile.SIZE_Y);
    }

    public BufferedImage[][] getSelectedTiles() {
        var startX = selectionStart % originalTileset.getTileCountX();
        var startY = selectionStart / originalTileset.getTileCountX();
        var endX = selectionEnd % originalTileset.getTileCountX();
        var endY = selectionEnd / originalTileset.getTileCountX();
        
        int lengthX = endX - startX + 1;
        int lengthY = endY - startY + 1;
        
        var images = new BufferedImage[lengthX][lengthY];
        
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                images[x - startX][y - startY] = editingTileset.getImageTile(x, y);
            }
        }
        return images;
    }

    public void updateImage(Color newIgnoreColor) {
        this.ignoreColor = newIgnoreColor;
        var image = originalTileset.getImage();
        var newImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                var p = image.getRGB(x, y);
                if (p != ignoreColor.getRGB()) {
                    newImage.setRGB(x, y, p);
                }
            }
        }

        editingTileset = new Tileset(originalTileset.getNum(), newImage);
    }

}
