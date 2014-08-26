/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data;

/**
 *
 * @author SebaTab
 */
public class Protocol {
    private int waitTime = 60;
    private int warmupTime = 300;
    private int stepTime = 180;
    private int startPower;
    private int stepPower = 40;
    private int rpm = 80;
    private int recovery = 300;

    public Protocol() {
        //not implemented yet
    }

    public void loadProtocol() {
        //not implemented yet
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getWarmupTime() {
        return warmupTime;
    }

    public int getStepTime() {
        return stepTime;
    }

    public int getStartPower() {
        return startPower;
    }

    public int getStepPower() {
        return stepPower;
    }

    public int getRpm() {
        return rpm;
    }

    public int getRecovery() {
        return recovery;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void setWarmupTime(int warmupTime) {
        this.warmupTime = warmupTime;
    }

    public void setStepTime(int stepTime) {
        this.stepTime = stepTime;
    }

    public void setStartPower(int startPower) {
        this.startPower = startPower;
    }

    public void setStepPower(int stepPower) {
        this.stepPower = stepPower;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public void setRecovery(int recovery) {
        this.recovery = recovery;
    }
}
