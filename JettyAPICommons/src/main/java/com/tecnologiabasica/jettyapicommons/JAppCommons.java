/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapicommons;

import com.tecnologiabasica.jettyapicommons.util.JFileUtil;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author afonso
 */
public class JAppCommons {

    public static final int VERSION_ID = 1;
    public static final String VERSION = "0.0.1." + VERSION_ID;
    public static final int GMT_SERVER = 3;
    public static final String PRODUCT_NAME = "JettyAPITemplate";
    public static final String COMPANY = "Powered by Tecnologia Básica";

    public static String URL_API = "http://localhost:8080/";
    public static String URL_UI = "https://localhost/";
    public static String URL_WS = "ws://localhost:8080/";

    public static final String API_TOKEN = "jetty";

    private JAppCommons() {

    }

    public static String getHomeDir() {
        String homeDir = System.getProperty("user.home") + File.separator + ".jettyapisample";
        File theDir = new File(homeDir);

        if (!theDir.exists()) {
            try {
                theDir.mkdir();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

        try {
            FileUtils.forceMkdir(new File(homeDir));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return homeDir;
    }

    public static String getServiceDir(String dir) {
        String serviceDir = getHomeDir() + File.separator + dir;

        File theDir = new File(serviceDir);

        if (!theDir.exists()) {
            try {
                theDir.mkdir();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

        try {
            FileUtils.forceMkdir(new File(serviceDir));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return serviceDir;
    }

    public static void configureServerIP(String homeDir) {
        String urlApi = null;
        String urlUi = null;
        System.out.println("HOME DIR: " + homeDir);
        try {
            Properties prop = JFileUtil.getPropertiesFile(homeDir + "/server.properties");
            if (prop != null) {
                urlApi = prop.getProperty("url.api");
                urlUi = prop.getProperty("url.ui");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JAppCommons.URL_API = urlApi != null ? urlApi : JAppCommons.URL_API;
        JAppCommons.URL_UI = urlUi != null ? urlUi : JAppCommons.URL_UI;
        System.out.println("url.ui: " + JAppCommons.URL_UI);
        System.out.println("url.api: " + JAppCommons.URL_API);
    }

    public static String getServerProperty(String homeDir, String property) {
        String returnValue = null;
        try {
            Properties prop = JFileUtil.getPropertiesFile(homeDir + "/server.properties");
            if (prop != null) {
                returnValue = prop.getProperty(property);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

}
