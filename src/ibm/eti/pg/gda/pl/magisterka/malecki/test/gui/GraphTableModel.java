/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author SebaTab
 */
public class GraphTableModel extends AbstractTableModel
                                implements TableModel {
    private Object[][] data;


    public GraphTableModel(int i) {
        super();
        data = new Object[i][7];
    }

    public int getColumnCount() {
        return 7;
    }

    public int getRowCount() {
        return data.length;
    }

    public Object getValueAt(int i, int j) {
        return data[i][j];
    }

    public void setValueAt(Object obj, int i, int j) {
        data[i][j] = obj;
        fireTableDataChanged();
    }

    public String getColumnName(int i) {
        switch (i) {
        case 0: // '\0'
            return "Series Name:";
        case 1: // '\001'
            return "X:";
        case 2: // '\002'
            return "Y:";
        case 3: // '\003'
            return "X (prev)";
        case 4: // '\004'
            return "Y (prev):";
        case 5: // '\005'
            return "X (next):";
        case 6: // '\006'
            return "Y (next):";
        default:
            break;
        }
        return null;
    }
}
