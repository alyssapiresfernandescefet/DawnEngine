package com.dawnengine.network;

import java.util.function.Consumer;

public class NetworkPackets {

    //Responses
    public static final int SV_LOGIN_RES = 0xFFFFF001;
    public static final int SV_REGISTER_RES = 0xFFFFF002;
    public static final int SV_UPDATE_MAP_RES = 0xFFFFF005;
    public static final int SV_PLAYER_MOVE_MAP_RES = 0xFFFFF006;

    //Events
    public static final int SV_ENTITY_INSTANCE_EV = 0xFFFFF101;
    public static final int SV_ENTITY_DESTROY_EV = 0xFFFFF102;
    public static final int SV_PLAYER_MOVE_EV = 0xFFFFF103;
    public static final int SV_GET_MAP_EV = 0xFFFFF104;

    public enum Client {
        CL_LOGIN_REQ(0xCFFFF001, ctx -> {
            NetworkEvents.onClientLoginRequest(ctx);
        }),
        CL_REGISTER_REQ(0xCFFFF002, ctx -> {
            NetworkEvents.onClientRegisterRequest(ctx);
        }),
        CL_PLAYER_MOVE_EV(0xCFFFF003, ctx -> {
            NetworkEvents.onPlayerMove(ctx);
        }),
        CL_GET_MAP_REQ(0xCFFFF005, ctx -> {
            NetworkEvents.onGetMap(ctx);
        }),
        CL_UPDATE_MAP_REQ(0xCFFFF006, ctx -> {
            NetworkEvents.onSaveMap(ctx);
        }),
        CL_PLAYER_MOVE_MAP_REQ(0xCFFFF007, ctx -> {
            NetworkEvents.onPlayerMoveMap(ctx);
        }),;

        public final int code;
        public final Consumer<NetworkContext> event;

        Client(int code, Consumer<NetworkContext> event) {
            this.code = code;
            this.event = event;
        }

        public static Client get(int code) {
            for (Client value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
            throw new IllegalArgumentException(Integer.toString(code));
        }
    }
}
