/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapitest;

import com.tecnologiabasica.jettyapicommons.JAppCommons;
import com.tecnologiabasica.jettyapicommons.util.JLoggerUtil;

/**
 *
 * @author afonso
 */
public class JLauncher {

    public static void main(String[] args) {
        /* Set the Nimbus look a    nd feel */
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
            java.util.logging.Logger.getLogger(JLauncher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JLauncher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JLauncher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JLauncher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        //Configura o arquivo de log que será gerado. Importante que seja feito o mais rápido possível.
        JLoggerUtil.getInstance().start(JAppCommons.getHomeDir(), "JettyAPITest");
        JMainApplication.getInstance();
    }

}
