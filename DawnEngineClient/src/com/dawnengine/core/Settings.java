/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dawnengine.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alyss
 */
public class Settings {

    private static final File configFile = new File("engine.config");
    private static final Properties props = new Properties();
    private static final String defaultConfigs
            = "app.name=Dawn Engine\n"
            + "user.name=\n"
            + "user.password=\n"
            + "user.savepassword=false\n"
            + "server.ip=localhost\n"
            + "server.tcpport=3001\n"
            + "server.udpport=3002\n";

    private static void load() {
        try {
            if (!configFile.exists()) {
                var out = new BufferedWriter(new FileWriter(configFile));
                out.write(defaultConfigs);
                out.close();
                load();
                return;
            }
            var in = new FileInputStream(configFile);
            props.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        if (props.isEmpty()) {
            load();
        }
        return props.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        if (props.isEmpty()) {
            load();
        }
        props.setProperty(key, value);
        try {
            var out = new BufferedWriter(new FileWriter(configFile));
            props.store(out, null);
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
