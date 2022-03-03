package com.dawnengine.serializers;

import java.io.File;
import java.io.FilenameFilter;
import com.dawnengine.game.graphics.Character;
import com.dawnengine.game.map.Tileset;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author alyss
 */
public class CharacterLoader {

    private static final File charsDir = new File("data files/graphics/characters/");

    static {
        if (!charsDir.exists()) {
            charsDir.mkdirs();
        }
    }

    public static Character load(int num) {
        File[] characters = charsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String string) {
                return string.startsWith(Integer.toString(num));
            }
        });

        if (characters.length == 0) {
            int sizeX = 100, sizeY = 100;
            return new Character(new BufferedImage(sizeX, sizeY,
                    BufferedImage.TYPE_INT_RGB), sizeX, sizeY);
        }
        
        Character c = null;
        try {
            var img = ImageIO.read(characters[0]);
//            c = new Character(img);
        } catch (IOException ex) {
            Logger.getLogger(CharacterLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }
}
