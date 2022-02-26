package com.dawnengine.network;

import com.dawnengine.core.Server;
import com.dawnengine.data.MapData;
import com.dawnengine.data.PlayerData;
import com.dawnengine.managers.MapManager;
import com.dawnengine.managers.PlayerManager;
import com.dawnengine.utils.Encrypter;
import org.json.JSONArray;
import org.json.JSONObject;

public class NetworkEvents {

    private static JSONObject getResponseObject(int responseCode, JSONObject request) {
        JSONObject res = new JSONObject();
        if (request.has("reqTime")) {
            res.put("reqTime", request.getLong("reqTime"));
        }
        res.put("code", responseCode);
        return res;
    }

    private static JSONObject getEventObject(int responseCode) {
        JSONObject res = new JSONObject();
        res.put("code", responseCode);
        return res;
    }

    public static void onPlayerDisconnect(PlayerData pd, Server server) {
        PlayerManager.save(pd);
        var ev = getEventObject(NetworkPackets.SV_ENTITY_DESTROY_EV);
        ev.put("entities", NetworkUtils.getPlayerEntity(pd.getID()));
        server.sendToAll(ev);
    }

    public static void onClientLoginRequest(NetworkContext ctx) {
        var req = ctx.request();

        int id = ctx.connection().getID();
        String username = req.getString("username");
        String password = req.getString("password");

        PlayerData player = PlayerManager.load(id, username);

        { //Checking player authentication.
            String reason = null;
            if (ctx.server().getPlayer(username) != null) {
                reason = "This account is already active.";
            } else if (username.isBlank() || password.isBlank()) {
                reason = "Username or password is blank!";
            } else if (player == null || !Encrypter.compare(password,
                    player.getAccount().password)) {
                reason = "Username or password is invalid!";
            }

            if (reason != null) {
                var res = getResponseObject(NetworkPackets.SV_LOGIN_RES, req)
                        .put("accept", false).put("reason", reason);
                ctx.server().sendTo(ctx.connection().getID(), res);
                return;
            }
        }

        player.setID(id);

        { //Telling all other players that a new player has arrived.
            var ev = getEventObject(NetworkPackets.SV_ENTITY_INSTANCE_EV);
            var array = new JSONArray();
            var obj = new JSONObject();
            obj.put("id", ctx.connection().getID());
            obj.put("posX", player.getPosX());
            obj.put("posY", player.getPosY());
            array.put(obj);
            ev.put("entities", array);

            ctx.server().sendToMap(player.getMapIndex(), ev);
        }

        { //Send to current player all of the data to initialize.
            var res = getResponseObject(NetworkPackets.SV_LOGIN_RES, req);
            res.put("accept", true);
            res.put("playerID", id);
            res.put("posX", player.getPosX());
            res.put("posY", player.getPosY());
            res.put("mapIndex", player.getMapIndex());

            ctx.server().addPlayer(player);
            ctx.server().sendTo(ctx.connection().getID(), res);
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

        var res = getResponseObject(NetworkPackets.SV_REGISTER_RES, req);

        if (invalidReason == null) {
            PlayerManager.save(new PlayerData(username, Encrypter.encrypt(password)));
        } else {
            res.put("reason", invalidReason);
        }

        res.put("accept", invalidReason == null);

        ctx.server().sendTo(ctx.connection().getID(), res);
    }

    public static void onPlayerMove(NetworkContext ctx) {
        var req = ctx.request();
        var posX = req.getFloat("posX");
        var posY = req.getFloat("posY");

        PlayerData pd = ctx.server().getPlayer(ctx.connection().getID());
        pd.setPosX(posX);
        pd.setPosY(posY);

        var ev = getEventObject(NetworkPackets.SV_PLAYER_MOVE_EV);
        ev.put("posX", posX);
        ev.put("posY", posY);
        ev.put("speed", req.getFloat("speed"));
        ev.put("id", ctx.connection().getID());
        ctx.server().sendToMapExcept(ctx.connection().getID(), pd.getMapIndex(), ev);
    }

    public static MapData onGetMap(NetworkContext ctx) {
        var index = ctx.request().getInt("mapIndex");

        MapData map = MapManager.load(index);
        var serverLastRevision = map.getLastRevision();

        var res = getResponseObject(NetworkPackets.SV_GET_MAP_EV, ctx.request())
                .put("mapIndex", index)
                .put("entities", NetworkUtils.getEntitiesOnMapExcept(
                        ctx.connection(), index, ctx.server()));
        if (!ctx.request().has("lastRevision")
                || ctx.request().getLong("lastRevision") != serverLastRevision) {
            res.put("name", map.getName())
                    .put("lastRevision", serverLastRevision)
                    .put("moral", map.getMoral())
                    .put("lUp", map.getLinkUp())
                    .put("lDown", map.getLinkDown())
                    .put("lRight", map.getLinkRight())
                    .put("lLeft", map.getLinkLeft())
                    .put("sizeX", map.getTileCountX())
                    .put("sizeY", map.getTileCountY())
                    .put("tiles", map.getTiles())
                    .put("update", true);
        }
        ctx.server().sendTo(ctx.connection().getID(), res);
        return map;
    }

    public static void onSaveMap(NetworkContext ctx) {
        var req = ctx.request();

        var index = req.getInt("mapIndex");
        var name = req.getString("name");
        var sizeX = req.getInt("sizeX");
        var sizeY = req.getInt("sizeY");
        var lastRevision = req.getLong("lastRevision");
        var moral = req.getInt("moral");
        var lUp = req.getInt("lUp");
        var lDown = req.getInt("lDown");
        var lRight = req.getInt("lRight");
        var lLeft = req.getInt("lLeft");
        var tiles = req.getString("tiles");

        var mapData = new MapData(name, sizeX, sizeY, lastRevision, moral, lUp,
                lDown, lRight, lLeft, tiles);
        MapManager.save(index, mapData);

        { //Responding to current player.
            var res = getResponseObject(NetworkPackets.SV_UPDATE_MAP_RES, req);
            res.put("accept", true);
            ctx.server().sendTo(ctx.connection().getID(), res);
        }

        { //Notifying all present players.
            var ev = getEventObject(NetworkPackets.SV_GET_MAP_EV)
                    .put("mapIndex", index).put("name", name)
                    .put("sizeX", sizeX).put("sizeY", sizeY)
                    .put("lastRevision", lastRevision).put("moral", moral)
                    .put("lUp", lUp).put("lDown", lDown)
                    .put("lRight", lRight).put("lLeft", lLeft)
                    .put("tiles", tiles)
                    .put("update", true);
            ctx.server().sendToMapExcept(ctx.connection().getID(), index, ev);
        }
    }

    public static void onPlayerMoveMap(NetworkContext ctx) {
        var player = ctx.server().getPlayer(ctx.connection().getID());
        var oldMapIndex = player.getMapIndex();
        var newMapIndex = ctx.request().getInt("mapIndex");
        var direction = ctx.request().getInt("dir");

        if (newMapIndex == 0 || oldMapIndex == newMapIndex) {
            return;
        }

        var mapData = MapManager.load(newMapIndex);

        switch (direction) {
            case NetworkUtils.DIR_UP:
                player.setPosY(mapData.getTileCountY() * MapData.TILE_SIZE_Y
                        - MapData.TILE_SIZE_Y / 2);
                var sizeX = mapData.getTileCountX() * MapData.TILE_SIZE_X
                        - MapData.TILE_SIZE_X / 2;
                var posX = player.getPosX();
                if (posX > sizeX) {
                    posX = sizeX;
                }
                player.setPosX(posX);
                break;
            case NetworkUtils.DIR_DOWN:
                player.setPosY(MapData.TILE_SIZE_Y / 2);
                sizeX = mapData.getTileCountX() * MapData.TILE_SIZE_X;
                posX = player.getPosX();
                if (posX > sizeX) {
                    posX = sizeX;
                }
                player.setPosX(posX);
                break;
            case NetworkUtils.DIR_RIGHT:
                player.setPosX(MapData.TILE_SIZE_X / 2);
                var sizeY = mapData.getTileCountY() * MapData.TILE_SIZE_Y;
                var posY = player.getPosY();
                if (posY > sizeY) {
                    posY = sizeY;
                }
                player.setPosY(posY);
                break;
            case NetworkUtils.DIR_LEFT:
                player.setPosX(mapData.getTileCountX() * MapData.TILE_SIZE_X
                        - MapData.TILE_SIZE_X / 2);
                sizeY = mapData.getTileCountY() * MapData.TILE_SIZE_Y
                        - MapData.TILE_SIZE_Y / 2;
                posY = player.getPosY();
                if (posY > sizeY) {
                    posY = sizeY;
                }
                player.setPosY(posY);
                break;
        }

        player.setMapIndex(newMapIndex);

        { //Tell all players on old map to destroy player.
            var ev = getEventObject(NetworkPackets.SV_ENTITY_DESTROY_EV);
            ev.put("entities", NetworkUtils.getPlayerEntity(ctx.connection()));
            ctx.server().sendToMap(oldMapIndex, ev);
        }

        { //Tell all players on new map to instantiate player.
            var ev = getEventObject(NetworkPackets.SV_ENTITY_INSTANCE_EV);
            ev.put("entities", NetworkUtils.getPlayerEntity(ctx.connection(),
                    player.getPosX(), player.getPosY()));
            ctx.server().sendToMapExcept(ctx.connection().getID(), newMapIndex, ev);
        }

        { //Send event to current player to get map.
            var ev = getEventObject(NetworkPackets.SV_GET_MAP_EV)
                    .put("mapIndex", newMapIndex)
                    .put("name", mapData.getName())
                    .put("sizeX", mapData.getTileCountX())
                    .put("sizeY", mapData.getTileCountY())
                    .put("lastRevision", mapData.getLastRevision())
                    .put("moral", mapData.getMoral())
                    .put("lUp", mapData.getLinkUp())
                    .put("lDown", mapData.getLinkDown())
                    .put("lRight", mapData.getLinkRight())
                    .put("lLeft", mapData.getLinkLeft())
                    .put("tiles", mapData.getTiles())
                    .put("update", true)
                    .put("newPosX", player.getPosX())
                    .put("newPosY", player.getPosY())
                    .put("entities", NetworkUtils.getEntitiesOnMapExcept(
                            ctx.connection(), newMapIndex, ctx.server()));
            ctx.server().sendTo(ctx.connection().getID(), ev);
        }
    }
}
