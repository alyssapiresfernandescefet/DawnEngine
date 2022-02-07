package com.dawnengine.model;

import com.esotericsoftware.kryonet.Connection;

/**
 *
 * @author alyss
 */
public class PlayerClient {

    public final Connection connection;
    private boolean inGame = false;

    public PlayerClient(Connection connection) {
        this.connection = connection;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

}
