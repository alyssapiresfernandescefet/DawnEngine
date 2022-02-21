package com.dawnengine.network;

import java.util.function.Consumer;

public enum ServerPackets {
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
    CHECK_MAP_RESPONSE(0xFFFFF006, ctx -> {
        NetworkEvents.onCheckMapResponse(ctx);
    }),
    GET_MAP_RESPONSE(0xFFFFF007, ctx -> {
        NetworkEvents.onGetMapResponse(ctx);
    }),
    UPDATE_MAP_RESPONSE(0xFFFFF008, ctx -> {
        NetworkEvents.onUpdateMapResponse(ctx);
    }),
    ;

    public final int code;
    public final Consumer<NetworkContext> event;

    ServerPackets(int code, Consumer<NetworkContext> event) {
        this.code = code;
        this.event = event;
    }

    public static ServerPackets get(int code) {
        for (ServerPackets value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException(Integer.toString(code));
    }
}
