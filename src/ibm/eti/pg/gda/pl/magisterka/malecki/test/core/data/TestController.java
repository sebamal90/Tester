/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.api.MessageResource;
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
    private final MessageResource messageResource;
    private Phase phase;

    public TestController(Main aMain) {
        main = aMain;
        messageResource = main.getMessageResource();
        timer = new Timer();
        dataSaver = new DataSaver();
    }

    @Override
    public void run() {
        timer.start();
        dataSaver.addData(0, messageResource.getHR(), 0);
        long lastTime = timer.getTime();
        phase = new Phase(1, 0, protocol.getWaitTime(), 0);

        while (work) {
            long now = (timer.getTime() / 100) * 100;
            if (now - lastTime >= 2000) {
                if (phase.getEndTime() <= now) {
                    nextPhase(now);
                }
                dataSaver.addData(now,
                     messageResource.getHR(), phase.getLoad());
                lastTime = now;
            }

            do {
               try {
                  Thread.sleep(200);
               } catch (InterruptedException ex) {
                   Logger.getLogger(DataSaver.class.getName()).
                           log(Level.SEVERE, null, ex);
               }
            } while(pause);
        }
    }

    private void nextPhase(long now) {
        if (phase.getPhaseId() >= 2) {
            phase = new Phase(phase.getPhaseId() + 1, now,
                    protocol.getStepTime(),
                    phase.getLoad() + protocol.getStepPower());
        } else if (phase.getPhaseId() > 0) {
            phase = new Phase(2, now, protocol.getWarmupTime(), protocol.getStartPower());
        }
    }

    public int getLoad() {
        return phase.getLoad();
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
        phase = new Phase(0, (timer.getTime() / 100) * 100,
                protocol.getRecovery(), 0);
    }

    public long getTime() {
        return timer.getTime();
    }

    public List<TestData> getTestDatas() {
        return dataSaver.getTestDatas();
    }

    public void endTest() {
        work = false;
        pause = false;
        timer.pause();
    }

}
