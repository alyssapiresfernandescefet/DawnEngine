package com.dawnengine.managers;

import com.dawnengine.data.MapData;
import com.dawnengine.utils.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alyss
 */
public class MapManager {

    private static final int DEF_MAP_WIDTH = 30, DEF_MAP_HEIGHT = 15;

    private static final File mapsDir = new File("data/maps/");

    static {
        if (!mapsDir.exists()) {
            mapsDir.mkdirs();
        }
    }

    public static MapData load(int index) {
        File[] maps = mapsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String string) {
                return string.equals("map" + index + ".map");
            }
        });

        if (maps.length == 0) {
            MapData map = new MapData("Map " + index, DEF_MAP_WIDTH, DEF_MAP_HEIGHT);
            save(index, map);
            return map;
        }
        
        MapData map = null;
        try (var in = new Input(new FileInputStream(maps[0]))) {
            map = Serializer.readObject(in, MapData.class);
        } catch (Exception ex) {
            Logger.getLogger(MapManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    public static boolean save(int index, MapData map) {
        File mapFile = new File(mapsDir, "map" + index + ".map");

        try (var out = new Output(new FileOutputStream(mapFile))) {
            Serializer.writeObject(out, map);
        } catch (IOException ex) {
            Logger.getLogger(PlayerManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
