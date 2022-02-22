package com.dawnengine.network;

import java.util.function.Consumer;

public enum ClientPackets {

    CLIENT_LOGIN_REQUEST(0xCFFFF001, ctx -> {
        NetworkEvents.onClientLoginRequest(ctx);
    }),
    CLIENT_REGISTER_REQUEST(0xCFFFF002, ctx -> {
        NetworkEvents.onClientRegisterRequest(ctx);
    }),
    CLIENT_TRANSFORM_UPDATE(0xCFFFF003, ctx -> {
        NetworkEvents.onPlayerTransformUpdate(ctx);
    }),
    CLIENT_CHECK_MAP_REQUEST(0xCFFFF004, ctx -> {
        NetworkEvents.onCheckMap(ctx);
    }),
    CLIENT_GET_MAP_REQUEST(0xCFFFF005, ctx -> {
        NetworkEvents.onGetMap(ctx);
    }),
    CLIENT_UPDATE_MAP_REQUEST(0xCFFFF006, ctx -> {
        NetworkEvents.onSaveMap(ctx);
    }),
    ;

    public final int code;
    public final Consumer<NetworkContext> event;

    ClientPackets(int code, Consumer<NetworkContext> event) {
        this.code = code;
        this.event = event;
    }

    public static ClientPackets get(int code) {
        for (ClientPackets value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException(Integer.toString(code));
    }
}
