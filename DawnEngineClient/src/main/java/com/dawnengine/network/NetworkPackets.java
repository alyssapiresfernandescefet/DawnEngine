package com.dawnengine.network;

import java.util.function.Consumer;

public final class NetworkPackets {

    public static final int CLIENT_LOGIN_REQUEST = 0xCFFFF001;
    public static final int CLIENT_REGISTER_REQUEST = 0xCFFFF002;
    public static final int CLIENT_PLAYER_MOVE = 0xCFFFF003;
    public static final int CLIENT_GET_MAP_REQUEST = 0xCFFFF005;
    public static final int CLIENT_UPDATE_MAP_REQUEST = 0xCFFFF006;
    public static final int CL_PLAYER_MOVE_MAP_REQ = 0xCFFFF007;

    public static final int SERVER_LOGIN_RESPONSE = 0xFFFFF001;
    public static final int SERVER_REGISTER_RESPONSE = 0xFFFFF002;
    public static final int SERVER_UPDATE_MAP_RESPONSE = 0xFFFFF005;
    public static final int SV_PLAYER_MOVE_MAP_RES = 0xFFFFF006;

    public static enum ServerEvents {
        SERVER_ENTITY_INSTANCE_EVENT(0xFFFFF101, ctx -> {
            ServerEventsImpl.onEntityInstance(ctx);
        }),
        SERVER_ENTITY_DESTROY_EVENT(0xFFFFF102, ctx -> {
            ServerEventsImpl.onEntityDestroy(ctx);
        }),
        SERVER_PLAYER_MOVE_EVENT(0xFFFFF103, ctx -> {
            ServerEventsImpl.onPlayerMove(ctx);
        }),
        SERVER_GET_MAP_EVENT(0xFFFFF104, ctx -> {
            ServerEventsImpl.onGetMap(ctx);
        });
        ;

        public final int code;
        public final Consumer<NetworkContext> event;

        ServerEvents(int code, Consumer<NetworkContext> event) {
            this.code = code;
            this.event = event;
        }

        public static ServerEvents get(int code) {
            for (ServerEvents value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
            throw new IllegalArgumentException(Integer.toString(code));
        }
    }

}
