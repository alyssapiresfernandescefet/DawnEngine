package com.dawnengine.core;

import com.dawnengine.game.Game;
import com.dawnengine.network.Client;
import javax.swing.JOptionPane;
import org.json.JSONObject;

public class GameFrame extends javax.swing.JFrame {

    private static GameFrame instance;

    private final Game game;

    public GameFrame(JSONObject config) {
        instance = this;
        initComponents();
        game = new Game(config);
        getContentPane().add(game);
        game.setBounds(5, 5, getContentPane().getWidth() - 55,
                getContentPane().getHeight() - 35);
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

    public static GameFrame getInstance() {
        return instance;
    }

    public Game getGame() {
        return game;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exit();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
