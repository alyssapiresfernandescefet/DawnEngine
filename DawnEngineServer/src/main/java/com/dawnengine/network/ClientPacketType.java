package com.dawnengine.network;

import java.util.function.Consumer;

public enum ClientPacketType {

    LOGIN_REQUEST(0xCFFFF001, ctx -> {
        NetworkEvents.onClientLoginRequest(ctx);
    }),
    REGISTER_REQUEST(0xCFFFF002, ctx -> {
        NetworkEvents.onClientRegisterRequest(ctx);
    }),
    TRANSFORM_UPDATE(0xCFFFF003, ctx -> {
        NetworkEvents.onPlayerTransformUpdate(ctx);
    }),
    CHECK_MAP_REQUEST(0xCFFFF004, ctx -> {
        NetworkEvents.onCheckMap(ctx);
    }),
    GET_MAP_REQUEST(0xCFFFF005, ctx -> {
        NetworkEvents.onGetMap(ctx);
    }),
    ;
    

    public final int code;
    public final Consumer<NetworkContext> event;

    ClientPacketType(int code, Consumer<NetworkContext> event) {
        this.code = code;
        this.event = event;
    }

    public static ClientPacketType get(int code) {
        for (ClientPacketType value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException(Integer.toString(code));
    }
}
