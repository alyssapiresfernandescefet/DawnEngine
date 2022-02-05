package com.dawnengine.network;

import org.json.JSONObject;

public class ServerNetworkEvents {

    public static void onClientLoginRequest(NetworkContext ctx) {
        String username = ctx.json.getString("username");
        String password = ctx.json.getString("password");
        JSONObject obj = new JSONObject();
        obj.put("code", ServerNetworkPackets.LOGIN_RESPONSE.code);
        obj.put("accept", true);
        obj.put("playerID", ctx.connection.getID());
        ctx.connection.sendTCP(obj.toString());
    }

    public static void onClientRegisterRequest(NetworkContext ctx) {

    }
}
