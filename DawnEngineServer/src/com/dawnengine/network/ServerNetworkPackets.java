package com.dawnengine.network;

public enum ServerNetworkPackets {
    LOGIN_RESPONSE(0xFFFFF001),
    REGISTER_RESPONSE(0xFFFFF002),
    ENTITY_INSTANCE(0xFFFFF003),
    TRANSFORM_UPDATE(0xFFFFF004),
    ;

    public final int code;

    ServerNetworkPackets(int code) {
        this.code = code;
    }
}
