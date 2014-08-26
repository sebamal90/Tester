/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SebaTab
 */
public class DataSaver extends Thread {

    private List<TestData> testDatas;


    public DataSaver() {
        super();
        testDatas = new ArrayList<TestData>();
    }

    public void addData(long time, int hr) {
        testDatas.add(new TestData(time, hr));
    }

    public void addData(long time, int hr, int load) {
         testDatas.add(new TestData(time, hr, load));
    }

    public List<TestData> getTestDatas() {
        return testDatas;
    }

    public static class TestData {
        private int heartRate = -1;
        private long time = -1;
        private int load;
        private float lactate;

        public TestData(long aTime, int aHr) {
            this.time = aTime;
            this.heartRate = aHr;
        }

        public TestData(long aTime, int aHr, int aLoad) {
            this.time = aTime;
            this.heartRate = aHr;
            this.load = aLoad;
        }

        public void setHeartRate(int aHr) {
            this.heartRate = aHr;
        }

        public int getHeartRate() {
            return heartRate;
        }

        public void setTime(long aTime) {
            this.time = aTime;
        }

        public long getTime() {
            return time;
        }

        public int getLoad() {
            return load;
        }

        public float getLactate() {
            return lactate;
        }

        public void setLoad(int load) {
            this.load = load;
        }

        public void setLactate(float lactate) {
            this.lactate = lactate;
        }
    }

}
