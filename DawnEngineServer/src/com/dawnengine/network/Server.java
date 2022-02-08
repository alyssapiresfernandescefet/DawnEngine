package com.dawnengine.network;

import com.dawnengine.model.NetworkEntity;
import com.dawnengine.utils.Utils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class Server extends Listener {

    private static Server server;
    private com.esotericsoftware.kryonet.Server socket;

    public static final HashMap<Integer, NetworkEntity> entities = new HashMap<>();

    private Server() throws IOException {
        socket = new com.esotericsoftware.kryonet.Server();
        socket.bind(3001, 3002);
        socket.addListener(this);
    }

    public static Server getServer() {
        if (server == null) {
            try {
                server = new Server();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return server;
    }

    @Override
    public void disconnected(Connection connection) {
        NetworkEntity e = entities.remove(connection.getID());
        if (e != null) {
            var json = new JSONObject();
            json.put("code", ServerPacket.ENTITY_DESTROY.code);
            var array = new JSONArray();
            var obj = new JSONObject();
            obj.put("id", e.id());
            array.put(obj);
            json.put("entities", array);
            socket.sendToAllTCP(json.toString());
        }
        //TODO: Remove player from UI player list if exists.
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof String str) {
            if (Utils.isJSONObject(str)) {
                var obj = new JSONObject(object.toString());
                var ctx = new NetworkContext(connection, obj);
                ClientPacket.get(obj.getInt("code")).event.accept(ctx);
            } else if (Utils.isJSONArray(str)) {
                var arr = new JSONArray(str);
                for (int i = 0; i < arr.length(); i++) {
                    var obj = arr.getJSONObject(i);
                    var ctx = new NetworkContext(connection, obj);
                    ClientPacket.get(obj.getInt("code")).event.accept(ctx);
                }
            }
        }
    }

    public void sendToAllTCP(String object) {
        socket.sendToAllTCP(object);
    }

    public void sendToAllExceptTCP(int connectionID, String object) {
        socket.sendToAllExceptTCP(connectionID, object);
    }

    public void sendToTCP(int connectionID, String object) {
        socket.sendToTCP(connectionID, object);
    }

    public void sendToAllUDP(String object) {
        socket.sendToAllUDP(object);
    }

    public void sendToAllExceptUDP(int connectionID, String object) {
        socket.sendToAllExceptUDP(connectionID, object);
    }

    public void sendToUDP(int connectionID, String object) {
        socket.sendToUDP(connectionID, object);
    }

    public static void open() {
        getServer().socket.start();
    }

    public static void close() {
        getServer().socket.stop();
        getServer().socket.close();
    }
}
