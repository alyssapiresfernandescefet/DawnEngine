package com.dawnengine.network;

import com.dawnengine.core.MainFrame;

public final class ClientNetworkEvents {

    public static void onServerLoginResponse(NetworkContext ctx) {
        var json = ctx.json();
        MainFrame.getInstance().onLoginComplete(
                json.getBoolean("accept"),
                json.optString("reason"),
                json.optInt("playerID"));
    }

    public static void onServerRegisterResponse(NetworkContext ctx) {
        var json = ctx.json();
        MainFrame.getInstance().onRegisterComplete(
                json.getBoolean("accept"),
                json.optString("reason"),
                json.optInt("playerID"));
    }
}
