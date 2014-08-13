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

public class PolarMessage extends Message {
    private int len;
    private int chk;
    private int seq;
    private int status;
    private int messagePart = 0;

    public PolarMessage(long time) {
        super();
        setTime(time);
    }

    public boolean setNextValue(int value) {
        boolean isEndMessage = false;

        if (messagePart == 0) {
            setLen(value);
        } else if (messagePart == 1) {
            setChk(value);
        } else if (messagePart == 2) {
            setSeq(value);
        } else if (messagePart == 3) {
            setStatus(value >> 4);
        } else if (messagePart == 4) {
            if (!isValid()) {
                System.out.println("Nieprawidłowa wiadomość");
                isEndMessage = false;
            } else {
                setHr(value);
                isEndMessage = true;
            }
        }
        messagePart++;
        return isEndMessage;
    }

    public void setLen(int aLen) {
        this.len = aLen;
    }

    public void setChk(int aChk) {
        this.chk = aChk;
    }

    public void setSeq(int aSeq) {
        this.seq = aSeq;
    }

    public void setStatus(int aStatus) {
        this.status = aStatus;
    }

    @Override
    public boolean isValid() {
        return len + chk == 255;
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
