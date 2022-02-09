package com.dawnengine.utils;

import com.dawnengine.game.map.Map;
import com.dawnengine.game.map.Tile;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.awt.Image;

/**
 *
 * @author alyss
 */
public class Serializer {
    private static final Kryo kryo = new Kryo();
    static {
        kryo.register(Map.class);
        kryo.register(Tile.class);
    }

    public static void writeObject(Output output, Object object) {
        kryo.writeObject(output, object);
    }

    public static <T> T readObject(Input input, Class<T> type) {
        return kryo.readObject(input, type);
    }
    
}
