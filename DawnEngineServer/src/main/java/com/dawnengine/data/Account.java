package com.dawnengine.data;

import java.io.Serializable;

/**
 *
 * @author alyss
 */
public class Account implements Serializable {

    public final String username, password;

    public Account() {
        this.username = null;
        this.password = null;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" + "username=" + username + ", password=" + password + '}';
    }

}
