package com.dawnengine.network;

public enum ServerPacketType {
    LOGIN_RESPONSE(0xFFFFF001),
    REGISTER_RESPONSE(0xFFFFF002),
    ENTITY_INSTANCE(0xFFFFF003),
    ENTITY_DESTROY(0xFFFFF004),
    TRANSFORM_UPDATE(0xFFFFF005),
    CHECK_MAP_RESPONSE(0xFFFFF006),
    GET_MAP_RESPONSE(0xFFFFF007),
    ;

    public final int code;

    ServerPacketType(int code) {
        this.code = code;
    }
}
