/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.api;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data.DataSaver;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author SebaTab
 */
public class TestResource {
    private final Main main;
    private DataSaver dataSaver;
    private boolean testStatus = false;
    
    public TestResource(Main main) {
        this.main = main;
    }
    
    public void startTest() {
        dataSaver = new DataSaver(main);
        testStatus = true;
        dataSaver.start();
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
        dataSaver.stopDataSaver();
        testStatus = false;
    }
    
    public boolean getTestStatus() {
        return testStatus;
    }

    public String getTimer() {
        long time = dataSaver.getTime();
        int sec = (int)(TimeUnit.MILLISECONDS.toSeconds(time) - 
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
        String timer;
        if (sec<10) {
            timer = TimeUnit.MILLISECONDS.toMinutes(time) + ":0" + sec; 
        } else {
            timer = TimeUnit.MILLISECONDS.toMinutes(time) + ":" + sec; 
        }
        
        return timer;
    }
    
    public ArrayList<DataSaver.HrData> getDatas()  {
        if (dataSaver == null) {
            return new ArrayList<>(); 
        }
        return dataSaver.getHrData();
    }
    
    
}
