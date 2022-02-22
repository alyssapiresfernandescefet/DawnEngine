package com.dawnengine.network;

import com.dawnengine.entity.Entity;
import com.dawnengine.game.Game;
import com.dawnengine.math.Vector2;
import java.util.function.Consumer;

public final class NetworkPackets {

    public static final int CLIENT_LOGIN_REQUEST = 0xCFFFF001;
    public static final int CLIENT_REGISTER_REQUEST = 0xCFFFF002;
    public static final int CLIENT_TRANSFORM_UPDATE = 0xCFFFF003;
    public static final int CLIENT_CHECK_MAP_REQUEST = 0xCFFFF004;
    public static final int CLIENT_GET_MAP_REQUEST = 0xCFFFF005;
    public static final int CLIENT_UPDATE_MAP_REQUEST = 0xCFFFF006;

    public static final int SERVER_LOGIN_RESPONSE = 0xFFFFF001;
    public static final int SERVER_REGISTER_RESPONSE = 0xFFFFF002;
    public static final int SERVER_CHECK_MAP_RESPONSE = 0xFFFFF003;
    public static final int SERVER_GET_MAP_RESPONSE = 0xFFFFF004;
    public static final int SERVER_UPDATE_MAP_RESPONSE = 0xFFFFF005;

    public static enum ServerEvents {
        SERVER_ENTITY_INSTANCE_EVENT(0xFFFFF101, ctx -> {
            var json = ctx.response();
            var arr = json.getJSONArray("entities");

            if (arr == null) {
                return;
            }

            for (int i = 0; i < arr.length(); i++) {
                var obj = arr.getJSONObject(i);

                if (obj == null) {
                    return;
                }

                var pos = new Vector2(obj.getFloat("posX"), obj.getFloat("posY"));
                var scale = new Vector2(obj.getFloat("scaX"), obj.getFloat("scaY"));
                var rot = obj.getFloat("rot");
                Game.addEntity(new Entity(obj.getInt("id"), pos, scale, rot));
            }
        }),
        SERVER_ENTITY_DESTROY_EVENT(0xFFFFF102, ctx -> {
            var json = ctx.response();
            var arr = json.getJSONArray("entities");

            if (arr == null) {
                return;
            }

            for (int i = 0; i < arr.length(); i++) {
                var obj = arr.getJSONObject(i);

                if (obj == null) {
                    return;
                }

                Game.removeEntity(obj.getInt("id"));
            }
        }),
        SERVER_TRANSFORM_UPDATE_EVENT(0xFFFFF103, ctx -> {
            var json = ctx.response();
            var entity = Game.findEntityByID(json.getInt("id"));

            if (entity == null) {
                return;
            }

            var pos = new Vector2(json.getFloat("posX"), json.getFloat("posY"));
            var scale = new Vector2(json.getFloat("scaX"), json.getFloat("scaY"));
            var rot = json.getFloat("rot");
            entity.transform().position(pos);
            entity.transform().scale(scale);
            entity.transform().rotation(rot);
        }),
        SERVER_GET_MAP_EVENT(0xFFFFF104, ctx -> {
            Game.get().onGetMapEvent(ctx.response());
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
