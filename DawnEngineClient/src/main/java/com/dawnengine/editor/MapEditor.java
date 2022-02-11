package com.dawnengine.editor;

import com.dawnengine.game.map.Tileset;
import java.io.File;
import javax.swing.JFrame;

public class MapEditor extends JFrame {

    public MapEditor() {
        initComponents();
        var files = Tileset.listAll();
        for (int i = 0; i < files.length; i++) {
            var file = files[i];
            final int ii = i;
            cbTilesets.addItem(new File(file.getPath()) {
                @Override
                public String toString() {
                    return "Tileset " + (ii + 1);
                }
            });
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSpinner1 = new javax.swing.JSpinner();
        cbTilesets = new javax.swing.JComboBox<>();
        scrTileset = new javax.swing.JScrollPane();
        tilesetPanel = new TilesetPanel(this);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
            .addGap(0, 494, Short.MAX_VALUE)
        );

        scrTileset.setViewportView(tilesetPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrTileset, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(cbTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(88, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scrTileset, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbTilesetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTilesetsActionPerformed
        tilesetPanel.setTileset(Tileset.load(
                cbTilesets.getItemAt(cbTilesets.getSelectedIndex())));
        tilesetPanel.repaint();
    }//GEN-LAST:event_cbTilesetsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<File> cbTilesets;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JScrollPane scrTileset;
    private com.dawnengine.editor.TilesetPanel tilesetPanel;
    // End of variables declaration//GEN-END:variables
}
