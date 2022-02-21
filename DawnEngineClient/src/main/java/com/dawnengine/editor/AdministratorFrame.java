package com.dawnengine.editor;

import com.dawnengine.game.Camera;
import com.dawnengine.game.Game;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class AdministratorFrame extends javax.swing.JFrame {

    private int xPress, yPress, xDrag, yDrag;
    private final Game game;
    private Editor openEditor;

    public AdministratorFrame(Game game) {
        initComponents();
        this.game = game;
        this.setLocationRelativeTo(this.game);
        this.setFocusableWindowState(false);

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                xDrag = e.getX();
                yDrag = e.getY();

                JFrame sFrame = (JFrame) e.getSource();
                sFrame.setLocation(sFrame.getLocation().x + xDrag - xPress,
                        sFrame.getLocation().y + yDrag - yPress);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                xPress = e.getX();
                yPress = e.getY();
            }
        });
    }

    public void updateEditor() {
        if (openEditor == null) {
            return;
        }
        openEditor.update();
    }

    public void renderEditor(Camera cam) {
        if (openEditor == null) {
            return;
        }
        openEditor.render(cam);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (openEditor != null) {
            openEditor.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblAdminPanel = new javax.swing.JLabel();
        btnMapEditor = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setResizable(false);

        lblAdminPanel.setFont(new java.awt.Font("sansserif", 0, 36)); // NOI18N
        lblAdminPanel.setForeground(new java.awt.Color(0, 0, 0));
        lblAdminPanel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAdminPanel.setText("Admin Panel");
        lblAdminPanel.setFocusable(false);
        lblAdminPanel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnMapEditor.setText("Edit Map");
        btnMapEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMapEditorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAdminPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnMapEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAdminPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnMapEditor)
                .addContainerGap(488, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnMapEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMapEditorActionPerformed
        if (openEditor instanceof MapEditor) {
            openEditor.setAlwaysOnTop(true);
            openEditor.setAlwaysOnTop(false);
            return;
        }

        if (openEditor != null) {
            openEditor.dispose();
        }
        
        openEditor = new MapEditor(game);
        openEditor.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                openEditor = null;
            }
        });
        openEditor.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnMapEditorActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMapEditor;
    private javax.swing.JLabel lblAdminPanel;
    // End of variables declaration//GEN-END:variables
}
