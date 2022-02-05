package com.dawnengine.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class Server extends Listener {

    private static Server server;
    private com.esotericsoftware.kryonet.Server socket;

    private Server() throws IOException {
        socket = new com.esotericsoftware.kryonet.Server();
        socket.bind(3001, 3002);
        socket.addListener(this);
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("New connection: " + connection.getID());
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Connection lost: " + connection.getID());
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof String) {
            JSONObject obj = new JSONObject(object.toString());
            NetworkContext ctx = new NetworkContext(connection, obj);
            ClientNetworkPackets.get(obj.getInt("code")).event.run(ctx);
        }
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

    public static void open() {
        getServer().socket.start();
    }
}
