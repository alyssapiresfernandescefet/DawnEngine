package com.dawnengine.network.events;

import com.dawnengine.data.MapData;
import com.dawnengine.data.PlayerData;
import com.dawnengine.managers.MapManager;
import com.dawnengine.managers.PlayerManager;
import com.dawnengine.network.NetworkContext;
import com.dawnengine.network.NetworkPackets;
import com.dawnengine.network.NUtil;
import com.dawnengine.utils.Encrypter;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Every event related to a client or a player. A client is a non-identified
 * player.
 *
 * @author alyss
 */
public class PlayerEvents {

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
                var res = NUtil.getResponseObject(NetworkPackets.SV_LOGIN_RES, req)
                        .put("accept", false).put("reason", reason);
                ctx.server().sendTo(ctx.connection().getID(), res);
                return;
            }
        }

        { //Telling all other players that a new player has arrived.
            var ev = NUtil.getEventObject(NetworkPackets.SV_ENTITY_INSTANCE_EV);
            var array = new JSONArray().put(new JSONObject()
                    .put("id", ctx.connection().getID())
                    .put("posX", player.getPosX())
                    .put("posY", player.getPosY()));
            ev.put("entities", array);
            ctx.server().sendToMap(player.getMapIndex(), ev);
        }

        { //Send to current player all of the data to initialize.
            var res = NUtil.getResponseObject(NetworkPackets.SV_LOGIN_RES, req)
                    .put("accept", true)
                    .put("playerID", id)
                    .put("posX", player.getPosX())
                    .put("posY", player.getPosY())
                    .put("mapIndex", player.getMapIndex());
            ctx.server().sendTo(ctx.connection().getID(), res);
            ctx.server().addPlayer(player);
        }
    }

    public static void onClientRegisterRequest(NetworkContext ctx) {
        var req = ctx.request();
        String username = req.getString("username");
        String password = req.getString("password");

        var res = NUtil.getResponseObject(NetworkPackets.SV_REGISTER_RES, req);
        res.put("accept", false);
        if (username.isBlank() || password.isBlank()) {
            res.put("reason", "Username or password is blank!");
            ctx.server().sendTo(ctx.connection().getID(), res);
            return;
        } else if (PlayerManager.exists(username)) {
            res.put("reason", "There is already another player with this username!");
            ctx.server().sendTo(ctx.connection().getID(), res);
            return;
        }
        
        PlayerManager.save(new PlayerData(username, Encrypter.encrypt(password)));
        onClientLoginRequest(ctx);
    }

    public static void onPlayerDisconnect(NetworkContext ctx, PlayerData pd) {

        PlayerManager.save(pd);
        var ev = NUtil.getEventObject(NetworkPackets.SV_ENTITY_DESTROY_EV)
                .put("entities", NUtil.getPlayerEntity(pd.getID()));
        ctx.server().sendToAll(ev);
    }

    public static void onPlayerMove(NetworkContext ctx) {
        var req = ctx.request();
        var posX = req.getFloat("posX");
        var posY = req.getFloat("posY");

        PlayerData pd = ctx.server().getPlayer(ctx.connection().getID());
        pd.setPosX(posX);
        pd.setPosY(posY);

        var ev = NUtil.getEventObject(NetworkPackets.SV_PLAYER_MOVE_EV);
        ev.put("posX", posX);
        ev.put("posY", posY);
        ev.put("speed", req.getFloat("speed"));
        ev.put("id", ctx.connection().getID());
        ctx.server().sendToMapExcept(ctx.connection().getID(), pd.getMapIndex(), ev);
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
            case NUtil.DIR_UP:
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
            case NUtil.DIR_DOWN:
                player.setPosY(MapData.TILE_SIZE_Y / 2);
                sizeX = mapData.getTileCountX() * MapData.TILE_SIZE_X;
                posX = player.getPosX();
                if (posX > sizeX) {
                    posX = sizeX;
                }
                player.setPosX(posX);
                break;
            case NUtil.DIR_RIGHT:
                player.setPosX(MapData.TILE_SIZE_X / 2);
                var sizeY = mapData.getTileCountY() * MapData.TILE_SIZE_Y;
                var posY = player.getPosY();
                if (posY > sizeY) {
                    posY = sizeY;
                }
                player.setPosY(posY);
                break;
            case NUtil.DIR_LEFT:
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
            var ev = NUtil.getEventObject(NetworkPackets.SV_ENTITY_DESTROY_EV)
                    .put("entities", NUtil.getPlayerEntity(ctx.connection()));
            ctx.server().sendToMap(oldMapIndex, ev);
        }

        { //Tell all players on new map to instantiate player.
            var ev = NUtil.getEventObject(NetworkPackets.SV_ENTITY_INSTANCE_EV)
                    .put("entities", NUtil.getPlayerEntity(ctx.connection(),
                            player.getPosX(), player.getPosY()));
            ctx.server().sendToMapExcept(ctx.connection().getID(), newMapIndex, ev);
        }

        { //Send event to current player to get map.
            var ev = NUtil.getEventObject(NetworkPackets.SV_GET_MAP_EV)
                    .put("mapIndex", newMapIndex)
                    .put("update", true)
                    .put("newPosX", player.getPosX())
                    .put("newPosY", player.getPosY())
                    .put("entities", NUtil.getEntitiesOnMapExcept(
                            ctx.connection(), newMapIndex, ctx.server()));
            NUtil.concat(ev, mapData.toJSON());
            ctx.server().sendTo(ctx.connection().getID(), ev);
        }
    }
}
