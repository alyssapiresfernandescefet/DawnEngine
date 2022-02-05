package com.dawnengine.network;

import com.esotericsoftware.kryonet.Connection;
import org.json.JSONObject;

public class NetworkContext {

    public final Connection connection;
    public final JSONObject json;

    public NetworkContext(Connection connection, JSONObject obj) {
        this.json = obj;
        this.connection = connection;
    }
}
