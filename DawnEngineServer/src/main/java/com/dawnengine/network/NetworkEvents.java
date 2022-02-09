package com.dawnengine.network;

import com.dawnengine.data.Account;
import com.dawnengine.data.PlayerManager;
import com.dawnengine.model.NetworkEntity;
import com.dawnengine.utils.Encrypter;
import org.json.JSONArray;
import org.json.JSONObject;

public class NetworkEvents {

    public static void onClientLoginRequest(NetworkContext ctx) {
        String username = ctx.json.getString("username");
        String password = ctx.json.getString("password");

        String invalidReason = null;

        Account acc = PlayerManager.get(username);

        if (username.isBlank() || password.isBlank()) {
            invalidReason = "Username or password is blank!";
        } else if (acc == null || !Encrypter.compare(password, acc.password)) {
            invalidReason = "Username or password is invalid!";
        }

        int playerID = ctx.connection.getID();
        JSONObject obj = new JSONObject();

        obj.put("code", ServerPacket.LOGIN_RESPONSE.code);
        obj.put("accept", invalidReason == null);
        if (invalidReason != null) {
            obj.put("reason", invalidReason);
        } else {
            instantiateNewPlayer(new NetworkEntity(playerID));
        }
        obj.put("playerID", playerID);

        ctx.connection.sendTCP(obj.toString());
    }

    public static void onClientRegisterRequest(NetworkContext ctx) {
        String username = ctx.json.getString("username");
        String password = ctx.json.getString("password");

        String invalidReason = null;

        Account acc = PlayerManager.get(username);

        if (username.isBlank() || password.isBlank()) {
            invalidReason = "Username or password is blank!";
        } else if (acc != null) {
            invalidReason = "There is already another player with this username!";
        }

        int playerID = ctx.connection.getID();
        JSONObject obj = new JSONObject();

        if (invalidReason == null) {
            String hashedPass = Encrypter.encrypt(password);
            PlayerManager.set(username, hashedPass);
            instantiateNewPlayer(new NetworkEntity(playerID));
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

        var entity = Server.entities.get(ctx.connection.getID());
        entity.setPosX(json.getFloat("posX"));
        entity.setPosY(json.getFloat("posY"));
        entity.setScaleX(json.getFloat("scaX"));
        entity.setScaleY(json.getFloat("scaY"));
        entity.setRotation(json.getFloat("rot"));

        json.remove("code");
        json.put("code", ServerPacket.TRANSFORM_UPDATE.code);
        Server.getServer().sendToAllExceptUDP(
                ctx.connection.getID(), json.toString());
    }

    private static void instantiateNewPlayer(NetworkEntity entity) {
        Server.entities.put(entity.id(), entity);

        var json = new JSONObject();
        json.put("code", ServerPacket.ENTITY_INSTANCE.code);

        var array = new JSONArray();
        var obj = new JSONObject();
        obj.put("id", entity.id());
        obj.put("posX", 0);
        obj.put("posY", 0);
        obj.put("scaX", 1);
        obj.put("scaY", 1);
        obj.put("rot", 0);
        array.put(obj);
        json.put("entities", array);

        Server.getServer().sendToAllExceptTCP(
                entity.id(), json.toString());

        json = new JSONObject();
        json.put("code", ServerPacket.ENTITY_INSTANCE.code);

        array = new JSONArray();
        for (var en : Server.entities.values()) {
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

        Server.getServer().sendToTCP(entity.id(), json.toString());
    }
}
