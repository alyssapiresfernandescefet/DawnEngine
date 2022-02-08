package com.dawnengine.core;

import com.dawnengine.game.Game;
import com.dawnengine.network.Client;
import javax.swing.JOptionPane;

public class GameFrame extends javax.swing.JFrame {
    
    private final Game game;

    public GameFrame(int playerID) {
        initComponents();
        game = new Game(playerID);
        game.setBounds(10, 10, getWidth() - 35, getHeight() - 55);
        getContentPane().add(game);
        this.setVisible(true);
        game.start();
    }

    private void exit() {
        int opt = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?", "Exit?", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.NO_OPTION) {
            return;
        }
        dispose();
        Client.getClient().closeConnection();
        game.stop();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(800, 600));
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
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
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
