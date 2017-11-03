/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiclient.api.controller;

/**
 *
 * @author afonso
 */
public class JApiResponseString {

    public static String getApiRespnse(int responseCode) {
        String returnValue = null;
        switch (responseCode) {
            //OK
            case 200:
                returnValue = "Solicitação aceita.";
                break;
            //BAD REQUEST
            case 400:
                returnValue = "Operação impossível.";
                break;
            //NOT ACCEPTABLE
            case 406:
                returnValue = "Informações incompletas. Operação não foi concluída.";
                break;
            //CONFLICT
            case 409:
                returnValue = "Informações conflitantes. Operação não foi concluída.";
                break;
            default:
                returnValue = "Código desconhecido: " + responseCode;
                break;
        }
        return returnValue;
    }

}
