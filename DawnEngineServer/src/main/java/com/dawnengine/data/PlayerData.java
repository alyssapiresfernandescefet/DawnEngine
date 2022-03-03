package com.dawnengine.data;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author alyss
 */
public class PlayerData implements Serializable {

    private transient int id;
    private final Account account;
    private int mapIndex;
    private float posX, posY;

    private PlayerData() {
        this.account = null;
    }

    public PlayerData(String username, String password) {
        this.account = new Account(username, password);
        mapIndex = 1;
        posX = posY = 0;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public int getMapIndex() {
        return mapIndex;
    }

    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.account);
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
        final PlayerData other = (PlayerData) obj;
        if (!Objects.equals(this.account, other.account)) {
            return false;
        }
        return true;
    }
}
