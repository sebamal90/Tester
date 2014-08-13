/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data.DataSaver.HrData;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author SebaTab
 */
public class DataTableModel extends AbstractTableModel {

    private List<HrData> datas;
    private String[] columns;

    public DataTableModel(List<HrData> aDatas) {
        super();
        this.datas = aDatas;
        columns = new String[]{"Time", "Hr"};
    }

    public void setDataTableModel(List<HrData> aDatas) {
        this.datas = aDatas;
    }

    @Override
    public int getRowCount() {
        return datas.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HrData data = datas.get(rowIndex);
        Object value = null;
        switch(columnIndex) {
            case 0:
                value = convert(data.getTime());
                break;
            case 1:
                value = data.getHr();
                break;
            default:
                break;
        }

        return value;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        HrData data = datas.get(rowIndex);
        String oldValue = "";
        String newValue = "";
        switch(columnIndex) {
            case 0:
                oldValue = String.valueOf(data.getTime());
                data.setTime((long) value);
                newValue = String.valueOf(data.getTime());
                break;
            case 1:
                oldValue = String.valueOf(data.getHr());
                data.setHr(Integer.parseInt((String) value));
                newValue = String.valueOf(data.getHr());
                break;
            default:
                break;
        }
        if (!oldValue.equals(newValue)) {
            System.out.println("In time " + convert(data.getTime()) + " change "
                    + getColumnName(columnIndex) + " from "
                    + oldValue + " to " + newValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    private String convert(long time) {
        int sec = (int) (TimeUnit.MILLISECONDS.toSeconds(time)
                - TimeUnit.MINUTES.toSeconds(
                  TimeUnit.MILLISECONDS.toMinutes(time)));
        String timeString;
        if (sec < 10) {
            timeString = TimeUnit.MILLISECONDS.toMinutes(time) + ":0" + sec;
        } else {
            timeString = TimeUnit.MILLISECONDS.toMinutes(time) + ":" + sec;
        }

        return timeString;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean isEditable = true;
        switch(columnIndex) {
            case 0:
                isEditable = false;
                break;
            case 1:
                isEditable = true;
                break;
            default:
                break;
        }

        return isEditable;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

}
