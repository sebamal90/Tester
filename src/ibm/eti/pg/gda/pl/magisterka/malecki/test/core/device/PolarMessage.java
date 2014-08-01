/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device;

/**
 * An implementation of a Sensor MessageParser for Polar Wearlink Bluetooth HRM.
 *
 *  Polar Bluetooth Wearlink packet example;
 *   Hdr Len Chk Seq Status HeartRate RRInterval_16-bits
 *    FE  08  F7  06   F1      48          03 64
 *   where;
 *      Hdr always = 254 (0xFE),
 *      Chk = 255 - Len
 *      Seq range 0 to 15
 *      Status = Upper nibble may be battery voltage
 *               bit 0 is Beat Detection flag.
 *              
 *  Additional packet examples;10100010
 *    FE 08 F7 06 F1 48 03 64          
 *    FE 0A F5 06 F1 48 03 64 03 70
 *    
 * @author John R. Gerthoffer
 */

public class PolarMessage extends Message{
    private int len;
    private int chk;
    private int seq;
    private int status;
    private int i=0;

    public PolarMessage(long time) {
        setTime(time);
    }
         
    public boolean setNextValue(int value) {
        if (i==0) setLen(value);
        else if (i==1) setChk(value);
        else if (i==2) setSeq(value);
        else if (i==3) setStatus(value >> 4);
        else if (i==4) {
            i++;
            setHr(value);      
            if (!isValid()) {
                System.out.println("Nieprawidłowa wiadomość");
                return false;
            }
            return true;
        }
        i++;
        return false;
    }
    
    public void setLen(int len) {
        this.len = len;
    }

    public void setChk(int chk) {
        this.chk = chk;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    @Override
    public boolean isValid() {
        if (len+chk == 255)
            return true;
        else
            return false;
        
    }

    public int getLen() {
        return len;
    }

    public int getChk() {
        return chk;
    }

    public int getSeq() {
        return seq;
    }

    public int getStatus() {
        return status;
    }    
}
