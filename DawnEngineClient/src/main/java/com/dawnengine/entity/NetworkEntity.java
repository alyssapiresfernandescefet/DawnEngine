package com.dawnengine.entity;

import com.dawnengine.math.Vector2;
import com.dawnengine.network.Client;
import com.dawnengine.network.NetworkPackets;
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
            Client.getClient().send(tcpArr.toString());
            packets.clear();
        }
    }

}
