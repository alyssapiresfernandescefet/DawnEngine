package com.dawnengine.core;

import com.dawnengine.game.Game;
import com.dawnengine.game.map.Tile;
import com.dawnengine.network.Client;
import javax.swing.JFrame;

/**
 *
 * @author alyss
 */
public class GameFrame extends JFrame {
    
    public static final int GAME_WIDTH = Tile.SIZE_X * 25,
            GAME_HEIGHT = Tile.SIZE_Y * 15;

    private final Game game;

    public GameFrame() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        game = Game.get();
        game.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
        getContentPane().add(game);

        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void dispose() {
        //        int opt = JOptionPane.showConfirmDialog(this,
//                "Are you sure you want to exit?", "Exit?", JOptionPane.YES_NO_OPTION);
//        if (opt == JOptionPane.NO_OPTION) {
//            return;
//        }
        Client.get().closeConnection();
        game.stop();
        super.dispose();
    }
}
