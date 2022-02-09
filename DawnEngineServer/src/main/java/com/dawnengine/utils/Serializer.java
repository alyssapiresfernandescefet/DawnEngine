package com.dawnengine.utils;

import com.dawnengine.data.Account;
import com.dawnengine.data.MapData;
import com.dawnengine.data.PlayerData;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 *
 * @author alyss
 */
public class Serializer {
    private static final Kryo kryo = new Kryo();
    static {
        kryo.register(Account.class);
        kryo.register(PlayerData.class);
        kryo.register(MapData.class);
    }

    public static void writeObject(Output output, Object object) {
        kryo.writeObject(output, object);
    }

    public static <T> T readObject(Input input, Class<T> type) {
        return kryo.readObject(input, type);
    }
    
}
