package com.dawnengine.data;

import java.io.Serializable;

/**
 *
 * @author alyss
 */
public class PlayerData implements Serializable {
    
    private int id;
    private final Account account;
    private int mapIndex;
    private float posX, posY, scaleX, scaleY;
    private float rotation;

    public PlayerData() {
        this.id = 0;
        this.account = null;
    }

    public PlayerData(int playerID, String username, String password) {
        this.account = new Account(username, password);
        this.id = playerID;
        mapIndex = 1;
        posX = posY = rotation = 0;
        scaleX = scaleY = 1;
    }

    public int id() {
        return id;
    }

    public void id(int id) {
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

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    
    
}
