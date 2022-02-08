package com.dawnengine.network;

import java.util.function.Consumer;

public enum ServerNetworkPackets {
    LOGIN_RESPONSE(0xFFFFF001, ctx -> {
        NetworkEvents.onServerLoginResponse(ctx);
    }),
    REGISTER_RESPONSE(0xFFFFF002, ctx -> {
        NetworkEvents.onServerRegisterResponse(ctx);
    }),
    ENTITY_INSTANCE(0xFFFFF003, ctx -> {
        NetworkEvents.instantiateNewEntity(ctx);
    }),
    TRANSFORM_UPDATE(0xFFFFF004, ctx -> {
        NetworkEvents.updateTransform(ctx);
    }),;

    public final int code;
    public final Consumer<NetworkContext> event;

    ServerNetworkPackets(int code, Consumer<NetworkContext> event) {
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
