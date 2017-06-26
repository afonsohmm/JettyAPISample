package com.tecnologiabasica.jettyapidesktop.view;

import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

public class JUserInfoTableModel extends AbstractTableModel {

    private LinkedList<JUserInfoEntity> users;

    private String[] colNames = {"Nome", "Senha", "Email", "Domain"};

    private Class<?>[] colTypes = {String.class, String.class, String.class, String.class};

    public JUserInfoTableModel() {
    }

    public void reload(LinkedList<JUserInfoEntity> users) {
        this.users = users;
        //atualiza o componente na tela
        fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return colTypes[column];
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }

    @Override
    public int getRowCount() {
        if (users == null) {
            return 0;
        }
        return users.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        JUserInfoEntity entity = users.get(row);
        switch (column) {
            case 0:
                return entity.getUserName();
            case 1:
                return entity.getUserPassword();
            case 2:
                return entity.getEmail();
            case 3:
                return entity.getDomainId();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public JUserInfoEntity getJUserInfoEntityAt(int index) {
        return users.get(index);
    }

}
