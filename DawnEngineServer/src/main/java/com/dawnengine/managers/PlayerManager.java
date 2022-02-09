package com.dawnengine.managers;

import com.dawnengine.data.PlayerData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

/**
 *
 * @author alyss
 */
public class PlayerManager {

    private static final File accountsDir = new File("data/accounts/");
    private static final ArrayList<PlayerData> cachedPlayers = new ArrayList<>();

    static {
        if (!accountsDir.exists()) {
            accountsDir.mkdirs();
        }
    }

    public static PlayerData load(String username) {
        File[] users = accountsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String string) {
                return string.equals(getUserFileWithExtension(username));
            }
        });

        if (users == null || users.length == 0) {
            return null;
        }

        try (FSTObjectInput in = new FSTObjectInput(new FileInputStream(users[0]))) {
            PlayerData pd = (PlayerData) in.readObject(PlayerData.class);
            cachedPlayers.add(pd);
            return pd;
        } catch (Exception ex) {
            Logger.getLogger(PlayerManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static boolean save(PlayerData player) {
        File user = new File(accountsDir,
                getUserFileWithExtension(player.getAccount().username));

        try (FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(user))) {
            out.writeObject(user);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PlayerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static String getUserFileWithExtension(String username) {
        return username + ".bin";
    }
    
    public static PlayerData getCachedPlayer(int id) {
        for (int i = 0; i < cachedPlayers.size(); i++) {
            var p = cachedPlayers.get(i);
            if (p.id() == id) {
                cachedPlayers.remove(i);
                return p;
            }
        }
        return null;
    }
}
