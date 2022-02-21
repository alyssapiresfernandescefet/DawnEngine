package com.dawnengine.network;

import com.dawnengine.data.MapData;
import com.dawnengine.data.PlayerData;
import com.dawnengine.managers.MapManager;
import com.dawnengine.managers.PlayerManager;
import com.dawnengine.utils.Encrypter;
import org.json.JSONArray;
import org.json.JSONObject;

public class NetworkEvents {

    public static void onClientLoginRequest(NetworkContext ctx) {
        String username = ctx.json().getString("username");
        String password = ctx.json().getString("password");

        String invalidReason = null;

        PlayerData player = PlayerManager.load(username);

        if (username.isBlank() || password.isBlank()) {
            invalidReason = "Username or password is blank!";
        } else if (player == null || !Encrypter.compare(password,
                player.getAccount().password)) {
            invalidReason = "Username or password is invalid!";
        }

        int playerID = ctx.connection().getID();
        if (player != null) {
            player.id(playerID);
        }
        var responseJSON = new JSONObject();

        responseJSON.put("code", ServerPacketType.LOGIN_RESPONSE.code);
        responseJSON.put("accept", invalidReason == null);
        if (invalidReason != null) {
            responseJSON.put("reason", invalidReason);
            ctx.connection().sendTCP(responseJSON.toString());
            return;
        }

        { //Telling all other players that a new player has arrived.
            var json = new JSONObject();
            json.put("code", ServerPacketType.ENTITY_INSTANCE.code);

            var array = new JSONArray();
            var obj = new JSONObject();
            obj.put("id", player.id());
            obj.put("posX", 0);
            obj.put("posY", 0);
            obj.put("scaX", 1);
            obj.put("scaY", 1);
            obj.put("rot", 0);
            array.put(obj);
            json.put("entities", array);

            Server.getServer().sendToMapTCP(player.getMapIndex(), json.toString());
        }

        { //Send to current player all of the data to initialize.
            var array = new JSONArray();
            for (var en : Server.getAllPlayers()) {
                var json = new JSONObject();
                json.put("id", en.id());
                json.put("posX", en.getPosX());
                json.put("posY", en.getPosY());
                json.put("scaX", en.getScaleX());
                json.put("scaY", en.getScaleY());
                json.put("rot", en.getRotation());
                array.put(json);
            }
            responseJSON.put("entities", array);
            responseJSON.put("playerID", playerID);
            responseJSON.put("mapIndex", player.getMapIndex());
            //Put more data here if necessary.

            Server.addPlayer(player);
            Server.getServer().sendToTCP(player.id(), responseJSON.toString());
        }
    }

    public static void onClientRegisterRequest(NetworkContext ctx) {
        String username = ctx.json().getString("username");
        String password = ctx.json().getString("password");

        String invalidReason = null;

        if (username.isBlank() || password.isBlank()) {
            invalidReason = "Username or password is blank!";
        } else if (PlayerManager.exists(username)) {
            invalidReason = "There is already another player with this username!";
        }

        JSONObject obj = new JSONObject();

        if (invalidReason == null) {
            int playerID = ctx.connection().getID();
            var pd = new PlayerData(playerID, username, Encrypter.encrypt(password));
            PlayerManager.save(pd);
        } else {
            obj.put("reason", invalidReason);
        }

        obj.put("code", ServerPacketType.REGISTER_RESPONSE.code);
        obj.put("accept", invalidReason == null);

        ctx.connection().sendTCP(obj.toString());
    }

    public static void onPlayerTransformUpdate(NetworkContext ctx) {
        var json = ctx.json();

        PlayerData pd = Server.getPlayer(ctx.connection().getID());
        pd.setPosX(json.getFloat("posX"));
        pd.setPosY(json.getFloat("posY"));
        pd.setScaleX(json.getFloat("scaX"));
        pd.setScaleY(json.getFloat("scaY"));
        pd.setRotation(json.getFloat("rot"));

        json.remove("code");
        json.put("code", ServerPacketType.TRANSFORM_UPDATE.code);
        Server.getServer().sendToAllExceptUDP(
                ctx.connection().getID(), json.toString());
    }

    public static void onCheckMap(NetworkContext ctx) {
        var index = ctx.json().getInt("mapIndex");
        var map = MapManager.load(index);
        Server.getServer().sendToTCP(ctx.connection().getID(),
                new JSONObject()
                        .put("code", ServerPacketType.CHECK_MAP_RESPONSE.code)
                        .put("lastRevision", map.getLastRevision())
                        .put("mapIndex", index).toString());
    }

    public static void onGetMap(NetworkContext ctx) {
        var index = ctx.json().getInt("mapIndex");
        MapData map = MapManager.load(index);
        var json = new JSONObject();
        json.put("code", ServerPacketType.GET_MAP_RESPONSE.code);

        json.put("name", map.getName());
        json.put("sizeX", map.getSizeX());
        json.put("sizeY", map.getSizeY());
        json.put("lastRevision", map.getLastRevision());
        json.put("tiles", map.getTiles());
        json.put("mapIndex", index);

        Server.getServer().sendToTCP(ctx.connection().getID(), json.toString());
    }
}
