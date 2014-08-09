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

    private int hr = -1;
    private long time = -1;

    public void setHr(int aHr) {
        this.hr = aHr;
    }

    public int getHr() {
        return hr;
    }

    public void setTime(long aTime) {
        this.time = aTime;
    }

    public long getTime() {
        return time;
    }

    public boolean isValid() {
        return time != -1 && hr != -1;
    }
}
