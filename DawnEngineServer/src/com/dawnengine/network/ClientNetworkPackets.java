package com.dawnengine.network;

public enum ClientNetworkPackets {

    LOGIN_REQUEST(0xCFFFF001, (ctx) -> {
        ServerNetworkEvents.onClientLoginRequest(ctx);
    }),
    REGISTER_REQUEST(0xCFFFF002, (ctx) -> {
        ServerNetworkEvents.onClientRegisterRequest(ctx);
    }),;

    public final int code;
    public final NetworkEvent event;

    ClientNetworkPackets(int code, NetworkEvent event) {
        this.code = code;
        this.event = event;
    }

    public static ClientNetworkPackets get(int code) {
        for (ClientNetworkPackets value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException(Integer.toString(code));
    }
}
