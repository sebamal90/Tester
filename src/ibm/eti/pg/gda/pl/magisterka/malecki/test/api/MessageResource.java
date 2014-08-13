/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.api;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.Message;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.MessageReader;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author SebaTab
 */
public class MessageResource {

    private MessageReader messageReader;

    public void startRead(StreamConnection connection, String messageType) {
        messageReader = new MessageReader(connection, messageType);

        Thread thread = new Thread((Runnable) messageReader);
        thread.setName("Message Reader Thread");
        thread.start();
    }

    public void stopRead() {
        if (messageReader != null) {
            messageReader.stopRead();
        }
    }

    public Message getLastMessage() {
        return messageReader.getLastMessage();
    }

    public int getLastHR() {
        return messageReader.getLastMessage().getHr();
    }

    public long getLastTime() {
        return messageReader.getLastMessage().getTime();
    }
}
