package com.tecnologiabasica.jettyapidatabase;

import com.tecnologiabasica.jettyapicommons.sql.JUserInfoSql;
import org.apache.log4j.Logger;

/**
 *
 * @author afonso
 */
public class JDatabaseConnector extends ADatabasePooling {

    private static JDatabaseConnector instance = null;
    
    //Todas as vezes que algo na estrutura do Banco de Dados for alterada, essa constante deve ser incrementada
    public static final int DATABASE_VERSION = 1;

    //Configurando a senha do PostgreSQL
    //sudo -u postgres psql -U postgres -c "ALTER USER postgres WITH PASSWORD 'master';"
    
    public static JDatabaseConnector getInstance() {
        if (instance == null) {
            instance = new JDatabaseConnector();
        }
        return instance;
    }

    private JDatabaseConnector() {

    }

    @Override
    protected String getUserName() {
        String returnValue = null;
        switch (databaseType) {
            case H2DB:
                returnValue = dataBaseName;
                break;
            case PostgreSQL:
                returnValue = "postgres";
                break;
            case Firebird:
                returnValue = "SYSDBA";
                break;
        }
        return returnValue;

    }

    @Override
    protected String getPassword() {
        String returnValue = null;
        switch (databaseType) {
            case H2DB:
                returnValue = "master";
                break;
            case PostgreSQL:
                returnValue = "master";
                break;
            case Firebird:
                returnValue = "masterkey";
                break;
        }
        return returnValue;
    }

    @Override
    protected void updateDatabase() {
        int version = getDBVersion();
        //Versão inicial para criação das tabelas
        if (version < 2) {
            executeSqlArray(JUserInfoSql.createTable(databaseType));
           
            JDefaultValues defaultValues = new JDefaultValues();
            defaultValues.insertV2();

            version = DATABASE_VERSION;
            setDBVersion(version);
            Logger.getLogger(JDatabaseConnector.class.getName()).info("Banco de dados criado - versão: " + version);
        }

       

    }

}
