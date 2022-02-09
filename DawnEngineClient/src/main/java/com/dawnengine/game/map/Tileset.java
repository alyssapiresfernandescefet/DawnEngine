package com.dawnengine.game.map;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author alyss
 */
public class Tileset {

    private static final File tilesetsDir
            = new File("data files/graphics/tilesets/");

    static {
        if (!tilesetsDir.exists()) {
            tilesetsDir.mkdirs();
        }
    }

    private BufferedImage tileset;

    public Tileset(BufferedImage tileset) {
        this.tileset = tileset;
    }

    public static Tileset load(int index) {
        File[] tilesets = tilesetsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String string) {
                return string.startsWith(Integer.toString(index));
            }
        });

        Tileset tileset = new Tileset(new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB));
        if (tilesets.length == 0) {
            return tileset;
        }

        try {
            tileset = new Tileset(ImageIO.read(tilesets[0]));
        } catch (IOException ex) {
            Logger.getLogger(Tileset.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tileset;
    }

    public Image getTileset() {
        return tileset;
    }

    public int getTileCountX() {
        return tileset.getWidth() / Tile.TILE_SIZE_X;
    }

    public int getTileCountY() {
        return tileset.getHeight() / Tile.TILE_SIZE_Y;
    }

    public Image getImageTile(int index) {
        return getImageTile(index % getTileCountX(), index / getTileCountX());
    }

    public Image getImageTile(int x, int y) {
        if (x < 0 || x >= getTileCountX() || y >= getTileCountY() || y < 0) {
            return null;
        }
        var img = tileset.getSubimage(
                Tile.TILE_SIZE_X * x,
                Tile.TILE_SIZE_Y * y,
                Tile.TILE_SIZE_X, Tile.TILE_SIZE_Y
        );
        return img;
    }

}
