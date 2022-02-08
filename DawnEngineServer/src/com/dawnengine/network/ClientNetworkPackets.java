package com.dawnengine.network;

import java.util.function.Consumer;

public enum ClientNetworkPackets {

    LOGIN_REQUEST(0xCFFFF001, (ctx) -> {
        NetworkEvents.onClientLoginRequest(ctx);
    }),
    REGISTER_REQUEST(0xCFFFF002, (ctx) -> {
        NetworkEvents.onClientRegisterRequest(ctx);
    }),
    TRANSFORM_UPDATE(0xCFFFF003, (ctx) -> {
        NetworkEvents.onPlayerTransformUpdate(ctx);
    }),
    ;

    public final int code;
    public final Consumer<NetworkContext> event;

    ClientNetworkPackets(int code, Consumer<NetworkContext> event) {
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
