package com.dawnengine.network;

import com.dawnengine.data.PlayerData;
import com.dawnengine.managers.PlayerManager;
import com.dawnengine.utils.Encrypter;
import org.json.JSONArray;
import org.json.JSONObject;

public class NetworkEvents {

    public static void onClientLoginRequest(NetworkContext ctx) {
        String username = ctx.json.getString("username");
        String password = ctx.json.getString("password");

        String invalidReason = null;

        PlayerData pd = PlayerManager.load(username);

        if (username.isBlank() || password.isBlank()) {
            invalidReason = "Username or password is blank!";
        } else if (pd == null || !Encrypter.compare(password,
                pd.getAccount().password)) {
            invalidReason = "Username or password is invalid!";
        }

        int playerID = ctx.connection.getID();
        JSONObject obj = new JSONObject();

        obj.put("code", ServerPacket.LOGIN_RESPONSE.code);
        obj.put("accept", invalidReason == null);
        if (invalidReason != null) {
            obj.put("reason", invalidReason);
        }
        obj.put("playerID", playerID);

        ctx.connection.sendTCP(obj.toString());
    }

    public static void onClientRegisterRequest(NetworkContext ctx) {
        String username = ctx.json.getString("username");
        String password = ctx.json.getString("password");

        String invalidReason = null;

        if (username.isBlank() || password.isBlank()) {
            invalidReason = "Username or password is blank!";
        } else if (PlayerManager.load(username) != null) {
            invalidReason = "There is already another player with this username!";
        }

        int playerID = ctx.connection.getID();
        JSONObject obj = new JSONObject();

        if (invalidReason == null) {
            var pd = new PlayerData(playerID, username, Encrypter.encrypt(password));
            PlayerManager.save(pd);
        } else {
            obj.put("reason", invalidReason);
        }

        obj.put("code", ServerPacket.REGISTER_RESPONSE.code);
        obj.put("accept", invalidReason == null);
        obj.put("playerID", playerID);

        ctx.connection.sendTCP(obj.toString());
    }

    public static void onPlayerTransformUpdate(NetworkContext ctx) {
        var json = ctx.json;

        PlayerData pd = Server.getPlayer(ctx.connection.getID());
        pd.setPosX(json.getFloat("posX"));
        pd.setPosY(json.getFloat("posY"));
        pd.setScaleX(json.getFloat("scaX"));
        pd.setScaleY(json.getFloat("scaY"));
        pd.setRotation(json.getFloat("rot"));

        json.remove("code");
        json.put("code", ServerPacket.TRANSFORM_UPDATE.code);
        Server.getServer().sendToAllExceptUDP(
                ctx.connection.getID(), json.toString());
    }

    public static void onNewPlayer(NetworkContext ctx) {
        var player = PlayerManager.getCachedPlayer(ctx.json.getInt("playerID"));

        var json = new JSONObject();
        json.put("code", ServerPacket.ENTITY_INSTANCE.code);

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
        Server.addPlayer(player);

        json = new JSONObject();
        json.put("code", ServerPacket.GAME_READY_RESPONSE.code);
        json.put("map", player.getMapIndex());

        array = new JSONArray();
        for (var en : Server.getAllPlayers()) {
            obj = new JSONObject();
            obj.put("id", en.id());
            obj.put("posX", en.getPosX());
            obj.put("posY", en.getPosY());
            obj.put("scaX", en.getScaleX());
            obj.put("scaY", en.getScaleY());
            obj.put("rot", en.getRotation());
            array.put(obj);
        }
        json.put("entities", array);

        Server.getServer().sendToTCP(player.id(), json.toString());
    }
}
