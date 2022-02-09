package com.dawnengine.game.map;

import com.dawnengine.utils.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        Map map = null;
        if (maps.length == 0) {
            return map;
        }

        try (var in = new Input(new FileInputStream(maps[0]))) {
            map = Serializer.readObject(in, Map.class);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MapLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return map;
    }

    public static boolean save(int mapIndex, Map map) {
        File f = new File(mapsDir, "map" + mapIndex + ".map");
        try (var out = new Output(new FileOutputStream(f))) {
            Serializer.writeObject(out, map);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MapLoader.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
