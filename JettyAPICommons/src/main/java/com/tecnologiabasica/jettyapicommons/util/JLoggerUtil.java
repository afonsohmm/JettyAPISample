/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapicommons.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author afonso
 */
public class JLoggerUtil {

    private static JLoggerUtil instance = null;
    private String homeDir = null;
    private String logFileName = null;

    public static JLoggerUtil getInstance() {
        if (instance == null) {
            instance = new JLoggerUtil();
        }
        return instance;
    }

    private JLoggerUtil() {

    }

    public void start(String path, String productName) {
        if (path == null) {
            path = "";
        }
        homeDir = path + File.separator + "logs";

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

        }

        logFileName = homeDir + File.separator + productName + ".log";
        setLogFile();
    }

    private void setLogFile() {

        //Load the Existing Properties
        Properties log4jprops = new Properties();

        //Modify the rootLogger to include a LOGFILE appender
        log4jprops.setProperty("log4j.rootLogger", "DEBUG, RollingAppender, stdout");

        //Set the output file for the LOGFILE appender
        log4jprops.setProperty("log4j.appender.RollingAppender", "org.apache.log4j.DailyRollingFileAppender");
        log4jprops.setProperty("log4j.appender.RollingAppender.File", logFileName);
        log4jprops.setProperty("log4j.appender.RollingAppender.DatePattern", "'.'yyyy-MM-dd'.log'");
        log4jprops.setProperty("log4j.appender.RollingAppender.layout", "org.apache.log4j.PatternLayout");
        log4jprops.setProperty("log4j.appender.RollingAppender.layout.ConversionPattern", "%d:%p:%c:%M:%m%n");

        log4jprops.setProperty("log4j.logger.org.apache.http", "ERROR");

        log4jprops.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        log4jprops.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        log4jprops.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%d:%p:%c:%M:\n%m%n");

        //Configure Log4j
        PropertyConfigurator.configure(log4jprops);
    }
}
