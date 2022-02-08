package com.dawnengine.game.entity;

import com.dawnengine.math.Vector2;
import com.dawnengine.network.Client;
import com.dawnengine.network.ClientNetworkPackets;
import com.dawnengine.network.EntityPacket;
import com.dawnengine.network.PacketType;
import com.dawnengine.utils.JSON;
import java.util.ArrayList;

/**
 *
 * @author alyss
 */
public class NetworkEntity extends Entity {

    private final ArrayList<EntityPacket> queuedPackets = new ArrayList<>();

    public NetworkEntity(int id, Vector2 position) {
        super(id, position);
    }

    public void invalidate(EntityPacket packet) {
        if (queuedPackets.contains(packet)) {
            queuedPackets.remove(packet);
        }
        queuedPackets.add(packet);
    }

    @Override
    public void networkUpdate() {
        for (EntityPacket p : queuedPackets) {
            var socket = Client.getClient().getSocket();
            switch (p.type()) {
                case TCP:
                    socket.sendTCP(p.packet());
                    break;
                case UDP:
                    socket.sendUDP(p.packet());
                    break;
            }
        }
        queuedPackets.clear();
    }

    protected void invalidateTransform() {
        invalidate(Client.createEntityPacket(ClientNetworkPackets.TRANSFORM_UPDATE,
                PacketType.UDP,
                new JSON("id", id()),
                new JSON("posX", transform().position().x),
                new JSON("posY", transform().position().y),
                new JSON("rot", transform().rotation()),
                new JSON("scaX", transform().scale().x),
                new JSON("scaY", transform().scale().y)));
    }

}
