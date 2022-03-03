package com.dawnengine.network;

import java.util.function.Consumer;

public final class NetworkPackets {
    //CL: Client
    //REQ: Request
    //SV: Server
    //RES: Response
    //EV: Event
    //TR: Trigger

    public static final int CL_LOGIN_REQ = 0xCFFFF001;
    public static final int CL_REGISTER_REQ = 0xCFFFF002;
    public static final int CL_PLAYER_MOVE_EV = 0xCFFFF003;
    public static final int CL_GET_MAP_EV_TR = 0xCFFFF005;
    public static final int CL_UPDATE_MAP_REQ = 0xCFFFF006;
    public static final int CL_PLAYER_MOVE_MAP_REQ = 0xCFFFF007;

    public static final int SV_LOGIN_RES = 0xFFFFF001;
    public static final int SV_REGISTER_RES = 0xFFFFF002;
    public static final int SV_UPDATE_MAP_RES = 0xFFFFF005;
    public static final int SV_PLAYER_MOVE_MAP_RES = 0xFFFFF006;

    public static enum ServerEventsMap {
        SV_ENTITY_INSTANCE_EV(0xFFFFF101, ctx -> {
            ServerEvents.onEntityInstance(ctx);
        }),
        SV_ENTITY_DESTROY_EV(0xFFFFF102, ctx -> {
            ServerEvents.onEntityDestroy(ctx);
        }),
        SV_PLAYER_MOVE_EV(0xFFFFF103, ctx -> {
            ServerEvents.onPlayerMove(ctx);
        }),
        SV_GET_MAP_EV(0xFFFFF104, ctx -> {
            ServerEvents.onGetMap(ctx);
        });
        ;

        public final int code;
        public final Consumer<NetworkContext> event;

        ServerEventsMap(int code, Consumer<NetworkContext> event) {
            this.code = code;
            this.event = event;
        }

        public static ServerEventsMap get(int code) {
            for (ServerEventsMap value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
            return null;
        }
    }

}
