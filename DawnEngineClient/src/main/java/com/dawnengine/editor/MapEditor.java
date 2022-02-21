package com.dawnengine.editor;

import com.dawnengine.game.Camera;
import com.dawnengine.game.Game;
import com.dawnengine.game.Input;
import com.dawnengine.game.map.Map;
import com.dawnengine.game.map.Tile;
import com.dawnengine.math.Vector2;
import com.dawnengine.serializers.TilesetLoader;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

public class MapEditor extends Editor {

    private final Game game;
    private final Map map, originalMap;
    private int currentLayer = Tile.GROUND;

    public MapEditor(Game game) {
        initComponents();

        this.game = game;
        this.map = game.getMap();
        this.originalMap = new Map(map);

        var files = TilesetLoader.listAll();
        for (int i = 0; i < files.length; i++) {
            cbTilesets.addItem("Tileset " + (i + 1));
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });
        
        rbGround.setSelected(true);
    }

    public void apply() {
        //TODO: send changes to server.
        dispose();
    }

    public void cancel() {
        game.setMap(originalMap);
        dispose();
    }

    public Map getMap() {
        return map;
    }

    @Override
    public void update() {
        if (Input.getMouseButtonDown(MouseEvent.BUTTON1)
                || Input.isMouseDragging()) {
            var pos = Input.getMousePosition();
            int x = (int) pos.x / Tile.SIZE_X;
            int y = (int) pos.y / Tile.SIZE_Y;

            var tiles = tilesetPanel.getSelectedTiles();
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[i].length; j++) {
                    this.map.setTile(currentLayer,
                            (x + i) + (y + j) * map.getTileCountX(), tiles[i][j]);
                }
            }
        }
    }

    @Override
    public void render(Camera cam) {
        var pos = Input.getMousePosition();
        pos.x = (int) pos.x / Tile.SIZE_X * Tile.SIZE_X;
        pos.y = (int) pos.y / Tile.SIZE_Y * Tile.SIZE_Y;
        cam.drawRect(pos, new Vector2(Tile.SIZE_X, Tile.SIZE_Y));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgLayers = new javax.swing.ButtonGroup();
        cbTilesets = new javax.swing.JComboBox<>();
        scrTileset = new javax.swing.JScrollPane();
        tilesetPanel = new TilesetPanel(this);
        btnCancel = new javax.swing.JButton();
        btnApply = new javax.swing.JButton();
        lblIgnoreColor = new javax.swing.JLabel();
        txtIgnoreColor = new javax.swing.JTextField();
        btnApplyIgnoreColor = new javax.swing.JButton();
        rbGround = new javax.swing.JRadioButton();
        rbMask = new javax.swing.JRadioButton();
        rbMask2 = new javax.swing.JRadioButton();
        rbFringe = new javax.swing.JRadioButton();
        rbFringe2 = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        cbTilesets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTilesetsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tilesetPanelLayout = new javax.swing.GroupLayout(tilesetPanel);
        tilesetPanel.setLayout(tilesetPanelLayout);
        tilesetPanelLayout.setHorizontalGroup(
            tilesetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 494, Short.MAX_VALUE)
        );
        tilesetPanelLayout.setVerticalGroup(
            tilesetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
        );

        scrTileset.setViewportView(tilesetPanel);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnApply.setText("Apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        lblIgnoreColor.setText("Ignore Color:");

        txtIgnoreColor.setText("ff00ff");

        btnApplyIgnoreColor.setText("Apply");
        btnApplyIgnoreColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyIgnoreColorActionPerformed(evt);
            }
        });

        bgLayers.add(rbGround);
        rbGround.setText("Ground");
        rbGround.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbGroundActionPerformed(evt);
            }
        });

        bgLayers.add(rbMask);
        rbMask.setText("Mask");
        rbMask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbMaskActionPerformed(evt);
            }
        });

        bgLayers.add(rbMask2);
        rbMask2.setText("Mask 2");
        rbMask2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbMask2ActionPerformed(evt);
            }
        });

        bgLayers.add(rbFringe);
        rbFringe.setText("Fringe");
        rbFringe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbFringeActionPerformed(evt);
            }
        });

        bgLayers.add(rbFringe2);
        rbFringe2.setText("Fringe 2");
        rbFringe2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbFringe2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(cbTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(scrTileset, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblIgnoreColor)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtIgnoreColor, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnApplyIgnoreColor, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(rbMask, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rbMask2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rbFringe, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rbFringe2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rbGround, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrTileset)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancel)
                            .addComponent(btnApply)
                            .addComponent(lblIgnoreColor)
                            .addComponent(txtIgnoreColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnApplyIgnoreColor))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rbGround)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbMask)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbMask2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbFringe)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbFringe2)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbTilesetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTilesetsActionPerformed
        tilesetPanel.setTileset(TilesetLoader.load(cbTilesets.getSelectedIndex() + 1));
    }//GEN-LAST:event_cbTilesetsActionPerformed

    private void btnApplyIgnoreColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyIgnoreColorActionPerformed
        var txt = txtIgnoreColor.getText();

        if (!txt.startsWith("#")) {
            txt = "#" + txt;
        }

        if (txt.length() != 7) {
            JOptionPane.showMessageDialog(this, "This color is invalid.");
            txtIgnoreColor.setText("#ff00ff");
            return;
        }

        tilesetPanel.updateImage(Color.decode(txt));
    }//GEN-LAST:event_btnApplyIgnoreColorActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        cancel();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
        apply();
    }//GEN-LAST:event_btnApplyActionPerformed

    private void rbGroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbGroundActionPerformed
        currentLayer = Tile.GROUND;
    }//GEN-LAST:event_rbGroundActionPerformed

    private void rbMaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMaskActionPerformed
        currentLayer = Tile.MASK;
    }//GEN-LAST:event_rbMaskActionPerformed

    private void rbMask2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMask2ActionPerformed
        currentLayer = Tile.MASK2;
    }//GEN-LAST:event_rbMask2ActionPerformed

    private void rbFringeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbFringeActionPerformed
        currentLayer = Tile.FRINGE;
    }//GEN-LAST:event_rbFringeActionPerformed

    private void rbFringe2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbFringe2ActionPerformed
        currentLayer = Tile.FRINGE2;
    }//GEN-LAST:event_rbFringe2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgLayers;
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnApplyIgnoreColor;
    private javax.swing.JButton btnCancel;
    private javax.swing.JComboBox<String> cbTilesets;
    private javax.swing.JLabel lblIgnoreColor;
    private javax.swing.JRadioButton rbFringe;
    private javax.swing.JRadioButton rbFringe2;
    private javax.swing.JRadioButton rbGround;
    private javax.swing.JRadioButton rbMask;
    private javax.swing.JRadioButton rbMask2;
    private javax.swing.JScrollPane scrTileset;
    private com.dawnengine.editor.TilesetPanel tilesetPanel;
    private javax.swing.JTextField txtIgnoreColor;
    // End of variables declaration//GEN-END:variables
}
