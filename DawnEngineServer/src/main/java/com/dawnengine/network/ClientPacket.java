package com.dawnengine.network;

import java.util.function.Consumer;

public enum ClientPacket {

    LOGIN_REQUEST(0xCFFFF001, ctx -> {
        NetworkEvents.onClientLoginRequest(ctx);
    }),
    REGISTER_REQUEST(0xCFFFF002, ctx -> {
        NetworkEvents.onClientRegisterRequest(ctx);
    }),
    TRANSFORM_UPDATE(0xCFFFF003, ctx -> {
        NetworkEvents.onPlayerTransformUpdate(ctx);
    }),
    GAME_READY(0xCFFFF004, ctx -> {
        NetworkEvents.onNewPlayer(ctx);
    }),
    ;

    public final int code;
    public final Consumer<NetworkContext> event;

    ClientPacket(int code, Consumer<NetworkContext> event) {
        this.code = code;
        this.event = event;
    }

    public static ClientPacket get(int code) {
        for (ClientPacket value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException(Integer.toString(code));
    }
}
