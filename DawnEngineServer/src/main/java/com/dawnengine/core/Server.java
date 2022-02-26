package com.dawnengine.core;

import com.dawnengine.data.PlayerData;
import com.dawnengine.network.NetworkContext;
import com.dawnengine.network.NetworkEvents;
import com.dawnengine.network.NetworkPackets;
import com.dawnengine.utils.Utils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class Server extends Listener {
    private com.esotericsoftware.kryonet.Server socket;

    private final HashMap<Integer, PlayerData> players = new HashMap<>();

    public Server() throws IOException {
        socket = new com.esotericsoftware.kryonet.Server(65536, 65536);
        socket.bind(3001);
        socket.addListener(this);
    }

    @Override
    public void disconnected(Connection connection) {
        PlayerData e = players.remove(connection.getID());
        if (e != null) {
            NetworkEvents.onPlayerDisconnect(e, this);
        }
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof String str) {
            if (Utils.isJSONObject(str)) {
                var obj = new JSONObject(str);
                if (!obj.has("code")) {
                    return;
                }
                var ctx = new NetworkContext(connection, obj, this);
                NetworkPackets.Client.get(obj.getInt("code")).event.accept(ctx);
            } else if (Utils.isJSONArray(str)) {
                var arr = new JSONArray(str);
                for (int i = 0; i < arr.length(); i++) {
                    var obj = arr.getJSONObject(i);
                    if (!obj.has("code")) {
                        continue;
                    }
                    var ctx = new NetworkContext(connection, obj, this);
                    NetworkPackets.Client.get(obj.getInt("code")).event.accept(ctx);
                }
            }
        }
    }

    public void sendToAll(JSONObject object) {
        socket.sendToAllTCP(object.toString());
    }

    public void sendToAllExcept(int connectionID, JSONObject object) {
        socket.sendToAllExceptTCP(connectionID, object.toString());
    }

    public void sendTo(int connectionID, JSONObject object) {
        socket.sendToTCP(connectionID, object.toString());
    }

    public void sendToMap(int mapIndex, JSONObject object) {
        players.values().forEach(p -> {
            if (p.getMapIndex() == mapIndex) {
                sendTo(p.getID(), object);
            }
        });
    }

    public void sendToMapExcept(int connectionID, int mapIndex, JSONObject object) {
        players.values().forEach(p -> {
            if (p.getMapIndex() == mapIndex && p.getID() != connectionID) {
                sendTo(p.getID(), object);
            }
        });
    }

    void open() {
        socket.start();
    }

    void close() {
        socket.stop();
        socket.close();
    }

    public Collection<PlayerData> getAllPlayers() {
        return players.values();
    }

    public PlayerData getPlayer(int id) {
        return players.get(id);
    }
    
    public PlayerData getPlayer(String username) {
        for (PlayerData pd : players.values()) {
            if (pd.getAccount().username.equals(username)) {
                return pd;
            }
        }
        return null;
    }

    public boolean addPlayer(PlayerData p) {
        return players.put(p.getID(), p) != null;
    }

    public boolean removePlayer(PlayerData p) {
        return players.remove(p.getID(), p);
    }
}
