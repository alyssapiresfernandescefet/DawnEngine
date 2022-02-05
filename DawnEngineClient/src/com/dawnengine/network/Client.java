package com.dawnengine.network;

import com.dawnengine.core.Settings;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class Client extends Listener {

    private static Client client;
    private com.esotericsoftware.kryonet.Client socket;

    private Client() throws IOException {
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
        if (object instanceof String) {
            JSONObject obj = new JSONObject(object.toString());
            NetworkContext ctx = new NetworkContext(connection, obj);
            ServerNetworkPackets.get(obj.getInt("code")).event.run(ctx);
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
            try {
                client = new Client();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return client;
    }
}
