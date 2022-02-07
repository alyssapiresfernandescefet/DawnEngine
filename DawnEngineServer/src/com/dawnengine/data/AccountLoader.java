package com.dawnengine.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alyss
 */
public class AccountLoader {

    private static final File accountsDir = new File("data/accounts/");

    static {
        if (!accountsDir.exists()) {
            accountsDir.mkdirs();
        }
    }

    public static Account get(String username) {
        File[] users = accountsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String string) {
                return string.equals(getUserFileWithExtension(username));
            }
        });

        if (users == null || users.length == 0) {
            return null;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(users[0]))) {
            String password = in.readLine();
            return new Account(username, password);
        } catch (IOException ex) {
            Logger.getLogger(AccountLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static boolean set(String username, String password) {
        File user = new File(accountsDir, getUserFileWithExtension(username));

        try {
            user.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(user));
            out.write(password);
            out.newLine();
            out.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(AccountLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static String getUserFileWithExtension(String username) {
        return username + ".bin";
    }
}
