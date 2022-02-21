package com.dawnengine.network;

import java.util.Objects;
import org.json.JSONObject;

/**
 * An utility class used by Network Entities to send data.
 *
 * @author alyss
 */
public class EntityPacket {

    private final int code;
    private final JSONObject json;

    public EntityPacket(int code, JSONObject json) {
        this.code = code;
        this.json = json;
        if (!this.json.has("code")) {
            this.json.put("code", this.code);
        }
    }

    public int type() {
        return code;
    }

    public JSONObject json() {
        return json;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.code);
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
