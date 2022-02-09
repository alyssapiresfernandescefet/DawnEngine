package com.dawnengine.entity;

import com.dawnengine.math.Vector2;
import com.dawnengine.network.Client;
import com.dawnengine.network.ClientPacketType;
import com.dawnengine.network.EntityPacket;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alyss
 */
public class NetworkEntity extends Entity {

    private final ArrayList<EntityPacket> tcpPackets = new ArrayList<>();
    private final ArrayList<EntityPacket> udpPackets = new ArrayList<>();

    public NetworkEntity(int id, Vector2 position) {
        super(id, position);
    }

    public void invalidate(EntityPacket packet) {
        switch (packet.type().route) {
            case "TCP":
                tcpPackets.remove(packet);
                tcpPackets.add(packet);
                break;
            case "UDP":
                udpPackets.remove(packet);
                udpPackets.add(packet);
                break;
        }
    }

    @Override
    public void networkUpdate() {
        if (tcpPackets.size() > 0) {
            var tcpArr = new JSONArray();
            for (EntityPacket p : tcpPackets) {
                tcpArr.put(p.json());
            }
            var tcpObj = tcpArr.length() == 1 ? tcpArr.getJSONObject(0) : tcpArr;
            Client.getClient().sendTCP(tcpObj.toString());
            tcpPackets.clear();
        }
        if (udpPackets.size() > 0) {
            var udpArr = new JSONArray();
            for (EntityPacket p : udpPackets) {
                udpArr.put(p.json());
            }
            var udpObj = udpArr.length() == 1 ? udpArr.getJSONObject(0) : udpArr;
            Client.getClient().sendUDP(udpObj.toString());
            udpPackets.clear();
        }
    }

    protected void invalidateTransform() {
        invalidate(new EntityPacket(ClientPacketType.TRANSFORM_UPDATE,
                new JSONObject().put("id", id())
                        .put("posX", transform().position().x)
                        .put("posY", transform().position().y)
                        .put("rot", transform().rotation())
                        .put("scaX", transform().scale().x)
                        .put("scaY", transform().scale().y)));
    }

}
