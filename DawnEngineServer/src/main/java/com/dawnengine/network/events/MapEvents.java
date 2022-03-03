package com.dawnengine.network.events;

import com.dawnengine.data.MapData;
import com.dawnengine.managers.MapManager;
import com.dawnengine.network.NUtil;
import com.dawnengine.network.NetworkContext;
import com.dawnengine.network.NetworkPackets;

/**
 *
 * @author alyss
 */
public class MapEvents {

    public static MapData onGetMap(NetworkContext ctx) {
        var index = ctx.request().getInt("mapIndex");
        var req = ctx.request();

        MapData map = MapManager.load(index);
        var serverLastRevision = map.getLastRevision();

        var res = NUtil.getResponseObject(NetworkPackets.SV_GET_MAP_EV, req)
                .put("mapIndex", index)
                .put("entities", NUtil.getEntitiesOnMapExcept(
                        ctx.connection(), index, ctx.server()));

        var clientLastRevision = req.getLong("lastRevision");
        if (clientLastRevision == 0 || clientLastRevision != serverLastRevision) {
            NUtil.concat(res, map.toJSON()).put("update", true);
        }
        ctx.server().sendTo(ctx.connection().getID(), res);
        return map;
    }

    public static void onSaveMap(NetworkContext ctx) {
        var req = ctx.request();
        var index = req.getInt("mapIndex");
        
        var mapData = new MapData(req.getString("name"), req.getInt("sizeX"),
                req.getInt("sizeY"), req.getLong("lastRevision"),
                req.getInt("moral"), req.getInt("lUp"),
                req.getInt("lDown"), req.getInt("lRight"), req.getInt("lLeft"),
                req.getString("tiles"));
        MapManager.save(index, mapData);

        { //Responding to current player.
            var res = NUtil.getResponseObject(NetworkPackets.SV_UPDATE_MAP_RES, req);
            res.put("accept", true);
            ctx.server().sendTo(ctx.connection().getID(), res);
        }

        { //Notifying all present players.
            var ev = NUtil.concat(NUtil.getEventObject(NetworkPackets.SV_GET_MAP_EV),
                    mapData.toJSON())
                    .put("mapIndex", index)
                    .put("update", true);
            ctx.server().sendToMapExcept(ctx.connection().getID(), index, ev);
        }
    }
}
