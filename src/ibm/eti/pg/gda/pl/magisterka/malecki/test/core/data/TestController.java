/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data.DataSaver.TestData;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SebaTab
 */
public class TestController extends Thread {
    private Protocol protocol = new Protocol();
    private Timer timer;
    private DataSaver dataSaver;
    private Main main;
    private boolean work = true;
    private boolean pause = false;

    public TestController(Main aMain) {
        main = aMain;
        timer = new Timer();
        dataSaver = new DataSaver();
    }

    @Override
    public void run() {
        Long startTime = System.currentTimeMillis();
        timer.start(startTime);
        dataSaver.addData(0, main.getMessageResource().getHR(), 0);
        long lastTime = timer.getTime();

        while (work) {
            long now = timer.getTime();
            if (now - lastTime >= 2000) {
                 dataSaver.addData(now,
                         main.getMessageResource().getHR(), 100);

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

    public void resumeTest() {
        work = true;
        pause = false;
        timer.resume();
    }

    public void pauseTest() {
        work = true;
        pause = true;
        timer.pause();
    }

    public void stopTest() {
        work = false;
        pause = false;
        timer.pause();
    }

    public long getTime() {
        return timer.getTime();
    }

    public List<TestData> getTestDatas() {
        return dataSaver.getTestDatas();
    }

}
