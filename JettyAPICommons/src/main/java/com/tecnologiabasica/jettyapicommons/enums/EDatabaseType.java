/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapicommons.enums;

/**
 *
 * @author afonso
 */
public enum EDatabaseType {

    H2DB(0),
    PostgreSQL(1),
    SQLite(2),
    Firebird(3);

    private int f_value = -1;

    EDatabaseType(int value) {
        f_value = value;
    }

    public int value() {
        return f_value;
    }

    public static EDatabaseType getEnum(int value) {
        EDatabaseType en = PostgreSQL;

        for (EDatabaseType e : values()) {
            if (e.value() == value) {
                en = e;
                break;
            }
        }
        return en;
    }
}
