package com.dawnengine.network;

import com.dawnengine.core.Settings;
import com.dawnengine.utils.JSON;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.net.InetAddress;
import org.json.JSONObject;

public class Client extends Listener {

    private static Client client;
    private com.esotericsoftware.kryonet.Client socket;

    private Client() {
        socket = new com.esotericsoftware.kryonet.Client();
        socket.addListener(this);
        socket.start();
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
            ServerNetworkPackets.get(obj.getInt("code")).event.invoke(ctx);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Discconnected from" + connection.getID());
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Successfully connected to " + connection.getID());
    }

    public com.esotericsoftware.kryonet.Client getSocket() {
        return socket;
    }

    public static Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public static String createPacket(ClientNetworkPackets packet,
            JSON... data) {
        var json = new JSONObject();
        json.put("code", packet.code);
        for (JSON dat : data) {
            json.put(dat.key(), dat.value());
        }
        return json.toString();
    }
}
