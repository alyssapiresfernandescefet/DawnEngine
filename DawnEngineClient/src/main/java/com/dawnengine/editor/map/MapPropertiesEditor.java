package com.dawnengine.editor.map;

import com.dawnengine.game.map.MapMoral;
import com.dawnengine.game.map.Map;
import java.awt.Dimension;

public class MapPropertiesEditor extends javax.swing.JPanel {

    private Map map;

    public MapPropertiesEditor(Map map) {
        initComponents();
        for (MapMoral moral : MapMoral.values()) {
            cmbMoral.addItem(moral);
        }
        lblCurrentMap.setText("Current Map: " + map.getIndex());
        txtName.setText(map.getName());
        cmbMoral.setSelectedIndex(map.getMoral().ordinal());
        txtLinkUp.setText(Integer.toString(map.getLinkUp()));
        txtLinkBottom.setText(Integer.toString(map.getLinkDown()));
        txtLinkRight.setText(Integer.toString(map.getLinkRight()));
        txtLinkLeft.setText(Integer.toString(map.getLinkLeft()));
        txtSizeX.setText(Integer.toString(map.getTileCountX()));
        txtSizeY.setText(Integer.toString(map.getTileCountY()));
    }

    public String getMapName() {
        return txtName.getText();
    }
    
    public int getLinkUp() {
        return Integer.parseInt(txtLinkUp.getText());
    }
    
    public int getLinkDown() {
        return Integer.parseInt(txtLinkBottom.getText());
    }
    
    public int getLinkRight() {
        return Integer.parseInt(txtLinkRight.getText());
    }
    
    public int getLinkLeft() {
        return Integer.parseInt(txtLinkLeft.getText());
    }

    public MapMoral getMoral() {
        return (MapMoral) cmbMoral.getModel().getSelectedItem();
    }

    public Dimension getMapSize() {
        return new Dimension(Integer.parseInt(txtSizeX.getText()),
                Integer.parseInt(txtSizeY.getText()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        pnlLinks = new javax.swing.JPanel();
        lblCurrentMap = new javax.swing.JLabel();
        pnlLinksWrapper = new javax.swing.JPanel();
        txtLinkUp = new javax.swing.JTextField();
        txtLinkBottom = new javax.swing.JTextField();
        txtLinkLeft = new javax.swing.JTextField();
        txtLinkRight = new javax.swing.JTextField();
        pnlSettings = new javax.swing.JPanel();
        cmbMoral = new javax.swing.JComboBox<>();
        lblMoral = new javax.swing.JLabel();
        pnlSize = new javax.swing.JPanel();
        txtSizeX = new javax.swing.JTextField();
        txtSizeY = new javax.swing.JTextField();
        lblSizeX = new javax.swing.JLabel();
        lblSizeY = new javax.swing.JLabel();

        lblName.setText("Name:");

        pnlLinks.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Links", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("sansserif", 0, 12))); // NOI18N

        lblCurrentMap.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentMap.setText("Current Map: 0");

        txtLinkUp.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtLinkUp.setText("100");

        txtLinkBottom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtLinkBottom.setText("100");

        txtLinkLeft.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtLinkLeft.setText("100");

        txtLinkRight.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtLinkRight.setText("100");

        javax.swing.GroupLayout pnlLinksWrapperLayout = new javax.swing.GroupLayout(pnlLinksWrapper);
        pnlLinksWrapper.setLayout(pnlLinksWrapperLayout);
        pnlLinksWrapperLayout.setHorizontalGroup(
            pnlLinksWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLinksWrapperLayout.createSequentialGroup()
                .addGroup(pnlLinksWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLinksWrapperLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtLinkBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLinksWrapperLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtLinkLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtLinkRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(pnlLinksWrapperLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtLinkUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlLinksWrapperLayout.setVerticalGroup(
            pnlLinksWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLinksWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtLinkUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlLinksWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLinkRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLinkLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtLinkBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlLinksLayout = new javax.swing.GroupLayout(pnlLinks);
        pnlLinks.setLayout(pnlLinksLayout);
        pnlLinksLayout.setHorizontalGroup(
            pnlLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblCurrentMap, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlLinksWrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlLinksLayout.setVerticalGroup(
            pnlLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLinksLayout.createSequentialGroup()
                .addComponent(lblCurrentMap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlLinksWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlSettings.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("sansserif", 0, 12))); // NOI18N

        lblMoral.setText("Moral:");

        javax.swing.GroupLayout pnlSettingsLayout = new javax.swing.GroupLayout(pnlSettings);
        pnlSettings.setLayout(pnlSettingsLayout);
        pnlSettingsLayout.setHorizontalGroup(
            pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSettingsLayout.createSequentialGroup()
                .addComponent(lblMoral)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbMoral, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSettingsLayout.setVerticalGroup(
            pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cmbMoral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblMoral))
        );

        pnlSize.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Size", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("sansserif", 0, 12))); // NOI18N

        txtSizeX.setText("100");

        txtSizeY.setText("100");

        lblSizeX.setText("X:");

        lblSizeY.setText("Y:");

        javax.swing.GroupLayout pnlSizeLayout = new javax.swing.GroupLayout(pnlSize);
        pnlSize.setLayout(pnlSizeLayout);
        pnlSizeLayout.setHorizontalGroup(
            pnlSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnlSizeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlSizeLayout.createSequentialGroup()
                        .addComponent(lblSizeX)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSizeX, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSizeLayout.createSequentialGroup()
                        .addComponent(lblSizeY)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSizeY)))
                .addContainerGap(140, Short.MAX_VALUE))
        );
        pnlSizeLayout.setVerticalGroup(
            pnlSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSizeLayout.createSequentialGroup()
                .addGroup(pnlSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSizeX)
                    .addComponent(txtSizeX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSizeY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSizeY))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlLinks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnlLinks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<MapMoral> cmbMoral;
    private javax.swing.JLabel lblCurrentMap;
    private javax.swing.JLabel lblMoral;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblSizeX;
    private javax.swing.JLabel lblSizeY;
    private javax.swing.JPanel pnlLinks;
    private javax.swing.JPanel pnlLinksWrapper;
    private javax.swing.JPanel pnlSettings;
    private javax.swing.JPanel pnlSize;
    private javax.swing.JTextField txtLinkBottom;
    private javax.swing.JTextField txtLinkLeft;
    private javax.swing.JTextField txtLinkRight;
    private javax.swing.JTextField txtLinkUp;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtSizeX;
    private javax.swing.JTextField txtSizeY;
    // End of variables declaration//GEN-END:variables
}
