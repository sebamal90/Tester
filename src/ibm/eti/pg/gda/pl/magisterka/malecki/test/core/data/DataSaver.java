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
    private long startTime;
    private final Main main;
    private List<HrData> hrData;
    private boolean work = true;
    private boolean pause = false;
    private long pauseStart;
    private long pauseTime = 0;

    public DataSaver(Main aMain) {
        super();
        this.main = aMain;
        hrData = new ArrayList<HrData>();
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        long lastTime = startTime;
        hrData.add(new HrData(0, main.getHeartBeat().getHR()));
       // hrData.add(new HrData(0, 59));
       // System.out.println("Saved time:"
            //+ hrData.get(hrData.size()-1).getTime()
       //   + "hr: " + hrData.get(hrData.size()-1).getHr());

        while (work) {
            long now = System.currentTimeMillis();
            if (now - lastTime >= 2000) {
                hrData.add(new HrData(now - startTime - pauseTime,
                        main.getHeartBeat().getHR()));
                //hrData.add(new HrData(now-startTime-pauseTime, 59));
                //System.out.println("Saved time:"
                            //+ hrData.get(hrData.size()-1).getTime()
                            //+ "hr: " + hrData.get(hrData.size()-1).getHr());
                lastTime = now;
            }

            while (pause) {
               try {
                  Thread.sleep(500);
               } catch (InterruptedException ex) {
                   Logger.getLogger(DataSaver.class.getName()).
                           log(Level.SEVERE, null, ex);
               }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(DataSaver.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
    }

    public long getTime() {
        long time = 0;
        if (startTime != 0) {
            time = System.currentTimeMillis() - startTime - pauseTime;
        }
        return time;
    }

    public List<HrData> getHrData() {
        return hrData;
    }

    public void resumeDataSaver() {
        work = true;
        pause = false;
        pauseTime = pauseTime + (System.currentTimeMillis() - pauseStart);
    }

    public void pauseDataSaver() {
        work = true;
        pause = true;
        pauseStart = System.currentTimeMillis();
    }

    public void stopDataSaver() {
        work = false;
        pause = false;
    }

    public void setWork(boolean aWork) {
        this.work = aWork;
    }

    public boolean isWork() {
        return work;
    }

    public void setPause(boolean aWork) {
        this.work = aWork;
    }

    public boolean isPause() {
        return work;
    }

    public static class HrData {
        private int heartRate = -1;
        private long time = -1;

        public HrData(long aTime, int aHr) {
            this.time = aTime;
            this.heartRate = aHr;
        }

        public void setHr(int aHr) {
            this.heartRate = aHr;
        }

        public int getHr() {
            return heartRate;
        }

        public void setTime(long aTime) {
            this.time = aTime;
        }

        public long getTime() {
            return time;
        }
    }

}
