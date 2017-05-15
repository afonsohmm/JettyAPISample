/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapidatabase;

import com.tecnologiabasica.jettyapicommons.enums.EDatabaseType;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;

/**
 *
 * @author afonso
 */
public abstract class ADatabasePooling {

    protected abstract String getUserName();

    protected abstract String getPassword();

    protected abstract void updateDatabase();

    public EDatabaseType databaseType = EDatabaseType.H2DB;
    protected String dataBaseName = null;
    protected String homeDir = null;
    protected BasicDataSource ds = null;
    protected JdbcConnectionPool cp = null;

    public long open(String homeDir, String dataBaseName, EDatabaseType databaseType) {
        long result = -1;
        this.homeDir = homeDir;
        this.dataBaseName = dataBaseName;
        this.databaseType = databaseType;

        switch (databaseType) {
            case H2DB:
                cp = JdbcConnectionPool.create(getUrl(), getUserName(), getPassword());
                cp.setMaxConnections(1);
                break;
            case PostgreSQL:
                ds = new BasicDataSource();
                ds.setDriverClassName(getDriver());
                ds.setUsername(getUserName());
                ds.setPassword(getPassword());
                ds.setUrl(getUrl());

                ds.setMinIdle(5);
                ds.setMaxIdle(20);
                ds.setInitialSize(5);
                ds.setMaxOpenPreparedStatements(180);
                break;
            case Firebird:
                ds = new BasicDataSource();
                ds.setDriverClassName(getDriver());
                ds.setUsername(getUserName());
                ds.setPassword(getPassword());
                ds.setUrl(getUrl());

                ds.setMinIdle(5);
                ds.setMaxIdle(20);
                ds.setInitialSize(5);
                ds.setMaxOpenPreparedStatements(180);
                break;

        }

        try {
            Connection connection = getConnection();
            if (connection != null) {
                connection.close();
                updateDatabase();
                result = 1;
            } else {
                result = -1;
            }
        } catch (Exception ex) {
            Logger.getLogger(ADatabasePooling.class.getName()).error(ex.getMessage());
            //Em caso de corrupção do arquivo do H2DB, nesse projeto eu deleto para que um novo arquivo seja criado.
            //Os dados locais não são importantes, pois já estão no servidor
            if (ex.getMessage().contains("IO Exception: null") && databaseType == EDatabaseType.H2DB) {
                File file = new File(homeDir + File.separator + dataBaseName + ".mv.db");
                File fileDest = new File(homeDir + File.separator + dataBaseName + ".mv.db.corrupted");
                if (file.exists()) {
                    fileDest.delete();
                    file.renameTo(fileDest);
                }
                file = new File(homeDir + File.separator + dataBaseName + ".trace.db");
                fileDest = new File(homeDir + File.separator + dataBaseName + ".trace.db.corrupted");
                if (file.exists()) {
                    fileDest.delete();
                    file.renameTo(fileDest);
                }
                Logger.getLogger(ADatabasePooling.class.getName()).error("Banco de dados corrompido");
            }
            result = -1;
        }

        return result;
    }

    public long close() {
        long result = -1;
        try {
            switch (databaseType) {
                case H2DB:
                    cp.dispose();
                    break;
                case PostgreSQL:
                    ds.close();
                    break;
                case Firebird:
                    ds.close();
                    break;
            }
            result = 1;
        } catch (Exception ex) {
            Logger.getLogger(ADatabasePooling.class.getName()).error(ex.getMessage());
        }
        return result;
    }

    protected String getDriver() {
        String returnValue = null;
        switch (databaseType) {
            case H2DB:
                returnValue = "org.h2.Driver";
                break;
            case PostgreSQL:
                returnValue = "org.postgresql.Driver";
                break;
            case Firebird:
                returnValue = "org.firebirdsql.jdbc.FBDriver";
                break;
        }
        return returnValue;
    }

    protected String getUrl() {
        String returnValue = null;
        switch (databaseType) {
            case H2DB:
                returnValue = "jdbc:h2:" + homeDir + File.separator + dataBaseName;
                break;
            case PostgreSQL:
                returnValue = "jdbc:postgresql://127.0.0.1:5432/" + dataBaseName;
                break;
            case Firebird:
                returnValue = "jdbc:firebirdsql:" + dataBaseName + "?encoding=ISO8859_1";
                break;
        }
        return returnValue;
    }

    protected Connection getConnection() throws SQLException {
        Connection connection = null;
        switch (databaseType) {
            case H2DB:
                connection = cp.getConnection();
                break;
            case PostgreSQL:
                connection = ds.getConnection();
                break;
            case Firebird:
                connection = ds.getConnection();
                break;
        }
        return connection;
    }

