package com.dawnengine.editor.map;

import com.dawnengine.editor.Editor;
import com.dawnengine.game.graphics.Camera;
import com.dawnengine.game.Game;
import com.dawnengine.game.Input;
import com.dawnengine.game.graphics.StringRenderPosition;
import com.dawnengine.game.map.Map;
import com.dawnengine.game.map.Tile;
import com.dawnengine.game.map.TileAttribute;
import com.dawnengine.math.Vector2;
import com.dawnengine.network.Client;
import com.dawnengine.network.NetworkPackets;
import com.dawnengine.serializers.MapSerializer;
import com.dawnengine.serializers.TilesetLoader;
import com.dawnengine.serializers.objects.MapData;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.json.JSONObject;

public class MapEditor extends Editor {

    private static final Font ATTRIBUTES_FONT = new Font("Verdana", Font.PLAIN, 16);

    private final MapPropertiesEditor propertiesEditor;
    private final Game game;
    private final Map map, originalMap;

    private int currentLayer = Tile.GROUND;
    private TileAttribute currentAttribute = TileAttribute.Block;
    private CardLayout optionsLayout;
    private String currentOption = "layers";

    public MapEditor(Game game) {
        initComponents();
        this.optionsLayout = (CardLayout) pnlOptions.getLayout();
        this.game = game;
        this.map = game.getMap();
        this.originalMap = new Map(map);
        this.propertiesEditor = new MapPropertiesEditor(this.map);

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

        rbLayerGround.setSelected(true);
        rbLayers.setSelected(true);
        rbAttributeBlock.setSelected(true);
    }

