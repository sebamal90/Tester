/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.api;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data.DataSaver;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author SebaTab
 */
public class TestResource {
    private final Main main;
    private DataSaver dataSaver;
    private boolean testStatus = false;

    public TestResource(Main aMain) {
        this.main = aMain;
    }

    public void startTest() {
        dataSaver = new DataSaver(main);
        dataSaver.setName("Data Saver Thread");
        dataSaver.start();
        testStatus = true;
    }

    public void pauseTest() {
        dataSaver.pauseDataSaver();
        testStatus = false;
    }

    public void resumeTest() {
        dataSaver.resumeDataSaver();
        testStatus = true;
    }

    public void stopTest() {
        if (dataSaver != null) {
            dataSaver.stopDataSaver();
        }
        testStatus = false;
    }

    public boolean isTestStatus() {
        return testStatus;
    }

    public long getTime() {
        return dataSaver.getTime();
    }

    public String getTimer() {
        long time = dataSaver.getTime();
        int sec = (int) (TimeUnit.MILLISECONDS.toSeconds(time)
                - TimeUnit.MINUTES.toSeconds(
                  TimeUnit.MILLISECONDS.toMinutes(time)));
        String timer;
        if (sec < 10) {
            timer = TimeUnit.MILLISECONDS.toMinutes(time) + ":0" + sec;
        } else {
            timer = TimeUnit.MILLISECONDS.toMinutes(time) + ":" + sec;
        }

        return timer;
    }

    public List<DataSaver.HrData> getDatas()  {
        List<DataSaver.HrData> dataList;

        if (dataSaver == null) {
            dataList = new ArrayList<DataSaver.HrData>();
        } else {
            dataList = dataSaver.getHrData();
        }

        return dataList;
    }
}
