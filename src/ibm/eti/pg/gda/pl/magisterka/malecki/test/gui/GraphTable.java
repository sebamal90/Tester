/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.DateCellRenderer;
import org.jfree.ui.NumberCellRenderer;

/**
 *
 * @author SebaTab
 */
public class GraphTable extends JPanel{
    private final GraphTableModel model;
    private static final int SERIES_COUNT = 1;

    public GraphTable(JFreeChart jfreechart) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(400, 75));
        setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
        model = new GraphTableModel(SERIES_COUNT);
        for (int i = 0; i < SERIES_COUNT; i++) {
            XYPlot xyplot = (XYPlot) jfreechart.getPlot();
            model.setValueAt(xyplot.getDataset(i).getSeriesKey(0), i, 0);
            model.setValueAt(new Double("0.00"), i, 1);
            model.setValueAt(new Double("0.00"), i, 2);
            model.setValueAt(new Double("0.00"), i, 3);
            model.setValueAt(new Double("0.00"), i, 4);
            model.setValueAt(new Double("0.00"), i, 5);
            model.setValueAt(new Double("0.00"), i, 6);
        }

        JTable jtable = new JTable(model);
        DateCellRenderer datecellrenderer =
                new DateCellRenderer(
                    new SimpleDateFormat("HH:mm:ss", Config.LOCALE));
        NumberCellRenderer numbercellrenderer = new NumberCellRenderer();
        jtable.getColumnModel().getColumn(1)
                .setCellRenderer(datecellrenderer);
        jtable.getColumnModel().getColumn(2)
                .setCellRenderer(numbercellrenderer);
        jtable.getColumnModel().getColumn(3)
                .setCellRenderer(numbercellrenderer);
        jtable.getColumnModel().getColumn(4)
                .setCellRenderer(numbercellrenderer);
        jtable.getColumnModel().getColumn(5)
                .setCellRenderer(datecellrenderer);
        jtable.getColumnModel().getColumn(6)
                .setCellRenderer(numbercellrenderer);

        add(new JScrollPane(jtable));
    }

    public GraphTableModel getModel() {
        return model;
    }

}
