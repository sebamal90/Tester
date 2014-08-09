/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.MessageResource;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SebaTab
 */
public class DataSaver extends Thread {
    private long startTime;
    private final Main main;   
    private ArrayList<HrData> hrData;
    private boolean work = true;
    private boolean pause = false;
    private final MessageResource messageResource;
    private long pauseStart;
    private long pauseTime = 0;

    public DataSaver(Main main) {
        this.main = main;
        hrData = new ArrayList<HrData>();
        messageResource = main.getMessageResource();
    }
    
    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        long lastTime = startTime;
        hrData.add(new HrData(0, main.getHeartBeat().getHR())); 
       // hrData.add(new HrData(0, 59)); 
       //          System.out.println("Saved time:" + hrData.get(hrData.size()-1).getTime()
       //                             + "hr: " + hrData.get(hrData.size()-1).getHr());
        
        while(work) {
            long now = System.currentTimeMillis();
            if (now-lastTime >= 2000) {
                hrData.add(new HrData(now-startTime-pauseTime, main.getHeartBeat().getHR())); 
        //        hrData.add(new HrData(now-startTime-pauseTime, 59)); 
        //        System.out.println("Saved time:" + hrData.get(hrData.size()-1).getTime()
        //                           + "hr: " + hrData.get(hrData.size()-1).getHr());
                lastTime = now;   
            }
             
            while(pause) {
               try {
                  Thread.sleep(500);
               } catch (InterruptedException ex) {
                   Logger.getLogger(DataSaver.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
           
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(DataSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public long getTime() {
        return System.currentTimeMillis()-startTime-pauseTime;
    }
    
    public ArrayList<HrData> getHrData() {
        return hrData; 
    }
    
    public void resumeDataSaver() {
        work = true;
        pause = false;
        pauseTime = pauseTime + (System.currentTimeMillis()-pauseStart);
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
    
    public void setWork(boolean work) {
        this.work = work;
    }
    
    public boolean getWork() {
        return work;
    }
    
    public void setPause(boolean work) {
        this.work = work;
    }
    
    public boolean getPause() {
        return work;
    }
    
    public class HrData {
        private int hr = -1;
        private long time = -1;

        public HrData(long time, int hr) {
            this.time = time;
            this.hr = hr;
        }

        public void setHr(int hr) {
            this.hr = hr;
        }

        public int getHr() {
            return hr;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public long getTime() {
            return time;
        }
    }
    
}
