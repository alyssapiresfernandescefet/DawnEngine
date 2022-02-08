package com.dawnengine.network;

public enum ClientPacketType {
    LOGIN_REQUEST(0xCFFFF001, "TCP"),
    REGISTER_REQUEST(0xCFFFF002, "TCP"),
    TRANSFORM_UPDATE(0xCFFFF003, "UDP"),
    ;

    public final int code;
    public final String route;

    ClientPacketType(int code, String route) {
        this.code = code;
        this.route = route;
    }
}
