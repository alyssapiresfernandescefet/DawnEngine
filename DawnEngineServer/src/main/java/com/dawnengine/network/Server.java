package com.dawnengine.network;

import com.dawnengine.data.PlayerData;
import com.dawnengine.managers.PlayerManager;
import com.dawnengine.utils.Utils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class Server extends Listener {

    private static Server server;
    private com.esotericsoftware.kryonet.Server socket;

    private static final HashMap<Integer, PlayerData> players = new HashMap<>();

    private Server() throws IOException {
        socket = new com.esotericsoftware.kryonet.Server(65536, 8192);
        socket.bind(3001);
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
        PlayerData e = players.remove(connection.getID());
        if (e != null) {
            PlayerManager.save(e);
            var json = new JSONObject();
            json.put("code", ServerPackets.ENTITY_DESTROY);
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
                var obj = new JSONObject(str);
                if (!obj.has("code")) {
                    return;
                }
                var ctx = new NetworkContext(connection, obj);
                ClientPackets.get(obj.getInt("code")).event.accept(ctx);
            } else if (Utils.isJSONArray(str)) {
                var arr = new JSONArray(str);
                for (int i = 0; i < arr.length(); i++) {
                    var obj = arr.getJSONObject(i);
                    if (!obj.has("code")) {
                        continue;
                    }
                    var ctx = new NetworkContext(connection, obj);
                    ClientPackets.get(obj.getInt("code")).event.accept(ctx);
                }
            }
        }
    }

    public void sendToAll(String object) {
        socket.sendToAllTCP(object);
    }

    public void sendToAllExcept(Connection connection, String object) {
        socket.sendToAllExceptTCP(connection.getID(), object);
    }

    public void sendTo(Connection connection, String object) {
        socket.sendToTCP(connection.getID(), object);
    }
    
    public void sendToAllExcept(int connectionID, String object) {
        socket.sendToAllExceptTCP(connectionID, object);
    }

    public void sendTo(int connectionID, String object) {
        socket.sendToTCP(connectionID, object);
    }

    public void sendToMap(int mapIndex, String object) {
        Server.players.values().forEach(p -> {
            if (p.getMapIndex() == mapIndex) {
                sendTo(p.id(), object);
            }
        });
    }

    public static void open() {
        getServer().socket.start();
    }

    public static void close() {
        getServer().socket.stop();
        getServer().socket.close();
    }
    
    public static Collection<PlayerData> getAllPlayers() {
        return players.values();
    }
    
    public static PlayerData getPlayer(int id) {
        return players.get(id);
    }
    
    public static boolean addPlayer(PlayerData p) {
        return players.put(p.id(), p) != null;
    }
    
    public static boolean removePlayer(PlayerData p) {
        return players.remove(p.id(), p);
    }
    
    public static boolean removePlayer(int id) {
        return players.remove(id) != null;
    }
}
