package com.dawnengine.network;

public enum ServerNetworkPackets {
    LOGIN_RESPONSE(0xFFFFF001, (ctx) -> {
        ClientNetworkEvents.onServerLoginResponse(ctx);
    }),
    REGISTER_RESPONSE(0xFFFFF002, (ctx) -> {
        ClientNetworkEvents.onServerRegisterResponse(ctx);
    }),;

    public final int code;
    public final NetworkEvent event;

    ServerNetworkPackets(int code, NetworkEvent event) {
        this.code = code;
        this.event = event;
    }

    public static ServerNetworkPackets get(int code) {
        for (ServerNetworkPackets value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException(Integer.toString(code));
    }
}
