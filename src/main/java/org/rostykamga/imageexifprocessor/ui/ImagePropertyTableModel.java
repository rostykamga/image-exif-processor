package org.rostykamga.imageexifprocessor.ui;

import org.rostykamga.imageexifprocessor.model.ImageProperty;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ImagePropertyTableModel extends AbstractTableModel {

    static String[] COLUMNS = {"Name", "Value"};

    private List<ImageProperty> properties;

    @Override
    public int getRowCount() {
        return properties == null ? 0 : properties.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int col){
        return COLUMNS[col];
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if(properties == null)
            return null;

        ImageProperty prop = properties.get(rowIndex);

        return columnIndex == 0 ? prop.getName() : prop.getValue();
    }

    public void updateProperties(List<ImageProperty> properties){
        this.properties = properties;
        fireTableDataChanged();
    }
}
