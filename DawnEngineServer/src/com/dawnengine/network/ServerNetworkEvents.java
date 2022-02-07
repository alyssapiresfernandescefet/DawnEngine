package com.dawnengine.network;

import com.dawnengine.data.Account;
import com.dawnengine.data.AccountLoader;
import com.dawnengine.utils.Encrypter;
import org.json.JSONObject;

public class ServerNetworkEvents {

    public static void onClientLoginRequest(NetworkContext ctx) {
        String username = ctx.json.getString("username");
        String password = ctx.json.getString("password");

        String invalidReason = null;

        Account acc = AccountLoader.get(username);

        if (username.isBlank() || password.isBlank()) {
            invalidReason = "Username or password is blank!";
        } else if (acc == null || !Encrypter.compare(password, acc.password)) {
            invalidReason = "Username or password is invalid!";
        }

        JSONObject obj = new JSONObject();
        obj.put("code", ServerNetworkPackets.LOGIN_RESPONSE.code);
        obj.put("accept", invalidReason == null);
        if (invalidReason != null) {
            obj.put("reason", invalidReason);
        }
        obj.put("playerID", ctx.connection.getID());

        ctx.connection.sendTCP(obj.toString());
    }

    public static void onClientRegisterRequest(NetworkContext ctx) {
        String username = ctx.json.getString("username");
        String password = ctx.json.getString("password");

        String invalidReason = null;

        Account acc = AccountLoader.get(username);

        if (username.isBlank() || password.isBlank()) {
            invalidReason = "Username or password is blank!";
        } else if (acc != null) {
            invalidReason = "There is already another player with this username!";
        }

        JSONObject obj = new JSONObject();

        if (invalidReason == null) {
            String hashedPass = Encrypter.encrypt(password);
            AccountLoader.set(username, hashedPass);
        } else {
            obj.put("reason", invalidReason);
        }

        obj.put("code", ServerNetworkPackets.REGISTER_RESPONSE.code);
        obj.put("accept", invalidReason == null);
        obj.put("playerID", ctx.connection.getID());

        ctx.connection.sendTCP(obj.toString());
    }
}
