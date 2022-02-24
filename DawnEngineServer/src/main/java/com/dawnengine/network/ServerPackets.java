package com.dawnengine.network;

public final class ServerPackets {
    //Responses
    public static final int SERVER_LOGIN_RESPONSE = 0xFFFFF001;
    public static final int SERVER_REGISTER_RESPONSE = 0xFFFFF002;
    public static final int SERVER_CHECK_MAP_RESPONSE = 0xFFFFF003;
    public static final int SERVER_GET_MAP_RESPONSE = 0xFFFFF004;
    public static final int SERVER_UPDATE_MAP_RESPONSE = 0xFFFFF005;
    
    //Events
    public static final int SERVER_ENTITY_INSTANCE_EVENT = 0xFFFFF101;
    public static final int SERVER_ENTITY_DESTROY_EVENT = 0xFFFFF102;
    public static final int SERVER_PLAYER_MOVE_EVENT = 0xFFFFF103;
    public static final int SERVER_GET_MAP_EVENT = 0xFFFFF104;
    
}
