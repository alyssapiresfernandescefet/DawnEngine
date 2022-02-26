package com.dawnengine.network;

import com.dawnengine.core.Server;
import com.esotericsoftware.kryonet.Connection;
import org.json.JSONObject;

public record NetworkContext(Connection connection, JSONObject request, Server server) {

}
