package com.dawnengine.network;

/**
 *
 * @author alyss
 */
public class EntityPacket {
    private final int code;
    private final String packet;
    private final PacketType type;

    public EntityPacket(int code, String packet, PacketType type) {
        this.code = code;
        this.packet = packet;
        this.type = type;
    }

    public String packet() {
        return packet;
    }

    public PacketType type() {
        return type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.code;
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
        if (this.code != other.code) {
            return false;
        }
        return true;
    }
    
    
}