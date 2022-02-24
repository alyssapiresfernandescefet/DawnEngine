package com.dawnengine.network;

import com.dawnengine.entity.Entity;
import com.dawnengine.game.Game;
import com.dawnengine.math.Vector2;

/**
 *
 * @author alyss
 */
public class ServerEventsImpl {

    static void onEntityDestroy(NetworkContext ctx) {
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
    }

    static void onGetMap(NetworkContext ctx) {
        Game.get().onGetMapEvent(ctx.response());
    }

    static void onPlayerMove(NetworkContext ctx) {
        var res = ctx.response();
        var entity = Game.findEntityByID(res.getInt("id"));
        if (entity != null) {
            entity.moveTo(new Vector2(res.getFloat("posX"),
                    res.getFloat("posY")), res.getFloat("speed"));
        }
    }

    static void onEntityInstance(NetworkContext ctx) {
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
    }

}
