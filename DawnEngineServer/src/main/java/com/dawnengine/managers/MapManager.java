package com.dawnengine.managers;

import com.dawnengine.data.Map;
import java.io.File;

/**
 *
 * @author alyss
 */
public class MapManager {
private static final File mapsDir = new File("data/maps/");

    static {
        if (!mapsDir.exists()) {
            mapsDir.mkdirs();
        }
    }
    
    public static Map load(int index) {
        return null;
    }
    
    public static void save(Map map) {
        
    }
}
