package com.dawnengine.network;

import com.dawnengine.data.MapData;
import com.dawnengine.data.PlayerData;
import com.dawnengine.managers.MapManager;
import com.dawnengine.managers.PlayerManager;
import com.dawnengine.utils.Encrypter;
import org.json.JSONArray;
import org.json.JSONObject;

public class NetworkEvents {

    public static void onPlayerDisconnect(PlayerData e) {
        PlayerManager.save(e);
        var json = new JSONObject();
        json.put("code", ServerPackets.SERVER_ENTITY_DESTROY_EVENT);
        var array = new JSONArray();
        var obj = new JSONObject();
        obj.put("id", e.getID());
        array.put(obj);
        json.put("entities", array);
        Server.getServer().sendToAll(json);
    }

    public static void onClientLoginRequest(NetworkContext ctx) {
        var req = ctx.request();
        int playerID = ctx.connection().getID();
        String username = req.getString("username");
        String password = req.getString("password");

        PlayerData player = PlayerManager.load(playerID, username);
        
        { //Checking player veracity.
            String reason = null;
            if (Server.getPlayer(playerID) != null) {
                reason = "This account is already active.";
            } else if (username.isBlank() || password.isBlank()) {
                reason = "Username or password is blank!";
            } else if (player == null || !Encrypter.compare(password,
                    player.getAccount().password)) {
                reason = "Username or password is invalid!";
            }

            if (reason != null) {
                var res = getResponseObject(req);
                res.put("code", ServerPackets.SERVER_LOGIN_RESPONSE);
                res.put("accept", false);
                res.put("reason", reason);
                Server.getServer().sendTo(ctx.connection(), res);
                return;
            }
        }

        { //Telling all other players that a new player has arrived.
            var ev = new JSONObject();
            ev.put("code", ServerPackets.SERVER_ENTITY_INSTANCE_EVENT);

            var array = new JSONArray();
            var obj = new JSONObject();
            obj.put("id", player.getID());
            obj.put("posX", 0);
            obj.put("posY", 0);
            obj.put("scaX", 1);
            obj.put("scaY", 1);
            obj.put("rot", 0);
            array.put(obj);
            ev.put("entities", array);

            Server.getServer().sendToMap(player.getMapIndex(), ev);
        }

        { //Send to current player all of the data to initialize.
            var res = getResponseObject(req);

            res.put("code", ServerPackets.SERVER_LOGIN_RESPONSE);
            res.put("accept", true);
            var array = new JSONArray();
            for (var en : Server.getAllPlayers()) {
                var json = new JSONObject();
                json.put("id", en.getID());
                json.put("posX", en.getPosX());
                json.put("posY", en.getPosY());
                array.put(json);
            }
            
            //Put more data here if necessary.
            res.put("entities", array);
            res.put("playerID", playerID);
            res.put("posX", player.getPosX());
            res.put("posY", player.getPosY());
            res.put("mapIndex", player.getMapIndex());

            Server.addPlayer(player);
            Server.getServer().sendTo(player.getID(), res);
        }
    }

    public static void onClientRegisterRequest(NetworkContext ctx) {
        var req = ctx.request();
        String username = req.getString("username");
        String password = req.getString("password");

        String invalidReason = null;

        if (username.isBlank() || password.isBlank()) {
            invalidReason = "Username or password is blank!";
        } else if (PlayerManager.exists(username)) {
            invalidReason = "There is already another player with this username!";
        }

        var res = getResponseObject(req);

        if (invalidReason == null) {
            int playerID = ctx.connection().getID();
            var pd = new PlayerData(playerID, username, Encrypter.encrypt(password));
            PlayerManager.save(pd);
        } else {
            res.put("reason", invalidReason);
        }

        res.put("code", ServerPackets.SERVER_REGISTER_RESPONSE);
        res.put("accept", invalidReason == null);

        Server.getServer().sendTo(ctx.connection(), res);
    }

    public static void onPlayerMove(NetworkContext ctx) {
        var req = ctx.request();
        PlayerData pd = Server.getPlayer(ctx.connection().getID());
        pd.setPosX(req.getFloat("posX"));
        pd.setPosY(req.getFloat("posY"));

        var res = copyRequestObject(req, "posX", "posY", "speed");
        res.put("code", ServerPackets.SERVER_PLAYER_MOVE_EVENT);
        res.put("id", ctx.connection().getID());
        Server.getServer().sendToAllExcept(ctx.connection(), res);
    }

    public static void onCheckMap(NetworkContext ctx) {
        var index = ctx.request().getInt("mapIndex");
        var map = MapManager.load(index);

        var res = getResponseObject(ctx.request());
        res.put("code", ServerPackets.SERVER_CHECK_MAP_RESPONSE);
        res.put("mapIndex", index);
        res.put("lastRevision", map.getLastRevision());
        Server.getServer().sendTo(ctx.connection().getID(), res);
    }

    public static void onGetMap(NetworkContext ctx) {
        var index = ctx.request().getInt("mapIndex");
        MapData map = MapManager.load(index);

        var res = getResponseObject(ctx.request());
        res.put("code", ServerPackets.SERVER_GET_MAP_RESPONSE);

        res.put("mapIndex", index);
        res.put("name", map.getName());
        res.put("lastRevision", map.getLastRevision());
        res.put("moral", map.getMoral());
        res.put("lUp", map.getLinkUp());
        res.put("lDown", map.getLinkDown());
        res.put("lRight", map.getLinkRight());
        res.put("lLeft", map.getLinkLeft());
        res.put("sizeX", map.getTileCountX());
        res.put("sizeY", map.getTileCountY());
        res.put("tiles", map.getTiles());

        Server.getServer().sendTo(ctx.connection().getID(), res);
    }

    public static void onSaveMap(NetworkContext ctx) {
        var req = ctx.request();

        var index = req.getInt("mapIndex");
        var mapData = new MapData(req.getString("name"),
                req.getInt("sizeX"), req.getInt("sizeY"),
                req.getLong("lastRevision"),
                req.getInt("moral"), req.getInt("lUp"),
                req.getInt("lDown"), req.getInt("lRight"),
                req.getInt("lLeft"), req.getString("tiles"));
        MapManager.save(index, mapData);

        var res = getResponseObject(req);
        res.put("code", ServerPackets.SERVER_UPDATE_MAP_RESPONSE);
        res.put("accept", true);
        Server.getServer().sendTo(ctx.connection(), res);

        res = new JSONObject()
                .put("code", ServerPackets.SERVER_GET_MAP_EVENT)
                .put("mapIndex", index)
                .put("name", mapData.getName())
                .put("sizeX", mapData.getTileCountX())
                .put("sizeY", mapData.getTileCountY())
                .put("lastRevision", mapData.getLastRevision())
                .put("moral", mapData.getMoral())
                .put("lUp", mapData.getLinkUp())
                .put("lDown", mapData.getLinkDown())
                .put("lRight", mapData.getLinkRight())
                .put("lLeft", mapData.getLinkLeft())
                .put("tiles", mapData.getTiles());
        Server.getServer().sendToMapExcept(ctx.connection(), index, res);
    }

    private static JSONObject getResponseObject(JSONObject request) {
        JSONObject res = new JSONObject();
        if (request.has("reqTime")) {
            res.put("reqTime", request.getLong("reqTime"));
        }
        return res;
    }

    private static JSONObject copyRequestObject(JSONObject request, String... names) {
        JSONObject res = new JSONObject(request, names);
        if (request.has("reqTime")) {
            res.put("reqTime", request.getLong("reqTime"));
        }
        return res;
    }
}
