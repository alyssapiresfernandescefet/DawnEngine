package com.dawnengine.network;

import com.dawnengine.core.Settings;
import com.dawnengine.network.NetworkPackets.ServerEvents;
import com.dawnengine.utils.Utils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class Client extends Listener {

    private final HashMap<ScheduledResponseHandler, Consumer<NetworkContext>> awaitingResponses = new HashMap<>();
    private static Client client;

    static {
        client = new Client();
    }

    private com.esotericsoftware.kryonet.Client socket;

    private Client() {
        socket = new com.esotericsoftware.kryonet.Client(65536, 65536);
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
            var packet = ServerEvents.get(code);
            packet.event.accept(ctx);
        } else {
            //It is responding to a request.
            awaitingResponses.remove(new ScheduledResponseHandler(
                    requestTime, code)).accept(ctx);
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

    public void sendPacket(int code, JSONObject obj,
            int intendedResponse, Consumer<NetworkContext> onResponse) {
        long time = new Date().getTime();

        var scheduled = new ScheduledResponseHandler(time, intendedResponse);
        awaitingResponses.put(scheduled, onResponse);
        obj.put("reqTime", time);

        var collectorThread = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            awaitingResponses.remove(scheduled, onResponse);
        }, "Schedule GC for " + time);
        sendPacket(code, obj);
        collectorThread.start();
    }

    private class ScheduledResponseHandler {

        public final long requestTime;
        public final int intendedResponse;

        public ScheduledResponseHandler(long requestTime, int intendedResponse) {
            this.requestTime = requestTime;
            this.intendedResponse = intendedResponse;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 41 * hash + (int) (this.requestTime ^ (this.requestTime >>> 32));
            hash = 41 * hash + Objects.hashCode(this.intendedResponse);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ScheduledResponseHandler other = (ScheduledResponseHandler) obj;
            if (this.requestTime != other.requestTime) {
                return false;
            }
            if (this.intendedResponse != other.intendedResponse) {
                return false;
            }
            return true;
        }
    }
}
