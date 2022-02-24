package com.dawnengine.entity;

import com.dawnengine.math.Vector2;
import com.dawnengine.network.Client;
import java.util.HashMap;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alyss
 */
public class NetworkEntity extends Entity {

    private final HashMap<Integer, JSONObject> queuedPackets = new HashMap<>();

    public NetworkEntity(int id, Vector2 position) {
        super(id, position);
    }

    public void invalidate(int code, JSONObject request) {
        if (!request.has("code")) {
            request.put("code", code);
        }
        queuedPackets.put(code, request);
    }

    @Override
    public void networkUpdate() {
        if (queuedPackets.isEmpty()) {
            return;
        }
        
        var arr = new JSONArray();
        for (JSONObject req : queuedPackets.values()) {
            arr.put(req);
        }
        Client.getClient().sendSerialized(arr.toString());
        queuedPackets.clear();
    }
}