    public long execute(String sql) {
        long result = -1;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            if (statement != null) {
                result = statement.executeUpdate(sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ADatabasePooling.class.getName()).error(ex.getMessage());
            result = -1;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ADatabasePooling.class.getName()).error(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ADatabasePooling.class.getName()).error(ex.getMessage());
                }
            }
        }

        return result;
    }
    
    public void executeSqlArray(String[] sqlArray) {
        if (sqlArray != null) {
            for (String sql : sqlArray) {
                execute(sql);
            }
        }
    }

    public long insert(String table, HashMap<String, Object> initialValues) {

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT");
        sql.append(" INTO ");
        sql.append(table);
        sql.append('(');

        String[] bindArgs = null;
        int size = (initialValues != null && initialValues.size() > 0)
                ? initialValues.size() : 0;

        bindArgs = new String[size];
        int i = 0;
        for (String colName : initialValues.keySet()) {
            sql.append((i > 0) ? "," : "");
            sql.append(colName);
            Object obj = initialValues.get(colName);
            if (obj != null) {
                if (obj.getClass().getName().equals("java.lang.String")) {
                    bindArgs[i++] = "'" + obj.toString().replace("'", "") + "'";
                } else {
                    bindArgs[i++] = obj.toString();
                }
            } else {
                bindArgs[i++] = null;
            }
        }
        sql.append(')');
        sql.append(" VALUES (");
        for (i = 0; i < size; i++) {
            sql.append((i > 0) ? "," + bindArgs[i] : bindArgs[i]);
        }

        sql.append(')');

        return execute(sql.toString());

    }

    public long update(String table, HashMap<String, Object> values,
            String whereClause, String[] whereArgs) {
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Empty values");
        }

        StringBuilder sql = new StringBuilder(120);
        sql.append("UPDATE ");
        sql.append(table);
        sql.append(" SET ");

        // move all bind args to one array
        int setValuesSize = values.size();
        int bindArgsSize = (whereArgs == null) ? setValuesSize : (setValuesSize + whereArgs.length);
        Object[] bindArgs = new Object[bindArgsSize];
        int i = 0;
        for (String colName : values.keySet()) {
            sql.append((i > 0) ? "," : "");
            sql.append(colName);
            Object obj = values.get(colName);
            if (obj != null) {
                if (obj.getClass().getName().equals("java.lang.String")) {
                    sql.append("='" + obj.toString() + "'");
                } else {
                    sql.append("=" + obj.toString());
                }
            } else {
                sql.append("= null");
            }
            i++;
        }
        if (whereArgs != null) {
            for (i = setValuesSize; i < bindArgsSize; i++) {
                bindArgs[i] = whereArgs[i - setValuesSize];
            }
        }
        if (!whereClause.isEmpty()) {
            sql.append(" WHERE ");
            sql.append(whereClause);
        }

        return execute(sql.toString());

    }

    public ResultSet query(String sql) {
        ResultSet result = null;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            result = statement.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(ADatabasePooling.class.getName()).error(ex.getMessage());
            result = null;

        } finally {
            if (result == null) {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(ADatabasePooling.class.getName()).error(ex.getMessage());
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(ADatabasePooling.class.getName()).error(ex.getMessage());
                    }
                }
            }
        }
        return result;
    }

    public void closeResult(ResultSet result) {
        Connection connection = null;
        Statement statement = null;
        if (result != null) {
            try {
                statement = result.getStatement();
                connection = statement.getConnection();
                result.close();
            } catch (SQLException ex) {
                Logger.getLogger(ADatabasePooling.class.getName()).error(ex.getMessage());
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ex) {
                Logger.getLogger(ADatabasePooling.class.getName()).error(ex.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(ADatabasePooling.class.getName()).error(ex.getMessage());
            }
        }
    }

    public int getDBVersion() {
        int result = -1;
        ResultSet rs = query("select * from version");
        if (rs != null) {
            try {
                rs.first();
                result = rs.getInt("value");
                closeResult(rs);
            } catch (SQLException ex) {
                Logger.getLogger(ADatabasePooling.class.getName()).info("Version control created");
            }
        } else {
            execute("create table version ( id INT PRIMARY KEY NOT NULL, value INT )");
            execute("insert into version (id,value) values (1,1)");
        }
        return result;
    }

    public void setDBVersion(int newVersion) {
        execute("update version set value = " + String.valueOf(newVersion));
    }

}
