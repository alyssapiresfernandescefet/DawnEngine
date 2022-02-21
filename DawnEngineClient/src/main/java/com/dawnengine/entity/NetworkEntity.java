package com.dawnengine.entity;

import com.dawnengine.math.Vector2;
import com.dawnengine.network.Client;
import com.dawnengine.network.ClientPackets;
import com.dawnengine.network.EntityPacket;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alyss
 */
public class NetworkEntity extends Entity {

    private final ArrayList<EntityPacket> packets = new ArrayList<>();

    public NetworkEntity(int id, Vector2 position) {
        super(id, position);
    }

    public void invalidate(EntityPacket packet) {
        packets.remove(packet);
        packets.add(packet);
    }

    @Override
    public void networkUpdate() {
        if (packets.size() > 0) {
            var tcpArr = new JSONArray();
            for (EntityPacket p : packets) {
                tcpArr.put(p.json());
            }
            var tcpObj = tcpArr.length() == 1 ? tcpArr.getJSONObject(0) : tcpArr;
            Client.getClient().send(tcpObj.toString());
            packets.clear();
        }
    }

    protected void invalidateTransform() {
        invalidate(new EntityPacket(ClientPackets.TRANSFORM_UPDATE,
                new JSONObject().put("id", id())
                        .put("posX", transform().position().x)
                        .put("posY", transform().position().y)
                        .put("rot", transform().rotation())
                        .put("scaX", transform().scale().x)
                        .put("scaY", transform().scale().y)));
    }

}
