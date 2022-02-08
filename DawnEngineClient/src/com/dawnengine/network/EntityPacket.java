package com.dawnengine.network;

import java.util.Objects;
import org.json.JSONObject;

/**
 * An utility class used by Network Entities to send data.
 * @author alyss
 */
public class EntityPacket {
    private final ClientPacketType type;
    private final JSONObject json;

    public EntityPacket(ClientPacketType type, JSONObject json) {
        this.type = type;
        this.json = json;
    }

    public ClientPacketType type() {
        return type;
    }

    public JSONObject json() {
        return json;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntityPacket other = (EntityPacket) obj;
        if (this.type != other.type) {
            return false;
        }
        return true;
    }
    
}