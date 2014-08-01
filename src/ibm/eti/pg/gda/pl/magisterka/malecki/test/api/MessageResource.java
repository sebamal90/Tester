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
    private final Main main;

    public MessageResource(Main main) {
        this.main = main;
    }
    
    public void startRead(StreamConnection connection, String messageType) {
        messageReader = new MessageReader(connection, messageType);
        
        Thread t = new Thread((Runnable)messageReader);
        t.start();
    }
    
    public void stopRead(StreamConnection connection, String messageType) {
        messageReader.stopRead();
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
