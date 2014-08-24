package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.TestResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data.DataSaver;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
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

/**
 *
 * @author SebaTab
 */
public final class Graph extends JPanel
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
        int chartSize = charts.size();
        JFreeChart[] ajfreechart = new JFreeChart[chartSize];
        for (int j = 0; j < chartSize; j++) {
            ajfreechart[j] = (JFreeChart) charts.get(j);
        }

        return ajfreechart;
    }

    private XYDataset createDataset(int nrSerii, String nazwaSerii,
                              RegularTimePeriod regulartimeperiod) {
        series[nrSerii] = new TimeSeries(nazwaSerii);
        RegularTimePeriod regulartime = regulartimeperiod;

        series[nrSerii].add(regulartime, 0);

        datasets[nrSerii] = new TimeSeriesCollection();
        datasets[nrSerii].addSeries(series[nrSerii]);
        return datasets[nrSerii];
    }

    @Override
    public void chartChanged(ChartChangeEvent chartchangeevent) {
        //not used
    }

    private JFreeChart createChart() {
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(
                    Config.labels.getString("Graph.title"),
                    Config.labels.getString("Graph.time"),
                    Config.labels.getString("Graph.hr"),
                    null, true, false, false);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        XYDataset[] axydataset = new XYDataset[SERIES_COUNT];
        Hour hour = new Hour(0, new Day());
        Minute minute = new Minute(0, hour);
        Second sec = new Second(1, minute);

        String[] seriesName = {
            Config.labels.getString("Graph.hr"),
            Config.labels.getString("Graph.power"),
            Config.labels.getString("Graph.lactate")};
        for (int i = 0; i < SERIES_COUNT; i++) {
            axydataset[i] = createDataset(i, seriesName[i], sec);
            if (i == 0) {
                xyplot.setDataset(axydataset[i]);
            } else {
                xyplot.setDataset(i, axydataset[i]);
                if (i < 3) {
                    xyplot.setRangeAxis(i, new NumberAxis(seriesName[i]));
                } else {
                    xyplot.setRangeAxis(i, new NumberAxis("Axis " + (i + 1)));
                }
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

        DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
        RelativeDateFormat relativedateformat =
                new RelativeDateFormat(sec.getFirstMillisecond());
        relativedateformat.setSecondFormatter(new DecimalFormat("00"));
        dateaxis.setDateFormatOverride(relativedateformat);
        dateaxis.setLowerMargin(0.01);
        dateaxis.setUpperMargin(0.01);
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

                for (int i = 0; i < SERIES_COUNT; i++) {
                    long lowerBound = series[i].getDataItem(0)
                            .getPeriod().getFirstMillisecond();
                    long upperBound = series[i].getDataItem(series[i].getItemCount() - 1)
                            .getPeriod().getFirstMillisecond();
                    if (l < lowerBound) {
                        l = lowerBound;
                        xyplot.setDomainCrosshairValue((double) l);
                    } else if (l > upperBound) {
                        l = upperBound;
                        xyplot.setDomainCrosshairValue((double) l);
                    }

                    model.setValueAt(Long.valueOf(l), i, 1);

                    int[] ai = datasets[i].getSurroundingItems(0, l);
                    long l1 = 0L;
                    long l2 = 0L;
                    double d1 = 0.0D;
                    double d2 = 0.0D;

                    TimeSeriesDataItem timeseriesdataitem;
                    if (ai[0] >= 0) {

                        timeseriesdataitem = series[i].getDataItem(ai[0]);
                    } else {
                        timeseriesdataitem = series[i].getDataItem(0);
                    }
                    l1 = timeseriesdataitem.getPeriod().getFirstMillisecond();
                    d1 = (double) timeseriesdataitem.getValue();

                    TimeSeriesDataItem timeseriesdataitem1;
                    if (ai[1] >= 0) {
                        timeseriesdataitem1 = series[i].getDataItem(ai[1]);
                    } else {
                        timeseriesdataitem1 = series[i].getDataItem(
                                series[i].getItemCount() - 1);
                    }
                    l2 = timeseriesdataitem1.getPeriod().getFirstMillisecond();
                    d2 = (double) timeseriesdataitem1.getValue();


                    double d3 = 0.0D;
                    if (l2 - l1 > 0L) {
                        d3 = d1 + (((double) l - (double) l1)
                                / ((double) l2 - (double) l1)) * (d2 - d1);
                    } else {
                        d3 = d1;
                    }
                    model.setValueAt((int) d3, i, 2);

                    setAvgMaxValue(i);
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

        GraphTable grpahTable = new GraphTable(jfreechart);
        model = grpahTable.getModel();
        jpanel.add(grpahTable, "South");
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

        while (work) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Graph.class.getName())
                        .log(Level.SEVERE, null, ex);
            }

            if (testResource.isTestStatus()) {
                List<DataSaver.HrData> dataList = testResource.getDatas();
                for (int i = 0; i < dataList.size(); i++) {
                    long time = dataList.get(i).getTime();
                    int sec = (int) (TimeUnit.MILLISECONDS.toSeconds(time)
                        - TimeUnit.MINUTES.toSeconds(
                          TimeUnit.MILLISECONDS.toMinutes(time)));

                    int min = (int) TimeUnit.MILLISECONDS.toMinutes(time);

                    Hour hour = new Hour(0, new Day());
                    Minute minute = new Minute(min, hour);
                    Second second = new Second(sec, minute);
                    RegularTimePeriod regulartime = second;

                    if (i == 0 && series[0].getDataItem(0).getPeriod()
                            .getFirstMillisecond()
                                != regulartime.getFirstMillisecond()) {
                        series[0].clear();
                    }
                    series[0].addOrUpdate(regulartime,
                                          dataList.get(i).getHr());
                }
            }
        }

    }

    private void setAvgMaxValue(int j) {
        TimeSeries serie = series[j];
        int max = 0;
        int avg = 0;
        int sum = 0;
        for (int i = 0; i < serie.getItemCount(); i++) {
            int tmp = serie.getDataItem(i).getValue().intValue();
            sum += tmp;
            if (tmp > max) {
                max = tmp;
            }
        }

        avg = (int) (sum / serie.getItemCount());

        model.setValueAt((int) avg, j, 3);
        model.setValueAt((int) max, j, 4);
    }
}
