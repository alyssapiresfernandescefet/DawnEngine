package com.dawnengine.network;

import com.dawnengine.core.Server;
import com.esotericsoftware.kryonet.Connection;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alyss
 */
public class NetworkUtils {

    public static final int DIR_UP = 0, DIR_DOWN = 1, DIR_RIGHT = 2, DIR_LEFT = 3;

    public static JSONArray getPlayerEntity(Connection con) {
        return getPlayerEntity(con.getID());
    }

    public static JSONArray getPlayerEntity(int connectionID) {
        var arr = new JSONArray();
        var obj = new JSONObject();
        obj.put("id", connectionID);
        arr.put(obj);
        return arr;
    }

    public static JSONArray getPlayerEntity(Connection con, float posX, float posY) {
        var arr = new JSONArray();
        var obj = new JSONObject();
        obj.put("id", con.getID());
        obj.put("posX", posX);
        obj.put("posY", posY);
        arr.put(obj);
        return arr;
    }

    public static JSONArray getEntitiesOnMap(int mapIndex, Server server) {
        var arr = new JSONArray();
        for (var en : server.getAllPlayers()) {
            if (en.getMapIndex() == mapIndex) {
                var obj = new JSONObject();
                obj.put("id", en.getID());
                obj.put("posX", en.getPosX());
                obj.put("posY", en.getPosY());
                arr.put(obj);
            }
        }
        return arr;
    }

    public static JSONArray getEntitiesOnMapExcept(Connection con, int mapIndex, Server server) {
        var arr = new JSONArray();
        for (var en : server.getAllPlayers()) {
            if (en.getID() == con.getID()
                    || en.getMapIndex() != mapIndex) {
                continue;
            }
            var obj = new JSONObject();
            obj.put("id", en.getID());
            obj.put("posX", en.getPosX());
            obj.put("posY", en.getPosY());
            arr.put(obj);
        }
        return arr;
    }
}
