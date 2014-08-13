/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.TestResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.util.RelativeDateFormat;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.DateCellRenderer;
import org.jfree.ui.NumberCellRenderer;

/**
 *
 * @author SebaTab
 */
public class Graph extends JPanel
    implements ChartChangeListener, ChartProgressListener, Runnable {

    private static final int SERIES_COUNT = 1;
    private TimeSeriesCollection[] datasets;
    private TimeSeries[] series;
    private ChartPanel chartPanel;
    private GraphTableModel model;
    private List charts;
    private final Main main;
    private boolean work = true;

    public void addChart(JFreeChart jfreechart) {
        charts.add(jfreechart);
    }

    public JFreeChart[] getCharts() {
        int i = charts.size();
        JFreeChart[] ajfreechart = new JFreeChart[i];
        for (int j = 0; j < i; j++) {
            ajfreechart[j] = (JFreeChart) charts.get(j);
        }

        return ajfreechart;
    }

    private XYDataset createDataset(int nrSerii, String nazwaSerii, double d,
                              RegularTimePeriod regulartimeperiod, int j) {
        series[nrSerii] = new TimeSeries(nazwaSerii);
        RegularTimePeriod regulartimeperiod1 = regulartimeperiod;
        double d1 = d;

        series[nrSerii].add(regulartimeperiod1, 0);
        for (int k = 0; k < j; k++) {
            series[nrSerii].add(regulartimeperiod1, d1);
            regulartimeperiod1 = regulartimeperiod1.next();
            d1 *= 1.0D + (Math.random() - 0.495D) / 10D;
        }

        datasets[nrSerii] = new TimeSeriesCollection();
        datasets[nrSerii].addSeries(series[nrSerii]);
        return datasets[nrSerii];
    }

    @Override
    public void chartChanged(ChartChangeEvent chartchangeevent) {
        if (chartPanel != null) {
            JFreeChart jfreechart = chartPanel.getChart();
            if (jfreechart != null) {
                XYPlot xyplot = (XYPlot) jfreechart.getPlot();
                XYDataset xydataset = xyplot.getDataset();
                Comparable comparable = xydataset.getSeriesKey(0);
                double d = xyplot.getDomainCrosshairValue();
                model.setValueAt(comparable, 0, 0);
                long l = (long) d;
                for (int i = 0; i < SERIES_COUNT; i++) {
                    model.setValueAt(Long.valueOf(l), i, 1);
                    int[] ai = datasets[i].getSurroundingItems(0, l);
                    long l1 = 0L;
                    long l2 = 0L;
                    double d1 = 0.0D;
                    double d2 = 0.0D;
                    if (ai[0] >= 0) {
                        TimeSeriesDataItem timeseriesdataitem =
                                series[i].getDataItem(ai[0]);
                        l1 = timeseriesdataitem.getPeriod()
                                .getMiddleMillisecond();
                        Number number = timeseriesdataitem.getValue();
                        if (number == null) {
                            model.setValueAt(null, i, 4);
                        } else {
                            d1 = number.doubleValue();
                            model.setValueAt(new Double(d1), i, 4);
                        }
                        model.setValueAt(Long.valueOf(l1), i, 3);
                    } else {
                        model.setValueAt(new Double(0.0D), i, 4);
                        model.setValueAt(new Double(xyplot.getDomainAxis()
                                .getRange().getLowerBound()), i, 3);
                    }
                    if (ai[1] >= 0) {
                        TimeSeriesDataItem timeseriesdataitem1 =
                                series[i].getDataItem(ai[1]);
                        l2 = timeseriesdataitem1.getPeriod()
                                .getMiddleMillisecond();
                        Number number1 = timeseriesdataitem1.getValue();
                        if (number1 == null) {
                            model.setValueAt(null, i, 6);
                        } else {
                            d2 = number1.doubleValue();
                            model.setValueAt(new Double(d2), i, 6);
                        }
                        model.setValueAt(Long.valueOf(l2), i, 5);
                    } else {
                        model.setValueAt(new Double(0.0D), i, 6);
                        model.setValueAt(new Double(xyplot.getDomainAxis()
                                .getRange().getUpperBound()), i, 5);
                    }
                    double d3 = 0.0D;
                    if (l2 - l1 > 0L) {
                        d3 = d1 + (((double) l - (double) l1)
                                / ((double) l2 - (double) l1)) * (d2 - d1);
                    } else {
                        d3 = d1;
                    }
                    model.setValueAt(new Double(d3), i, 2);
                }
            }
        }
    }

    private JFreeChart createChart() {
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(
                    "Progressive test", "Time", "Hr",
                    null, true, false, false);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        XYDataset[] axydataset = new XYDataset[SERIES_COUNT];
        Hour hour = new Hour(0, new Day());
        Minute minute = new Minute(0, hour);
        Second sec = new Second(0, minute);

        String[] seriesName = {"Hr", "Power", "Lactate"};
        for (int i = 0; i < SERIES_COUNT; i++) {
            axydataset[i] = createDataset(i, seriesName[i], 100D
                    + (double) i * 200D, sec, 0);
            if (i == 0) {
                xyplot.setDataset(axydataset[i]);
            } else {
                xyplot.setDataset(i, axydataset[i]);
                if (i < 3) xyplot.setRangeAxis(i, new NumberAxis(seriesName[i]));
                else xyplot.setRangeAxis(i, new NumberAxis("Axis " + (i + 1)));
                xyplot.mapDatasetToRangeAxis(i, i);
                xyplot.setRenderer(i,
                                  new XYLineAndShapeRenderer(true, false));
            }
        }

        jfreechart.addChangeListener(this);
        jfreechart.addProgressListener(this);
        xyplot.setOrientation(PlotOrientation.VERTICAL);
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setDomainCrosshairLockedOnData(false);
        xyplot.setRangeCrosshairVisible(false);

        DateAxis dateaxis = (DateAxis)xyplot.getDomainAxis();
        RelativeDateFormat relativedateformat = new RelativeDateFormat(sec.getFirstMillisecond());
        relativedateformat.setSecondFormatter(new DecimalFormat("00"));
        dateaxis.setDateFormatOverride(relativedateformat);
        ChartUtilities.applyCurrentTheme(jfreechart);

        return jfreechart;
    }

    @Override
    public void chartProgress(ChartProgressEvent chartprogressevent) {
        if (chartprogressevent.getType() != 2) {
            return;
        }
        if (chartPanel != null) {
            JFreeChart jfreechart = chartPanel.getChart();
            if (jfreechart != null) {
                XYPlot xyplot = (XYPlot) jfreechart.getPlot();
                XYDataset xydataset = xyplot.getDataset();
                Comparable comparable = xydataset.getSeriesKey(0);
                double d = xyplot.getDomainCrosshairValue();
                model.setValueAt(comparable, 0, 0);
                long l = (long) d;
                model.setValueAt(Long.valueOf(l), 0, 1);
                for (int i = 0; i < SERIES_COUNT; i++) {
                    int j = series[i].getIndex(new Second(new Date(l)));
                    if (j >= 0) {
                        TimeSeriesDataItem timeseriesdataitem =
                                series[i].getDataItem(
                                Math.min(199, Math.max(0, j)));
                        TimeSeriesDataItem timeseriesdataitem1 =
                                series[i].getDataItem(Math.max(0, j - 1));
                        TimeSeriesDataItem timeseriesdataitem2 =
                                series[i].getDataItem(Math.min(199, j + 1));
                        long l1 = timeseriesdataitem.getPeriod()
                                .getMiddleMillisecond();
                        double d1 = timeseriesdataitem.getValue()
                                .doubleValue();
                        long l2 = timeseriesdataitem1.getPeriod()
                                .getMiddleMillisecond();
                        double d2 = timeseriesdataitem1.getValue()
                                .doubleValue();
                        long l3 = timeseriesdataitem2.getPeriod()
                                .getMiddleMillisecond();
                        double d3 = timeseriesdataitem2.getValue()
                                .doubleValue();
                        model.setValueAt(Long.valueOf(l1), i, 1);
                        model.setValueAt(new Double(d1), i, 2);
                        model.setValueAt(Long.valueOf(l2), i, 3);
                        model.setValueAt(new Double(d2), i, 4);
                        model.setValueAt(Long.valueOf(l3), i, 5);
                        model.setValueAt(new Double(d3), i, 6);
                    }
                }
            }
        }
    }

    public Graph(Main aMain) {
        super(new BorderLayout());
        main = aMain;
        charts = new ArrayList();
        datasets = new TimeSeriesCollection[SERIES_COUNT];
        series = new TimeSeries[SERIES_COUNT];
        setPreferredSize(new Dimension(800, 400));
        JPanel jpanel = new JPanel(new BorderLayout());
        setBackground(Color.red);
        jpanel.setBackground(Color.red);
        JFreeChart jfreechart = createChart();
        addChart(jfreechart);
        chartPanel = new ChartPanel(jfreechart);
        chartPanel.setPreferredSize(new Dimension(600, 270));
        chartPanel.setDomainZoomable(true);
        chartPanel.setRangeZoomable(true);
        javax.swing.border.CompoundBorder compoundborder =
                BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 4, 4, 4),
                BorderFactory.createEtchedBorder());
        chartPanel.setBorder(compoundborder);
        jpanel.add(chartPanel);
        JPanel jpanel1 = new JPanel(new BorderLayout());
        jpanel1.setPreferredSize(new Dimension(400, 75));
        jpanel1.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
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
                .setCellRenderer(datecellrenderer);
        jtable.getColumnModel().getColumn(4)
                .setCellRenderer(numbercellrenderer);
        jtable.getColumnModel().getColumn(5)
                .setCellRenderer(datecellrenderer);
        jtable.getColumnModel().getColumn(6)
                .setCellRenderer(numbercellrenderer);
        jpanel1.add(new JScrollPane(jtable));
        jpanel.add(jpanel1, "South");
        add(jpanel);
    }

    public void stop() {
        work = false;
    }

    public void startGraph() {
        work = true;
    }

    @Override
    public void run() {

        TestResource testResource = main.getTestResource();
        double d1 = 100.90000000000000002D + 0.20000000000000001D * Math.random();
        String lastTime="0:0";

        while(work) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (testResource.isTestStatus()) {
                long time = testResource.getTime();
                int sec = (int) (TimeUnit.MILLISECONDS.toSeconds(time)
                        - TimeUnit.MINUTES.toSeconds(
                          TimeUnit.MILLISECONDS.toMinutes(time)));

                int min = (int)TimeUnit.MILLISECONDS.toMinutes(time);

                if (!lastTime.equals(min + ":" + sec)) {
                    lastTime = min + ":" + sec;

                    Hour hour = new Hour(0, new Day());
                    Minute minute = new Minute(min, hour);
                    Second second = new Second(sec, minute);
                    RegularTimePeriod regulartimeperiod1 = second;

                    series[0].add(regulartimeperiod1, main.getHeartBeat().getHR());

                        for (int k = 1; k < SERIES_COUNT; k++) {
                            series[k].add(regulartimeperiod1, d1);

                            d1 *= 1.0D + (Math.random() - 0.495D) / 10D;
                        }
                }
            }
        }

    }
}

