package com.dawnengine.network;

import com.dawnengine.core.Settings;
import com.dawnengine.utils.Utils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.net.InetAddress;
import org.json.JSONArray;
import org.json.JSONObject;

public class Client extends Listener {

    private static Client client;

    static {
        client = new Client();
    }

    private com.esotericsoftware.kryonet.Client socket;

    private Client() {
        socket = new com.esotericsoftware.kryonet.Client();
        socket.addListener(this);
        socket.start();
    }

    public static Client getClient() {
        return client;
    }

    public void openConnection() throws IOException {
        if (socket.isConnected()) {
            return;
        }
        socket.connect(5000,
                InetAddress.getByName(Settings.getProperty("server.ip")),
                Integer.parseInt(Settings.getProperty("server.tcpport")),
                Integer.parseInt(Settings.getProperty("server.udpport")));
    }

    public void closeConnection() {
        socket.close();
        socket.stop();
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
                ServerPacketType.get(obj.getInt("code")).event.accept(ctx);
            } else if (Utils.isJSONArray(str)) {
                var arr = new JSONArray(str);
                for (int i = 0; i < arr.length(); i++) {
                    var obj = arr.getJSONObject(i);
                    if (!obj.has("code")) {
                        continue;
                    }
                    var ctx = new NetworkContext(connection, obj);
                    ServerPacketType.get(obj.getInt("code")).event.accept(ctx);
                }
            }
        }
    }

    public int sendTCP(String serialized) {
        return socket.sendTCP(serialized);
    }

    public int sendUDP(String serialized) {
        return socket.sendUDP(serialized);
    }

    public void sendPacket(ClientPacketType type, JSONObject obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        if (!obj.has("code")) {
            obj.put("code", type.code);
        }
        switch (type.route) {
            case "TCP":
                sendTCP(obj.toString());
                break;
            case "UDP":
                sendUDP(obj.toString());
                break;
        }
    }
}
