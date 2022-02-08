package com.dawnengine.network;

import com.dawnengine.core.MainFrame;
import com.dawnengine.game.Game;
import com.dawnengine.entity.Entity;
import com.dawnengine.math.Vector2;

public final class NetworkEvents {

    public static void onServerLoginResponse(NetworkContext ctx) {
        var json = ctx.json();
        MainFrame.getInstance().onLoginComplete(
                json.getBoolean("accept"),
                json.optString("reason"),
                json.optInt("playerID"));
    }

    public static void onServerRegisterResponse(NetworkContext ctx) {
        var json = ctx.json();
        MainFrame.getInstance().onRegisterComplete(
                json.getBoolean("accept"),
                json.optString("reason"),
                json.optInt("playerID"));
    }

    public static void updateTransform(NetworkContext ctx) {
        var json = ctx.json();
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
    }

    public static void instantiateNewEntity(NetworkContext ctx) {
        var json = ctx.json();
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

    public static void destroyEntity(NetworkContext ctx) {
        var json = ctx.json();
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
}
