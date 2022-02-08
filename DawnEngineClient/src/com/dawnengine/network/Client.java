package com.dawnengine.network;

import com.dawnengine.core.Settings;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.net.InetAddress;
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
            var obj = new JSONObject(str);
            var ctx = new NetworkContext(connection, obj);
            ServerPacketType.get(obj.getInt("code")).event.accept(ctx);
        }
    }

    public int sendTCP(String serialized) {
        return socket.sendTCP(serialized);
    }

    public int sendUDP(String serialized) {
        return socket.sendUDP(serialized);
    }

    public void sendPacket(ClientPacketType type, JSONObject obj) {
        obj.put("code", type.code);
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
