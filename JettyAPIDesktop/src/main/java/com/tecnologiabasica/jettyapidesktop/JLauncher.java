/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapidesktop;

import com.tecnologiabasica.jettyapicommons.JAppCommons;
import com.tecnologiabasica.jettyapicommons.util.JLoggerUtil;

/**
 *
 * @author afonso
 */
public class JLauncher {

    public static void main(String[] args) {        
        //Configura o arquivo de log que será gerado. Importante que seja feito o mais rápido possível.
        JLoggerUtil.getInstance().start(JAppCommons.getHomeDir(), "JettyAPIDesktop");
        JMainApplication.getInstance();
    }

}
