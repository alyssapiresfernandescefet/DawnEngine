package com.dawnengine.network;

import com.dawnengine.core.Settings;
import com.dawnengine.network.NetworkPackets.ServerEventsMap;
import com.dawnengine.utils.Utils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class Client extends Listener {

    public static final int MAX_NETWORK_BANDWIDTH = 65536;

    private final HashMap<Long, Consumer<NetworkContext>> awaitingResponses = new HashMap<>();
    private static final Client client = new Client();

    private com.esotericsoftware.kryonet.Client socket;

    private Client() {
        socket = new com.esotericsoftware.kryonet.Client(MAX_NETWORK_BANDWIDTH,
                MAX_NETWORK_BANDWIDTH);
        socket.addListener(this);
        socket.start();
    }

    public static Client get() {
        return client;
    }

    public void openConnection() throws IOException {
        if (socket.isConnected()) {
            return;
        }
        socket.connect(5000,
                InetAddress.getByName(Settings.getProperty("server.ip")),
                Integer.parseInt(Settings.getProperty("server.tcpport")));
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
                acceptPacket(connection, obj);
            } else if (Utils.isJSONArray(str)) {
                var arr = new JSONArray(str);
                for (int i = 0; i < arr.length(); i++) {
                    var obj = arr.getJSONObject(i);
                    if (!obj.has("code")) {
                        continue;
                    }
                    acceptPacket(connection, obj);
                }
            }
        }
    }

    private void acceptPacket(Connection connection, JSONObject serverData) {
        var ctx = new NetworkContext(connection, serverData);
        var code = serverData.getInt("code");
        var requestTime = serverData.optLong("reqTime");
        if (requestTime == 0) {
            //It is an event.
            var packet = ServerEventsMap.get(code);
            if (packet != null) {
                packet.event.accept(ctx);
            }
        } else {
            //It is responding to a request.
            awaitingResponses.remove(requestTime).accept(ctx);
        }
    }

    public int sendSerialized(String serialized) {
        return socket.sendTCP(serialized);
    }

    public void sendPacket(int code, JSONObject obj) {
        if (obj == null) {
            return;
        }

        if (!obj.has("code")) {
            obj.put("code", code);
        }
        sendSerialized(obj.toString());
    }

    public void sendPacket(int code, JSONObject obj, Consumer<NetworkContext> onResponse) {
        long requestTime = new Date().getTime();

        awaitingResponses.put(requestTime, onResponse);

        obj.put("reqTime", requestTime);
        sendPacket(code, obj);

        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            awaitingResponses.remove(requestTime, onResponse);
        }, "Schedule GC for " + requestTime).start();
    }
}
