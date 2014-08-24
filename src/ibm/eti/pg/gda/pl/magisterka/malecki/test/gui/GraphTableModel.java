package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author SebaTab
 */
public class GraphTableModel extends AbstractTableModel
                                implements TableModel {
    private Object[][] data;


    public GraphTableModel(int rows) {
        super();
        data = new Object[rows][7];
    }

    public int getColumnCount() {
        return 7;
    }

    public int getRowCount() {
        return data.length;
    }

    public Object getValueAt(int row, int column) {
        return data[row][column];
    }

    public void setValueAt(Object obj, int row, int column) {
        data[row][column] = obj;
        fireTableDataChanged();
    }

    public String getColumnName(int column) {
        String columnName = null;

        switch (column) {
        case 0: // '\0'
            columnName = Config.labels.getString("GraphTable.series");
            break;
        case 1: // '\001'
            columnName = Config.labels.getString("GraphTable.time");
            break;
        case 2: // '\002'
            columnName = Config.labels.getString("GraphTable.value");
            break;
        case 3: // '\003'
            columnName = Config.labels.getString("GraphTable.avg");
            break;
        case 4: // '\004'
            columnName = Config.labels.getString("GraphTable.max");
            break;
        case 5: // '\005'
            columnName = "X (next):";
            break;
        case 6: // '\006'
            columnName = "Y (next):";
            break;
        default:
            break;
        }
        return columnName;
    }
}
