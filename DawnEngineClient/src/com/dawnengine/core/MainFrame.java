package com.dawnengine.core;

import com.dawnengine.network.Client;
import com.dawnengine.network.ClientNetworkPackets;
import com.dawnengine.utils.JSON;
import java.awt.CardLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class MainFrame extends javax.swing.JFrame {

    private static MainFrame instance;
    private final CardLayout cards;
    private String currentCard = "home";

    public MainFrame() {
        this.setTitle(Settings.getProperty("app.name"));
        initComponents();
        cards = (CardLayout) pnlCards.getLayout();
        cards.show(pnlCards, "home");

        txtOptionsIP.setText(Settings.getProperty("server.ip"));
        txtOptionsTCP.setText(Settings.getProperty("server.tcpport"));
        txtOptionsUDP.setText(Settings.getProperty("server.udpport"));
        txtLoginUsername.setText(Settings.getProperty("user.name"));
        pswLoginPassword.setText(Settings.getProperty("user.password"));
        rbSavePassword.setSelected(Boolean.parseBoolean(
                Settings.getProperty("user.savepassword")));
    }

    public void onLoginComplete(boolean accept, String reason, int playerID) {
        if (!accept) {
            JOptionPane.showMessageDialog(this, reason);
            return;
        }
        dispose();
        GameFrame game = new GameFrame(playerID);
        game.setVisible(true);
    }

    public void onRegisterComplete(boolean accept, String reason, int playerID) {
        onLoginComplete(accept, reason, playerID);
    }

    public static MainFrame getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameFrame(1);
//                instance = new MainFrame();
//                instance.setVisible(true);
            }
        });
    }

    private void exit() {
        Client.getClient().closeConnection();
        dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        pnlCards = new javax.swing.JPanel();
        pnlHome = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        pnlRegister = new javax.swing.JPanel();
        lblRegisterUsername = new javax.swing.JLabel();
        txtRegisterUsername = new javax.swing.JTextField();
        lblRegisterPassword = new javax.swing.JLabel();
        lblRegister = new javax.swing.JLabel();
        pswRegisterPassword = new javax.swing.JPasswordField();
        lblRegisterConfirmPassword = new javax.swing.JLabel();
        pswRegisterConfirmPassword = new javax.swing.JPasswordField();
        pnlOptions = new javax.swing.JPanel();
        lblOptionsIP = new javax.swing.JLabel();
        lblOptionsTCP = new javax.swing.JLabel();
        lblOptionsUDP = new javax.swing.JLabel();
        txtOptionsIP = new javax.swing.JTextField();
        txtOptionsTCP = new javax.swing.JTextField();
        txtOptionsUDP = new javax.swing.JTextField();
        lblSave = new javax.swing.JLabel();
        pnlLogin = new javax.swing.JPanel();
        lblLoginUsername = new javax.swing.JLabel();
        txtLoginUsername = new javax.swing.JTextField();
        lblLoginPassword = new javax.swing.JLabel();
        rbSavePassword = new javax.swing.JRadioButton();
        lblLoginEnter = new javax.swing.JLabel();
        pswLoginPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        btnOptions = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();

        jScrollPane1.setViewportView(jTree1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Dawn Engine");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlCards.setLayout(new java.awt.CardLayout());

        pnlHome.setBackground(new java.awt.Color(0, 0, 0));

        lblWelcome.setBackground(new java.awt.Color(255, 255, 255));
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWelcome.setText("Welcome to Dawn Engine!");
        lblWelcome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlHomeLayout = new javax.swing.GroupLayout(pnlHome);
        pnlHome.setLayout(pnlHomeLayout);
        pnlHomeLayout.setHorizontalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlHomeLayout.setVerticalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlCards.add(pnlHome, "home");

        pnlRegister.setBackground(new java.awt.Color(0, 0, 0));

        lblRegisterUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblRegisterUsername.setText("Username:");

        lblRegisterPassword.setForeground(new java.awt.Color(255, 255, 255));
        lblRegisterPassword.setText("Password:");

        lblRegister.setForeground(new java.awt.Color(255, 255, 255));
        lblRegister.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRegister.setText("Register");
        lblRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegisterMouseClicked(evt);
            }
        });

        lblRegisterConfirmPassword.setForeground(new java.awt.Color(255, 255, 255));
        lblRegisterConfirmPassword.setText("Confirm Password:");

        javax.swing.GroupLayout pnlRegisterLayout = new javax.swing.GroupLayout(pnlRegister);
        pnlRegister.setLayout(pnlRegisterLayout);
        pnlRegisterLayout.setHorizontalGroup(
            pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRegisterLayout.createSequentialGroup()
                        .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblRegisterUsername)
                            .addComponent(lblRegisterPassword))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtRegisterUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(pswRegisterPassword)))
                    .addGroup(pnlRegisterLayout.createSequentialGroup()
                        .addComponent(lblRegisterConfirmPassword)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pswRegisterConfirmPassword)))
                .addGap(63, 63, 63))
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addGap(223, 223, 223)
                .addComponent(lblRegister)
                .addContainerGap(223, Short.MAX_VALUE))
        );
        pnlRegisterLayout.setVerticalGroup(
            pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRegisterLayout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegisterUsername)
                    .addComponent(txtRegisterUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegisterPassword)
                    .addComponent(pswRegisterPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegisterConfirmPassword)
                    .addComponent(pswRegisterConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(lblRegister)
                .addGap(38, 38, 38))
        );

        pnlCards.add(pnlRegister, "register");

        pnlOptions.setBackground(new java.awt.Color(0, 0, 0));

        lblOptionsIP.setBackground(new java.awt.Color(255, 255, 255));
        lblOptionsIP.setForeground(new java.awt.Color(255, 255, 255));
        lblOptionsIP.setText("Server Adress:");

        lblOptionsTCP.setBackground(new java.awt.Color(255, 255, 255));
        lblOptionsTCP.setForeground(new java.awt.Color(255, 255, 255));
        lblOptionsTCP.setText("TCP Port:");

        lblOptionsUDP.setBackground(new java.awt.Color(255, 255, 255));
        lblOptionsUDP.setForeground(new java.awt.Color(255, 255, 255));
        lblOptionsUDP.setText("UDP Port:");

        lblSave.setBackground(new java.awt.Color(255, 255, 255));
        lblSave.setForeground(new java.awt.Color(255, 255, 255));
        lblSave.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSave.setText("Save");
        lblSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSaveMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlOptionsLayout = new javax.swing.GroupLayout(pnlOptions);
        pnlOptions.setLayout(pnlOptionsLayout);
        pnlOptionsLayout.setHorizontalGroup(
            pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOptionsLayout.createSequentialGroup()
                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlOptionsLayout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlOptionsLayout.createSequentialGroup()
                                .addComponent(lblOptionsIP)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtOptionsIP, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlOptionsLayout.createSequentialGroup()
                                .addComponent(lblOptionsTCP)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtOptionsTCP))
                            .addGroup(pnlOptionsLayout.createSequentialGroup()
                                .addComponent(lblOptionsUDP)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtOptionsUDP))))
                    .addGroup(pnlOptionsLayout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addComponent(lblSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(103, Short.MAX_VALUE))
        );
        pnlOptionsLayout.setVerticalGroup(
            pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOptionsLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOptionsIP)
                    .addComponent(txtOptionsIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOptionsTCP)
                    .addComponent(txtOptionsTCP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOptionsUDP)
                    .addComponent(txtOptionsUDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(lblSave)
                .addGap(38, 38, 38))
        );

        pnlCards.add(pnlOptions, "options");

        pnlLogin.setBackground(new java.awt.Color(0, 0, 0));

        lblLoginUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblLoginUsername.setText("Username:");

        lblLoginPassword.setForeground(new java.awt.Color(255, 255, 255));
        lblLoginPassword.setText("Password:");

        rbSavePassword.setBackground(new java.awt.Color(0, 0, 0));
        rbSavePassword.setForeground(new java.awt.Color(255, 255, 255));
        rbSavePassword.setText("Save Password?");

        lblLoginEnter.setForeground(new java.awt.Color(255, 255, 255));
        lblLoginEnter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoginEnter.setText("Enter");
        lblLoginEnter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLoginEnterMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlLoginLayout = new javax.swing.GroupLayout(pnlLogin);
        pnlLogin.setLayout(pnlLoginLayout);
        pnlLoginLayout.setHorizontalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLoginLayout.createSequentialGroup()
                        .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblLoginUsername)
                            .addComponent(lblLoginPassword))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtLoginUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(pswLoginPassword)))
                    .addGroup(pnlLoginLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(rbSavePassword)))
                .addGap(63, 63, 63))
            .addGroup(pnlLoginLayout.createSequentialGroup()
                .addGap(231, 231, 231)
                .addComponent(lblLoginEnter)
                .addContainerGap(231, Short.MAX_VALUE))
        );
        pnlLoginLayout.setVerticalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginLayout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoginUsername)
                    .addComponent(txtLoginUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoginPassword)
                    .addComponent(pswLoginPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(rbSavePassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(lblLoginEnter)
                .addGap(38, 38, 38))
        );

        pnlCards.add(pnlLogin, "login");

        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        btnOptions.setText("Options");
        btnOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptionsActionPerformed(evt);
            }
        });

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlCards, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCards, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegister)
                    .addComponent(btnOptions)
                    .addComponent(btnExit)
                    .addComponent(btnLogin))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        if ("login".equals(currentCard)) {
            currentCard = "home";
        } else {
            currentCard = "login";
        }
        cards.show(pnlCards, currentCard);
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        if ("register".equals(currentCard)) {
            currentCard = "home";
        } else {
            currentCard = "register";
        }
        cards.show(pnlCards, currentCard);
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOptionsActionPerformed
        if ("options".equals(currentCard)) {
            currentCard = "home";
        } else {
            currentCard = "options";
        }
        cards.show(pnlCards, currentCard);
    }//GEN-LAST:event_btnOptionsActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        exit();
    }//GEN-LAST:event_btnExitActionPerformed

    private void lblLoginEnterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLoginEnterMouseClicked
        String username = txtLoginUsername.getText();
        String password = new String(pswLoginPassword.getPassword());
        boolean savePassword = rbSavePassword.isSelected();

        if (username.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password must be filled.");
            return;
        }

        Settings.setProperty("user.name", username);
        Settings.setProperty("user.password", savePassword ? password : "");
        Settings.setProperty("user.savepassword", Boolean.toString(savePassword));
        pswLoginPassword.setText("");
        Client client = Client.getClient();

        try {
            client.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Unable to connect to the server.");
            return;
        }
        client.getSocket().sendTCP(Client.createPacket(
                ClientNetworkPackets.LOGIN_REQUEST,
                new JSON("username", username),
                new JSON("password", password)));
    }//GEN-LAST:event_lblLoginEnterMouseClicked

    private void lblRegisterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegisterMouseClicked
        String username = txtRegisterUsername.getText();
        String password = new String(pswRegisterPassword.getPassword());
        String confirmPassword = new String(pswRegisterConfirmPassword.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match.");
            return;
        }

        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Username and passwords must be filled.");
            return;
        }

        pswRegisterPassword.setText("");
        pswRegisterConfirmPassword.setText("");
        Client client = Client.getClient();

        try {
            client.openConnection();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Unable to connect to the server.");
            return;
        }
        client.getSocket().sendTCP(Client.createPacket(
                ClientNetworkPackets.REGISTER_REQUEST,
                new JSON("username", username),
                new JSON("password", password)));
    }//GEN-LAST:event_lblRegisterMouseClicked

    private void lblSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSaveMouseClicked
        String ip = txtOptionsIP.getText();
        String tcp = txtOptionsTCP.getText();
        String udp = txtOptionsUDP.getText();

        Settings.setProperty("server.ip", ip);
        Settings.setProperty("server.tcpport", tcp);
        Settings.setProperty("server.udpport", udp);

        JOptionPane.showMessageDialog(this, "Saved successfully.");
    }//GEN-LAST:event_lblSaveMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exit();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnOptions;
    private javax.swing.JButton btnRegister;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    private javax.swing.JLabel lblLoginEnter;
    private javax.swing.JLabel lblLoginPassword;
    private javax.swing.JLabel lblLoginUsername;
    private javax.swing.JLabel lblOptionsIP;
    private javax.swing.JLabel lblOptionsTCP;
    private javax.swing.JLabel lblOptionsUDP;
    private javax.swing.JLabel lblRegister;
    private javax.swing.JLabel lblRegisterConfirmPassword;
    private javax.swing.JLabel lblRegisterPassword;
    private javax.swing.JLabel lblRegisterUsername;
    private javax.swing.JLabel lblSave;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel pnlCards;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlLogin;
    private javax.swing.JPanel pnlOptions;
    private javax.swing.JPanel pnlRegister;
    private javax.swing.JPasswordField pswLoginPassword;
    private javax.swing.JPasswordField pswRegisterConfirmPassword;
    private javax.swing.JPasswordField pswRegisterPassword;
    private javax.swing.JRadioButton rbSavePassword;
    private javax.swing.JTextField txtLoginUsername;
    private javax.swing.JTextField txtOptionsIP;
    private javax.swing.JTextField txtOptionsTCP;
    private javax.swing.JTextField txtOptionsUDP;
    private javax.swing.JTextField txtRegisterUsername;
    // End of variables declaration//GEN-END:variables
}
