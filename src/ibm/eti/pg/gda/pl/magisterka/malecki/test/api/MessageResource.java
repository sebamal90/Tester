/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.api;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.HeartRate;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.Message;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.MessageReader;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author SebaTab
 */
public class MessageResource {

    private MessageReader messageReader;
    private HeartRate heartRate;
    private boolean readingStatus = false;

    public void startRead(StreamConnection connection, String messageType) {
        messageReader = new MessageReader(connection, messageType);

        Thread thread = new Thread((Runnable) messageReader);
        thread.setName("Message Reader Thread");
        thread.start();

        heartRate = new HeartRate(messageReader);
        heartRate.setName("Heart Rate Thread");
        heartRate.start();
        readingStatus = true;
    }

    public void stopRead() {
        if (messageReader != null) {
            heartRate.stopRead();
            messageReader.stopRead();
        }
        readingStatus = false;
    }

    public Message getLastMessage() {
        return messageReader.getLastMessage();
    }

    public long getLastTime() {
        return messageReader.getLastMessage().getTime();
    }

    public int getHR() {
        return heartRate.getHR();
    }

    public boolean isHRTimeValid() {
        return heartRate.isValid();
    }

    public boolean isReading() {
        return readingStatus;
    }
}
