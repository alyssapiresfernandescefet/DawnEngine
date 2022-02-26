package com.dawnengine.managers;

import com.dawnengine.data.PlayerData;
import com.dawnengine.utils.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alyss
 */
public class PlayerManager {

    private static final File accountsDir = new File("data/accounts/");

    static {
        if (!accountsDir.exists()) {
            accountsDir.mkdirs();
        }
    }

    public static PlayerData load(int id, String username) {
        File[] users = accountsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String string) {
                return string.equals(username + ".bin");
            }
        });

        if (users.length == 0) {
            return null;
        }

        PlayerData pd = null;
        try (var in = new Input(new FileInputStream(users[0]))) {
            pd = Serializer.readObject(in, PlayerData.class);
            pd.setID(id);
        } catch (Exception ex) {
            Logger.getLogger(PlayerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pd;
    }

    public static boolean save(PlayerData pd) {
        File user = new File(accountsDir,
                pd.getAccount().username + ".bin");

        try (var out = new Output(new FileOutputStream(user))) {
            Serializer.writeObject(out, pd);
        } catch (IOException ex) {
            Logger.getLogger(PlayerManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public static boolean exists(String username) {
        File[] users = accountsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String string) {
                return string.equals(username + ".bin");
            }
        });
        
        return users.length == 1;
    }
}
