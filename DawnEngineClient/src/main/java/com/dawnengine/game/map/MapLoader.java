package com.dawnengine.game.map;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author alyss
 */
public class MapLoader {

    private static final File mapsDir = new File("data files/maps/");

    static {
        if (!mapsDir.exists()) {
            mapsDir.mkdirs();
        }
    }

    public static Map load(int index) {
        File[] maps = mapsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String string) {
                return string.equals("map" + index + ".map");
            }
        });
        return null;
    }
    
    public static void saveAsync(int mapIndex, Map map) {
        
    }
}
