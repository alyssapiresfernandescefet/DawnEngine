package com.dawnengine.network;

public enum ServerPacket {
    LOGIN_RESPONSE(0xFFFFF001),
    REGISTER_RESPONSE(0xFFFFF002),
    ENTITY_INSTANCE(0xFFFFF003),
    ENTITY_DESTROY(0xFFFFF004),
    TRANSFORM_UPDATE(0xFFFFF005),
    GAME_READY_RESPONSE(0xFFFFF006),
    ;

    public final int code;

    ServerPacket(int code) {
        this.code = code;
    }
}
