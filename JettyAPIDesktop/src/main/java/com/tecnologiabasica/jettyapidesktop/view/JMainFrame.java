/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapidesktop.view;

import com.tecnologiabasica.jettyapiapi.api.controller.JUserInfoApiController;
import com.tecnologiabasica.jettyapiapi.api.listener.IUserInfoDeleteListener;
import com.tecnologiabasica.jettyapiapi.api.listener.IUserInfoReadListener;
import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import java.util.LinkedList;

/**
 *
 * @author afonso
 */
public class JMainFrame extends javax.swing.JFrame {

    private JUserInfoTable table = null;

    /**
     * Creates new form JMainFrame
     */
    public JMainFrame() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);

        setTitle("Lista de Usuários");
        table = new JUserInfoTable();
        spContent.setViewportView(table);
        refreshTable();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnBotton = new javax.swing.JPanel();
        lbStatus = new javax.swing.JLabel();
        spContent = new javax.swing.JScrollPane();
        pnTop = new javax.swing.JPanel();
        btCreate = new javax.swing.JButton();
        btUpdate = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        btRead = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnBotton.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbStatus.setText(" ");

        javax.swing.GroupLayout pnBottonLayout = new javax.swing.GroupLayout(pnBotton);
        pnBotton.setLayout(pnBottonLayout);
        pnBottonLayout.setHorizontalGroup(
            pnBottonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBottonLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 762, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        pnBottonLayout.setVerticalGroup(
            pnBottonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBottonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbStatus)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        getContentPane().add(pnBotton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, 800, 40));
        getContentPane().add(spContent, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 800, 400));

        pnTop.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btCreate.setText("Novo");
        btCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCreateActionPerformed(evt);
            }
        });

        btUpdate.setText("Alterar");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        btDelete.setText("Excluir");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        btRead.setText("Refresh");
        btRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btReadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnTopLayout = new javax.swing.GroupLayout(pnTop);
        pnTop.setLayout(pnTopLayout);
        pnTopLayout.setHorizontalGroup(
            pnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btUpdate)
                .addGap(18, 18, 18)
                .addComponent(btDelete)
                .addGap(18, 18, 18)
                .addComponent(btRead)
                .addContainerGap(413, Short.MAX_VALUE))
        );
        pnTopLayout.setVerticalGroup(
            pnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btCreate)
                    .addComponent(btUpdate)
                    .addComponent(btDelete)
                    .addComponent(btRead))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        getContentPane().add(pnTop, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCreateActionPerformed
        JNewDialog newDialog = new JNewDialog(this, true, null);
        if (newDialog.getEntity() != null) {
            refreshTable();
        }
    }//GEN-LAST:event_btCreateActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        JUserInfoEntity entity = table.getJUserInfoEntitySelected();
        if (entity != null) {
            JNewDialog newDialog = new JNewDialog(this, true, entity);
            if (newDialog.getEntity() != null) {
                refreshTable();
            }
        }
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
         JUserInfoEntity entity = table.getJUserInfoEntitySelected();
        if (entity != null) {
            lbStatus.setText("");
            JUserInfoApiController apiController = new JUserInfoApiController();
            apiController.delete(entity.getEmail(), new UserInfoDeleteListener());
        }
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btReadActionPerformed
         refreshTable();
    }//GEN-LAST:event_btReadActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCreate;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btRead;
    private javax.swing.JButton btUpdate;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JPanel pnBotton;
    private javax.swing.JPanel pnTop;
    private javax.swing.JScrollPane spContent;
    // End of variables declaration//GEN-END:variables

    public void refreshTable() {
        lbStatus.setText("");
        JUserInfoApiController apiController = new JUserInfoApiController();
        apiController.read(null, null, new UserInfoReadListener());
    }

    private class UserInfoReadListener implements IUserInfoReadListener {

        @Override
        public void onSucess(LinkedList<JUserInfoEntity> collection) {
            table.reload(collection);
        }

        @Override
        public void onNotFound() {
            table.reload(new LinkedList<JUserInfoEntity>());
        }

        @Override
        public void onUnknow() {
            table.reload(new LinkedList<JUserInfoEntity>());
        }

        @Override
        public void onFailure(String message) {
            table.reload(new LinkedList<JUserInfoEntity>());
            lbStatus.setText("Falha ao comunicar-se com servidor: " + message);
        }

    }
    
    private class UserInfoDeleteListener implements IUserInfoDeleteListener {

        @Override
        public void onSucess(JUserInfoEntity entity) {
            refreshTable();
        }

        @Override
        public void onNotFound() {
            
        }

        @Override
        public void onError() {
            
        }

        @Override
        public void onUnknow() {
            
        }

        @Override
        public void onFailure(String message) {
            lbStatus.setText("Falha ao comunicar-se com servidor: " + message);
        }
        
    }

}
