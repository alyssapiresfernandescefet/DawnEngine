package com.dawnengine.entity;

import com.dawnengine.math.Vector2;
import com.dawnengine.network.Client;
import com.dawnengine.network.NetworkContext;
import java.util.HashMap;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alyss
 */
public class NetworkEntity extends Entity {

    private final HashMap<Integer, Request> queuedPackets = new HashMap<>();

    public NetworkEntity(int id, Vector2 position) {
        super(id, position);
    }

    public void invalidate(int code, JSONObject request) {
        if (!request.has("code")) {
            request.put("code", code);
        }
        queuedPackets.put(code, new Request(code, request));
    }

    public void invalidate(int code, JSONObject request, Consumer<NetworkContext> onResponse) {
        if (!request.has("code")) {
            request.put("code", code);
        }
        queuedPackets.put(code, new Request(code, request, onResponse));
    }

    @Override
    public void networkUpdate() {
        if (queuedPackets.isEmpty()) {
            return;
        }

        JSONArray arr = new JSONArray();
        for (Request req : queuedPackets.values()) {
            if (req.onResponse != null) {
                Client.getClient().sendPacket(req.requestCode, req.data, req.onResponse);
            } else {
                arr.put(req.data);
            }
        }
        queuedPackets.clear();
        Client.getClient().sendSerialized(arr.toString());
    }

    private class Request {

        public final int requestCode;
        public final JSONObject data;
        public final Consumer<NetworkContext> onResponse;

        public Request(int requestCode, JSONObject data) {
            this.requestCode = requestCode;
            this.data = data;
            this.onResponse = null;
        }

        public Request(int requestCode, JSONObject data,
                Consumer<NetworkContext> onResponse) {
            this.requestCode = requestCode;
            this.data = data;
            this.onResponse = onResponse;
        }
    }
}
