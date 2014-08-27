package ibm.eti.pg.gda.pl.magisterka.malecki.test.api;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data.DataSaver;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data.DataSaver.TestData;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data.TestController;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestResource {
    private final Main main;
    private TestController testController;
    private boolean testStatus = false;

    public TestResource(Main aMain) {
        this.main = aMain;
        testController = new TestController(main);
        testController.setName("TestController Thread");
    }

    public void startTest() {
        testController.start();
        testStatus = true;
    }

    public void pauseTest() {
        testController.pauseTest();
        testStatus = false;
    }

    public void resumeTest() {
        testController.resumeTest();
        testStatus = true;
    }

    public void endTest() {
        testController.endTest();
        testStatus = false;
    }

    public void stopTest() {
        testController.stopTest();
    }

    public boolean isTestStatus() {
        return testStatus;
    }

    public long getTime() {
        return testController.getTime();
    }

    public String getTimer() {
        long time = testController.getTime();
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

    public List<TestData> getDatas()  {
        return testController.getTestDatas();
    }

    public int getLoad() {
        return testController.getLoad();
    }
}
