/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device;

/**
 *
 * @author SebaTab
 */
public class Message {

    private int heartRate = -1;
    private long time = -1;

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

    public boolean isValid() {
        return time != -1 && heartRate != -1;
    }
}
