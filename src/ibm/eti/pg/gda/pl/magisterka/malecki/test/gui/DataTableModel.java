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
public class DataTableModel extends AbstractTableModel{
    
    private List<HrData> datas ;
    private String[] columns ; 

    public DataTableModel(List<HrData> datas){
        super();
        this.datas = datas ;
        columns = new String[]{"Time", "Hr"};
    }

    @Override
    public int getRowCount() {
        return datas.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length ;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HrData data = datas.get(rowIndex);
        switch(columnIndex) {
            case 0: return convert(data.getTime());
            case 1: return data.getHr();
            default: return null;
        }
    }
    
    private String convert(long time) {
        int sec = (int)(TimeUnit.MILLISECONDS.toSeconds(time) - 
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
        if (sec<10) {
            return  TimeUnit.MILLISECONDS.toMinutes(time) + ":0" + sec; 
        } else {
            return  TimeUnit.MILLISECONDS.toMinutes(time) + ":" + sec; 
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) { 
        switch(columnIndex) {
            case 0: return false;
            default: return true;
        }
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex] ;
    }
    
}
