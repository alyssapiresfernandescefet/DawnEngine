package com.dawnengine.network;

public enum ClientNetworkPackets {
    LOGIN_REQUEST(0xCFFFF001),
    REGISTER_REQUEST(0xCFFFF002),
    TRANSFORM_UPDATE(0xCFFFF003),
    ;

    public final int code;

    ClientNetworkPackets(int code) {
        this.code = code;
    }
}
