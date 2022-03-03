package com.dawnengine.network;

import com.dawnengine.entity.Entity;
import com.dawnengine.game.Game;
import com.dawnengine.game.map.Map;
import com.dawnengine.math.Vector2;
import com.dawnengine.serializers.MapSerializer;
import com.dawnengine.serializers.objects.MapData;

/**
 *
 * @author alyss
 */
public class ServerEvents {

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

            Game.get().removeEntity(obj.getInt("id"));
        }
    }

    static void onGetMap(NetworkContext ctx) {
        var res = ctx.response();
        var game = Game.get();

        var index = res.getInt("mapIndex");
        if (res.optBoolean("update")) {
            var mapData = new MapData(res.getString("name"), res.getInt("sizeX"),
                    res.getInt("sizeY"), res.getInt("moral"), res.getInt("lUp"),
                    res.getInt("lDown"), res.getInt("lRight"), res.getInt("lLeft"),
                    res.getLong("lastRevision"), res.getString("tiles"));
            game.setMap(new Map(index, mapData));
            MapSerializer.save(index, mapData);
        } else {
            game.setMap(new Map(index, MapSerializer.load(index)));
        }
        
        var player = game.getPlayer();
        var posX = res.optFloat("newPosX");
        var posY = res.optFloat("newPosY");
        if (!Float.isNaN(posX) && !Float.isNaN(posY)) {
            player.transform().position(posX, posY);
        }

        var arr = res.optJSONArray("entities");
        if (arr != null) {
            game.clearEntities();
            game.addEntity(player);
            for (int i = 0; i < arr.length(); i++) {
                var obj = arr.getJSONObject(i);
                var id = obj.getInt("id");
                var position = new Vector2(obj.getFloat("posX"), obj.getFloat("posY"));
                game.addEntity(new Entity(id, position));
            }
        }
    }

    static void onPlayerMove(NetworkContext ctx) {
        var res = ctx.response();
        var entity = Game.get().findEntityByID(res.getInt("id"));
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
            Game.get().addEntity(new Entity(obj.getInt("id"), pos));
        }
    }

}