    public void save() {
        dispose();

        var req = new JSONObject();
        var index = map.getIndex();
        var lastRevision = new Date().getTime();
        map.setLastRevision(lastRevision);
        var mapData = new MapData(map);
        req.put("name", mapData.getName());
        req.put("moral", mapData.getMoral());
        req.put("lUp", mapData.getLinkUp());
        req.put("lDown", mapData.getLinkDown());
        req.put("lRight", mapData.getLinkRight());
        req.put("lLeft", mapData.getLinkLeft());
        req.put("sizeX", mapData.getTileCountX());
        req.put("sizeY", mapData.getTileCountY());
        req.put("tiles", mapData.getTiles());
        req.put("mapIndex", index);
        req.put("lastRevision", lastRevision);

        JOptionPane pane = new JOptionPane("Loading...",
                JOptionPane.INFORMATION_MESSAGE);

        JDialog dialog = new JDialog();
        dialog.setTitle("Loading Map");
        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        dialog.setContentPane(pane);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
        Client.getClient().sendPacket(NetworkPackets.CLIENT_UPDATE_MAP_REQUEST, req,
                NetworkPackets.SERVER_UPDATE_MAP_RESPONSE, ctx -> {
                    var res = ctx.response();
                    var accept = res.getBoolean("accept");
                    if (!accept) {
                        var message = res.getString("message");
                        JOptionPane.showMessageDialog(null,
                                "The operation could not be performed. Reason: " + message,
                                "Update Map Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    MapSerializer.save(index, mapData);
                    dialog.dispose();
                });
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
        switch (currentOption) {
            case "layers":
                handleLayersUpdate();
                break;
            case "attributes":
                handleAttributesUpdate();
                break;
            case "block":
                handleBlockUpdate();
                break;
        }
    }

    private void handleLayersUpdate() {
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

    private void handleAttributesUpdate() {
        if (((Input.isMouseDragging() && Input.getMouseButton(MouseEvent.BUTTON1))
                || Input.getMouseButtonDown(MouseEvent.BUTTON1))) {
            var pos = Input.getMousePosition();
            int x = (int) pos.x / Tile.SIZE_X;
            int y = (int) pos.y / Tile.SIZE_Y;

            this.map.setAttribute(x, y, currentAttribute);
        } else if ((Input.isMouseDragging() && Input.getMouseButton(MouseEvent.BUTTON3))
                || Input.getMouseButtonDown(MouseEvent.BUTTON3)) {
            var pos = Input.getMousePosition();
            int x = (int) pos.x / Tile.SIZE_X;
            int y = (int) pos.y / Tile.SIZE_Y;

            this.map.setAttribute(x, y, TileAttribute.None);
        }
    }

    private void handleBlockUpdate() {

    }

    @Override
    public void render(Camera cam) {
        switch (currentOption) {
            case "layers":
                handleLayersRender(cam);
                break;
            case "attributes":
                handleAttributesRender(cam);
                break;
            case "block":
                handleBlockRender(cam);
                break;
        }
    }

    private void handleLayersRender(Camera cam) {
        var pos = Input.getMousePosition();
        pos.x = (int) pos.x / Tile.SIZE_X * Tile.SIZE_X;
        pos.y = (int) pos.y / Tile.SIZE_Y * Tile.SIZE_Y;
        cam.drawRect(pos, new Vector2(Tile.SIZE_X, Tile.SIZE_Y));
    }

    private void handleAttributesRender(Camera cam) {
        handleLayersRender(cam);
        var pos = map.getTilesPositions();
        for (int i = 0; i < pos.length; i++) {
            var attr = map.getAttribute(i);
            cam.setColor(attr.color);
            cam.setFont(ATTRIBUTES_FONT);
            cam.setStringRenderPosition(StringRenderPosition.Center);
            cam.drawString(attr.abbreviation, pos[i]);
        }
    }

    private void handleBlockRender(Camera cam) {

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgLayers = new javax.swing.ButtonGroup();
        bgTypes = new javax.swing.ButtonGroup();
        bgAttributes = new javax.swing.ButtonGroup();
        cbTilesets = new javax.swing.JComboBox<>();
        scrTileset = new javax.swing.JScrollPane();
        tilesetPanel = new com.dawnengine.editor.map.TilesetPanel();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        separator2 = new javax.swing.JSeparator();
        rbLayers = new javax.swing.JRadioButton();
        rbAttributes = new javax.swing.JRadioButton();
        rbBlock = new javax.swing.JRadioButton();
        separator1 = new javax.swing.JSeparator();
        pnlOptions = new javax.swing.JPanel();
        pnlLayers = new javax.swing.JPanel();
        rbLayerGround = new javax.swing.JRadioButton();
        rbLayerMask = new javax.swing.JRadioButton();
        rbLayerMask2 = new javax.swing.JRadioButton();
        rbLayerFringe = new javax.swing.JRadioButton();
        rbLayerFringe2 = new javax.swing.JRadioButton();
        btnLayerClear = new javax.swing.JButton();
        btnLayerFill = new javax.swing.JButton();
        pnlAttributes = new javax.swing.JPanel();
        rbAttributeBlock = new javax.swing.JRadioButton();
        rbAttributeWarp = new javax.swing.JRadioButton();
        rbAttributeItem = new javax.swing.JRadioButton();
        rbAttributeNPCBlock = new javax.swing.JRadioButton();
        pnlBlock = new javax.swing.JPanel();
        btnProperties = new javax.swing.JButton();

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

        bgLayers.add(rbLayerGround);
        rbLayerGround.setText("Ground");
        rbLayerGround.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbLayerGroundActionPerformed(evt);
            }
        });

        bgLayers.add(rbLayerMask);
        rbLayerMask.setText("Mask");
        rbLayerMask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbLayerMaskActionPerformed(evt);
            }
        });

        bgLayers.add(rbLayerMask2);
        rbLayerMask2.setText("Mask 2");
        rbLayerMask2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbLayerMask2ActionPerformed(evt);
            }
        });

        bgLayers.add(rbLayerFringe);
        rbLayerFringe.setText("Fringe");
        rbLayerFringe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbLayerFringeActionPerformed(evt);
            }
        });

        bgLayers.add(rbLayerFringe2);
        rbLayerFringe2.setText("Fringe 2");
        rbLayerFringe2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbLayerFringe2ActionPerformed(evt);
            }
        });

        btnLayerClear.setText("Clear");
        btnLayerClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayerClearActionPerformed(evt);
            }
        });

        btnLayerFill.setText("Fill");
        btnLayerFill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayerFillActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlLayersLayout = new javax.swing.GroupLayout(pnlLayers);
        pnlLayers.setLayout(pnlLayersLayout);
        pnlLayersLayout.setHorizontalGroup(
            pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLayersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLayerClear, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                    .addComponent(btnLayerFill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLayersLayout.createSequentialGroup()
                .addGroup(pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(rbLayerFringe2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                    .addComponent(rbLayerFringe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbLayerMask2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbLayerMask, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbLayerGround, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlLayersLayout.setVerticalGroup(
            pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLayersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbLayerGround)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbLayerMask)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbLayerMask2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbLayerFringe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbLayerFringe2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
                .addComponent(btnLayerClear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLayerFill))
        );

        pnlOptions.add(pnlLayers, "layers");

        bgAttributes.add(rbAttributeBlock);
        rbAttributeBlock.setText("Block");
        rbAttributeBlock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAttributeBlockActionPerformed(evt);
            }
        });

        bgAttributes.add(rbAttributeWarp);
        rbAttributeWarp.setText("Warp");
        rbAttributeWarp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAttributeWarpActionPerformed(evt);
            }
        });

        bgAttributes.add(rbAttributeItem);
        rbAttributeItem.setText("Item");
        rbAttributeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAttributeItemActionPerformed(evt);
            }
        });

        bgAttributes.add(rbAttributeNPCBlock);
        rbAttributeNPCBlock.setText("NPC Block");
        rbAttributeNPCBlock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAttributeNPCBlockActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAttributesLayout = new javax.swing.GroupLayout(pnlAttributes);
        pnlAttributes.setLayout(pnlAttributesLayout);
        pnlAttributesLayout.setHorizontalGroup(
            pnlAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAttributesLayout.createSequentialGroup()
                .addGroup(pnlAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(rbAttributeNPCBlock, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbAttributeItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbAttributeWarp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbAttributeBlock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlAttributesLayout.setVerticalGroup(
            pnlAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAttributesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbAttributeBlock)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbAttributeWarp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbAttributeItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbAttributeNPCBlock)
                .addContainerGap(260, Short.MAX_VALUE))
        );

        pnlOptions.add(pnlAttributes, "attributes");

        javax.swing.GroupLayout pnlBlockLayout = new javax.swing.GroupLayout(pnlBlock);
        pnlBlock.setLayout(pnlBlockLayout);
        pnlBlockLayout.setHorizontalGroup(
            pnlBlockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 85, Short.MAX_VALUE)
        );
        pnlBlockLayout.setVerticalGroup(
            pnlBlockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 356, Short.MAX_VALUE)
        );

        pnlOptions.add(pnlBlock, "block");

        btnProperties.setText("Properties");
        btnProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPropertiesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnProperties)
                                .addGap(151, 151, 151)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(scrTileset, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(pnlOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(rbLayers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(rbAttributes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(rbBlock, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12))
                            .addComponent(separator1)
                            .addComponent(separator2, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbTilesets, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(separator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbLayers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbAttributes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbBlock)
                        .addGap(18, 18, 18)
                        .addComponent(separator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrTileset, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel)
                    .addComponent(btnProperties))
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

    private void rbLayerGroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbLayerGroundActionPerformed
        currentLayer = Tile.GROUND;
    }//GEN-LAST:event_rbLayerGroundActionPerformed

    private void rbLayerMaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbLayerMaskActionPerformed
        currentLayer = Tile.MASK;
    }//GEN-LAST:event_rbLayerMaskActionPerformed

    private void rbLayerMask2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbLayerMask2ActionPerformed
        currentLayer = Tile.MASK2;
    }//GEN-LAST:event_rbLayerMask2ActionPerformed

    private void rbLayerFringeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbLayerFringeActionPerformed
        currentLayer = Tile.FRINGE;
    }//GEN-LAST:event_rbLayerFringeActionPerformed

    private void rbLayerFringe2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbLayerFringe2ActionPerformed
        currentLayer = Tile.FRINGE2;
    }//GEN-LAST:event_rbLayerFringe2ActionPerformed

    private void btnLayerClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayerClearActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to clear "
                + "this layer?", "Clear Layer", JOptionPane.YES_NO_OPTION)
                != JOptionPane.YES_OPTION) {
            return;
        }
        this.map.setTiles(currentLayer, null);
    }//GEN-LAST:event_btnLayerClearActionPerformed

    private void btnLayerFillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayerFillActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to fill "
                + "this layer?", "Fill Layer", JOptionPane.YES_NO_OPTION)
                != JOptionPane.YES_OPTION) {
            return;
        }
        var fillTile = tilesetPanel.getSelectedTiles()[0][0];
        this.map.setTiles(currentLayer, fillTile);
    }//GEN-LAST:event_btnLayerFillActionPerformed

    private void rbLayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbLayersActionPerformed
        optionsLayout.show(pnlOptions, "layers");
        currentOption = "layers";
    }//GEN-LAST:event_rbLayersActionPerformed

    private void rbAttributesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAttributesActionPerformed
        optionsLayout.show(pnlOptions, "attributes");
        currentOption = "attributes";
    }//GEN-LAST:event_rbAttributesActionPerformed

    private void rbBlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbBlockActionPerformed
        optionsLayout.show(pnlOptions, "block");
        currentOption = "block";
    }//GEN-LAST:event_rbBlockActionPerformed

    private void btnPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPropertiesActionPerformed
        int result = JOptionPane.showOptionDialog(null, propertiesEditor,
                "Properties", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        //TODO: process properties
    }//GEN-LAST:event_btnPropertiesActionPerformed

    private void rbAttributeBlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAttributeBlockActionPerformed
        currentAttribute = TileAttribute.Block;
    }//GEN-LAST:event_rbAttributeBlockActionPerformed

    private void rbAttributeWarpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAttributeWarpActionPerformed
        currentAttribute = TileAttribute.Block;
    }//GEN-LAST:event_rbAttributeWarpActionPerformed

    private void rbAttributeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAttributeItemActionPerformed
        currentAttribute = TileAttribute.Block;
    }//GEN-LAST:event_rbAttributeItemActionPerformed

    private void rbAttributeNPCBlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAttributeNPCBlockActionPerformed
        currentAttribute = TileAttribute.Block;
    }//GEN-LAST:event_rbAttributeNPCBlockActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgAttributes;
    private javax.swing.ButtonGroup bgLayers;
    private javax.swing.ButtonGroup bgTypes;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnLayerClear;
    private javax.swing.JButton btnLayerFill;
    private javax.swing.JButton btnProperties;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbTilesets;
    private javax.swing.JPanel pnlAttributes;
    private javax.swing.JPanel pnlBlock;
    private javax.swing.JPanel pnlLayers;
    private javax.swing.JPanel pnlOptions;
    private javax.swing.JRadioButton rbAttributeBlock;
    private javax.swing.JRadioButton rbAttributeItem;
    private javax.swing.JRadioButton rbAttributeNPCBlock;
    private javax.swing.JRadioButton rbAttributeWarp;
    private javax.swing.JRadioButton rbAttributes;
    private javax.swing.JRadioButton rbBlock;
    private javax.swing.JRadioButton rbLayerFringe;
    private javax.swing.JRadioButton rbLayerFringe2;
    private javax.swing.JRadioButton rbLayerGround;
    private javax.swing.JRadioButton rbLayerMask;
    private javax.swing.JRadioButton rbLayerMask2;
    private javax.swing.JRadioButton rbLayers;
    private javax.swing.JScrollPane scrTileset;
    private javax.swing.JSeparator separator1;
    private javax.swing.JSeparator separator2;
    private com.dawnengine.editor.map.TilesetPanel tilesetPanel;
    // End of variables declaration//GEN-END:variables
}
