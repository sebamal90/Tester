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

    public MessageResource(Main aMain) {
        Main main = aMain;
    }

    public void startRead(StreamConnection connection, String messageType) {
        messageReader = new MessageReader(connection, messageType);

        Thread t = new Thread((Runnable) messageReader);
        t.setName("Message Reader Thread");
        t.start();
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
