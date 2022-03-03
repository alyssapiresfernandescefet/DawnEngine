package com.dawnengine.serializers;

import com.dawnengine.game.map.Tile;
import com.dawnengine.game.map.Tileset;
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
public class TilesetLoader {

    private static final File tilesetsDir
            = new File("data files/graphics/tilesets/");

    static {
        if (!tilesetsDir.exists()) {
            tilesetsDir.mkdirs();
        }
    }

    public static Tileset load(int num) {
        File[] tilesets = tilesetsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String string) {
                return string.startsWith(Integer.toString(num));
            }
        });

        if (tilesets.length == 0) {
            return new Tileset(num, new BufferedImage(Tile.SIZE_X, Tile.SIZE_Y, 
                    BufferedImage.TYPE_INT_RGB));
        }
        
        Tileset tileset = null;
        try {
            tileset = new Tileset(num, ImageIO.read(tilesets[0]));
        } catch (IOException ex) {
            Logger.getLogger(Tileset.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tileset;
    }

    public static File[] listAll() {
        return tilesetsDir.listFiles();
    }
}
