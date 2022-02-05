package com.dawnengine.network;

import javax.swing.JOptionPane;

public final class ClientNetworkEvents {

    public static void onServerLoginResponse(NetworkContext ctx) {
        JOptionPane.showMessageDialog(null, "Accept: "
                + Boolean.toString(ctx.json.getBoolean("accept")));
    }

    public static void onServerRegisterResponse(NetworkContext ctx) {

    }
}
