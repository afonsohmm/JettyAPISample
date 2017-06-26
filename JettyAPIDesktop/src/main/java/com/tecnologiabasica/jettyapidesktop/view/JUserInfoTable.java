package com.tecnologiabasica.jettyapidesktop.view;

import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import java.util.LinkedList;
import javax.swing.JTable;

public class JUserInfoTable extends JTable {

    private JUserInfoTableModel model;

    public JUserInfoTable() {
        model = new JUserInfoTableModel();
        setModel(model);
    }

    public JUserInfoEntity getJUserInfoEntitySelected() {
        int i = getSelectedRow();
        if (i < 0) {
            return null;
        }
        return model.getJUserInfoEntityAt(i);
    }

    public void reload(LinkedList<JUserInfoEntity> users) {
        model.reload(users);
    }
}
