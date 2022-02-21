package com.dawnengine.network;

import com.dawnengine.core.GameFrame;
import com.dawnengine.core.MainFrame;
import com.dawnengine.game.Game;
import com.dawnengine.entity.Entity;
import com.dawnengine.math.Vector2;
import javax.swing.JOptionPane;

public final class NetworkEvents {

    public static void onServerLoginResponse(NetworkContext ctx) {
        MainFrame.getInstance().onLoginComplete(ctx.json());
    }

    public static void onServerRegisterResponse(NetworkContext ctx) {
        var json = ctx.json();
        MainFrame.getInstance().onRegisterComplete(
                json.getBoolean("accept"),
                json.optString("reason"));
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

    public static void onCheckMapResponse(NetworkContext ctx) {
        var json = ctx.json();
        GameFrame.get().getGame().onCheckMapReceived(json.getInt("mapIndex"),
                json.getLong("lastRevision"));
    }

    public static void onGetMapResponse(NetworkContext ctx) {
        GameFrame.get().getGame().onGetMapReceived(ctx.json());
    }

    public static void onUpdateMapResponse(NetworkContext ctx) {
        var accept = ctx.json().getBoolean("accept");
        if (!accept) {
            var message = ctx.json().getString("message");
            JOptionPane.showMessageDialog(null,
                    "The operation could not be performed. Reason: " + message,
                    "Update Map Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
