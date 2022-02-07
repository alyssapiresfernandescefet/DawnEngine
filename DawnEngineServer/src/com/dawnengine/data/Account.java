package com.dawnengine.data;

/**
 *
 * @author alyss
 */
public class Account {

    public final String username, password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" + "username=" + username + ", password=" + password + '}';
    }

}
