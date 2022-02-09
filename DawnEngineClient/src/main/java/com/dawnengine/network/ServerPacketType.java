package com.dawnengine.network;

import java.util.function.Consumer;

public enum ServerPacketType {
    LOGIN_RESPONSE(0xFFFFF001, ctx -> {
        NetworkEvents.onServerLoginResponse(ctx);
    }),
    REGISTER_RESPONSE(0xFFFFF002, ctx -> {
        NetworkEvents.onServerRegisterResponse(ctx);
    }),
    ENTITY_INSTANCE(0xFFFFF003, ctx -> {
        NetworkEvents.instantiateNewEntity(ctx);
    }),
    ENTITY_DESTROY(0xFFFFF004, ctx -> {
        NetworkEvents.destroyEntity(ctx);
    }),
    TRANSFORM_UPDATE(0xFFFFF005, ctx -> {
        NetworkEvents.updateTransform(ctx);
    }),
    GAME_READY_RESPONSE(0xFFFFF006, ctx -> {
        NetworkEvents.onGameReady(ctx);
    }),
    ;

    public final int code;
    public final Consumer<NetworkContext> event;

    ServerPacketType(int code, Consumer<NetworkContext> event) {
        this.code = code;
        this.event = event;
    }

    public static ServerPacketType get(int code) {
        for (ServerPacketType value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException(Integer.toString(code));
    }
}
