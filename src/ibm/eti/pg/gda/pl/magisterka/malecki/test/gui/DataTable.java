/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.TestResource;
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.table.TableColumn;

/**
 *
 * @author SebaTab
 */
public class DataTable extends JPanel implements Runnable{
    //private final JScrollPane scrollPane;
    private final JFrame frame;
    private final JTable table;
    private final JPanel pan;
    private final DataTableModel model;
    
    public DataTable(TestResource testResource, JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Data Table:");
        
        model = new DataTableModel(testResource.getDatas());
        table = new JTable(model);
        
        TableColumn column = null;
        for (int i = 0; i < 2; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 1) {
                column.setPreferredWidth(100); //third column is bigger
            } else {
                column.setPreferredWidth(150);
            }
        }
        
        this.add(title, BorderLayout.PAGE_START);
        
        pan = new JPanel();
        pan.setLayout(new BorderLayout());
        
        pan.add(table.getTableHeader(), BorderLayout.PAGE_START);
        pan.add(table, BorderLayout.CENTER);
        this.add(pan, BorderLayout.CENTER);
        /*
        //scrollPane = new JScrollPane(table);
        table.setPreferredSize(new Dimension(200, frame.getHeight()-325));
        table.setFillsViewportHeight(true);
        add(table)*/;        
    }
    
/*
    @Override
    public void updateUI() {
        setUI((BasicPanelUI)UIManager.getUI(this));
        if (table != null) {
             table.revalidate();
        }
   }
*/
    @Override
    public void run() {
        while (true) {
            table.revalidate();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DataTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
