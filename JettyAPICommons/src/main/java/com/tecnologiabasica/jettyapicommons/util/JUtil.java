/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapicommons.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.DateTime;
import org.json.JSONObject;

/**
 *
 * @author afonso
 */
public class JUtil {

    public static boolean validateEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN
                = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static int getEmailHashCode(String value1, String value2) {
        int hash = 5;
        hash = 41 * hash + (value1 != null ? value1.hashCode() : 0);
        hash = 41 * hash + (value2 != null ? value2.hashCode() : 0);
        hash = (int) (41 * hash + (DateTime.now().getMillis()));
        if (hash < 0) {
            hash = hash * -1;
        }
        return hash;
    }

    public static String getMD5(String passwordToHash) {
        String generatedPassword = null;
        if (passwordToHash != null && !passwordToHash.equals("")) {
            try {
                // Create MessageDigest instance for MD5
                MessageDigest md = MessageDigest.getInstance("MD5");
                //Add password bytes to digest
                md.update(passwordToHash.getBytes());
                //Get the hash's bytes
                byte[] bytes = md.digest();
                //This bytes[] has bytes in decimal format;
                //Convert it to hexadecimal format
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bytes.length; i++) {
                    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                }
                //Get complete hashed password in hex format
                generatedPassword = sb.toString();
            } catch (NoSuchAlgorithmException ex) {
                
            }
        }
        return generatedPassword;
    }
    
    public static String buildJsonOutputMessage(String message) {
        return new JSONObject().put("msg", message).toString();
    }

}
