package com.dawnengine.core;

import com.dawnengine.game.Game;
import com.dawnengine.network.Client;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author alyss
 */
public class GameFrame extends JFrame {
    public static final int GAME_WIDTH = 960, GAME_HEIGHT = 640;

    private final Game game;

    public GameFrame() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        
        game = Game.get();
        game.setBounds(0, 0, GAME_WIDTH - 10, GAME_HEIGHT - 10);
        getContentPane().add(game);
    }

    private void exit() {
//        int opt = JOptionPane.showConfirmDialog(this,
//                "Are you sure you want to exit?", "Exit?", JOptionPane.YES_NO_OPTION);
//        if (opt == JOptionPane.NO_OPTION) {
//            return;
//        }
        dispose();
        Client.getClient().closeConnection();
        game.stop();
    }
}
