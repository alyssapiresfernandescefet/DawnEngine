package com.dawnengine.editor;

import com.dawnengine.game.Camera;
import com.dawnengine.game.Game;
import com.dawnengine.game.Input;
import com.dawnengine.game.map.Map;
import com.dawnengine.game.map.Tile;
import com.dawnengine.math.Vector2;
import com.dawnengine.network.Client;
import com.dawnengine.network.ClientPackets;
import com.dawnengine.serializers.MapSerializer;
import com.dawnengine.serializers.TilesetLoader;
import com.dawnengine.serializers.objects.MapData;
import java.awt.CardLayout;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import javax.swing.JOptionPane;
import org.json.JSONObject;

public class MapEditor extends Editor {

    private final Game game;
    private final Map map, originalMap;
    private int currentLayer = Tile.GROUND;
    private CardLayout optionsLayout;

    public MapEditor(Game game) {
        initComponents();
        this.optionsLayout = (CardLayout) pnlOptions.getLayout();
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
        rbLayers.setSelected(true);
    }

    public void save() {
        var obj = new JSONObject();
        var index = map.getIndex();
        var name = map.getName();
        var sizeX = map.getTileCountX();
        var sizeY = map.getTileCountY();
        var lastRevision = new Date().getTime();
        var tiles = map.getSerializedTiles();
        
        var mapData = new MapData(name, sizeX, sizeY, lastRevision, tiles);
        
        obj.put("mapIndex", index);
        obj.put("name", name);
        obj.put("sizeX", sizeX);
        obj.put("sizeY", sizeY);
        obj.put("lastRevision", lastRevision);
        obj.put("tiles", tiles);
        Client.getClient().sendPacket(ClientPackets.UPDATE_MAP_REQUEST, obj);
        MapSerializer.save(index, mapData);
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
        if (((Input.isMouseDragging() && Input.getMouseButton(MouseEvent.BUTTON1))
                || Input.getMouseButtonDown(MouseEvent.BUTTON1))) {
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
        } else if ((Input.isMouseDragging() && Input.getMouseButton(MouseEvent.BUTTON3))
                || Input.getMouseButtonDown(MouseEvent.BUTTON3)) {
            var pos = Input.getMousePosition();
            int x = (int) pos.x / Tile.SIZE_X;
            int y = (int) pos.y / Tile.SIZE_Y;
            this.map.setTile(currentLayer, x + y * map.getTileCountX(), null);
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
        bgTypes = new javax.swing.ButtonGroup();
        cbTilesets = new javax.swing.JComboBox<>();
        scrTileset = new javax.swing.JScrollPane();
        tilesetPanel = new com.dawnengine.editor.TilesetPanel();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnFill = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        separator2 = new javax.swing.JSeparator();
        rbLayers = new javax.swing.JRadioButton();
        rbAttributes = new javax.swing.JRadioButton();
        rbBlock = new javax.swing.JRadioButton();
        separator1 = new javax.swing.JSeparator();
        pnlOptions = new javax.swing.JPanel();
        pnlLayers = new javax.swing.JPanel();
        rbGround = new javax.swing.JRadioButton();
        rbMask = new javax.swing.JRadioButton();
        rbMask2 = new javax.swing.JRadioButton();
        rbFringe = new javax.swing.JRadioButton();
        rbFringe2 = new javax.swing.JRadioButton();
        pnlAttributes = new javax.swing.JPanel();
        pnlBlock = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        cbTilesets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTilesetsActionPerformed(evt);
            }
        });

        tilesetPanel.setPreferredSize(new java.awt.Dimension(394, 504));

        javax.swing.GroupLayout tilesetPanelLayout = new javax.swing.GroupLayout(tilesetPanel);
        tilesetPanel.setLayout(tilesetPanelLayout);
        tilesetPanelLayout.setHorizontalGroup(
            tilesetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
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

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnFill.setText("Fill");
        btnFill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFillActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        bgTypes.add(rbLayers);
        rbLayers.setText("Layers");
        rbLayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbLayersActionPerformed(evt);
            }
        });

        bgTypes.add(rbAttributes);
        rbAttributes.setText("Attributes");
        rbAttributes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAttributesActionPerformed(evt);
            }
        });

        bgTypes.add(rbBlock);
        rbBlock.setText("Block");
        rbBlock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbBlockActionPerformed(evt);
            }
        });

        pnlOptions.setLayout(new java.awt.CardLayout());

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

        javax.swing.GroupLayout pnlLayersLayout = new javax.swing.GroupLayout(pnlLayers);
        pnlLayers.setLayout(pnlLayersLayout);
        pnlLayersLayout.setHorizontalGroup(
            pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLayersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(rbMask, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbMask2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbFringe, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbFringe2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbGround, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlLayersLayout.setVerticalGroup(
            pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLayersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbGround)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbMask)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbMask2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbFringe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbFringe2)
                .addContainerGap(180, Short.MAX_VALUE))
        );

        pnlOptions.add(pnlLayers, "layers");

        javax.swing.GroupLayout pnlAttributesLayout = new javax.swing.GroupLayout(pnlAttributes);
        pnlAttributes.setLayout(pnlAttributesLayout);
        pnlAttributesLayout.setHorizontalGroup(
            pnlAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 76, Short.MAX_VALUE)
        );
        pnlAttributesLayout.setVerticalGroup(
            pnlAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pnlOptions.add(pnlAttributes, "attributes");

        javax.swing.GroupLayout pnlBlockLayout = new javax.swing.GroupLayout(pnlBlock);
        pnlBlock.setLayout(pnlBlockLayout);
        pnlBlockLayout.setHorizontalGroup(
            pnlBlockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 76, Short.MAX_VALUE)
        );
        pnlBlockLayout.setVerticalGroup(
            pnlBlockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pnlOptions.add(pnlBlock, "block");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(239, 239, 239)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrTileset, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnFill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                .addComponent(rbLayers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rbAttributes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(rbBlock, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(pnlOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(separator1)
                            .addComponent(separator2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbTilesets, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(pnlOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(separator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbLayers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbAttributes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbBlock)
                        .addGap(18, 18, 18)
                        .addComponent(separator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFill))
                    .addComponent(scrTileset, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbTilesetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTilesetsActionPerformed
        tilesetPanel.setTileset(TilesetLoader.load(cbTilesets.getSelectedIndex() + 1));
    }//GEN-LAST:event_cbTilesetsActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        cancel();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

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

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to clear "
                + "this layer?", "Clear Layer", JOptionPane.YES_NO_OPTION)
                != JOptionPane.YES_OPTION) {
            return;
        }
        this.map.setTiles(currentLayer, null);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnFillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFillActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to fill "
                + "this layer?", "Fill Layer", JOptionPane.YES_NO_OPTION)
                != JOptionPane.YES_OPTION) {
            return;
        }
        var fillTile = tilesetPanel.getSelectedTiles()[0][0];
        this.map.setTiles(currentLayer, fillTile);
    }//GEN-LAST:event_btnFillActionPerformed

    private void rbLayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbLayersActionPerformed
        optionsLayout.show(pnlOptions, "layers");
    }//GEN-LAST:event_rbLayersActionPerformed

    private void rbAttributesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAttributesActionPerformed
        optionsLayout.show(pnlOptions, "attributes");
    }//GEN-LAST:event_rbAttributesActionPerformed

    private void rbBlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbBlockActionPerformed
        optionsLayout.show(pnlOptions, "block");
    }//GEN-LAST:event_rbBlockActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgLayers;
    private javax.swing.ButtonGroup bgTypes;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnFill;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbTilesets;
    private javax.swing.JPanel pnlAttributes;
    private javax.swing.JPanel pnlBlock;
    private javax.swing.JPanel pnlLayers;
    private javax.swing.JPanel pnlOptions;
    private javax.swing.JRadioButton rbAttributes;
    private javax.swing.JRadioButton rbBlock;
    private javax.swing.JRadioButton rbFringe;
    private javax.swing.JRadioButton rbFringe2;
    private javax.swing.JRadioButton rbGround;
    private javax.swing.JRadioButton rbLayers;
    private javax.swing.JRadioButton rbMask;
    private javax.swing.JRadioButton rbMask2;
    private javax.swing.JScrollPane scrTileset;
    private javax.swing.JSeparator separator1;
    private javax.swing.JSeparator separator2;
    private com.dawnengine.editor.TilesetPanel tilesetPanel;
    // End of variables declaration//GEN-END:variables
}
