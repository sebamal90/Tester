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
    
    public boolean isValid() {
        if (time != -1 && hr != -1) {
            return true;
        }
        else {
            return false;
        }
    }
}
