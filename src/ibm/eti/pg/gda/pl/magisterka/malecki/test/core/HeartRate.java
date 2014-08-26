package ibm.eti.pg.gda.pl.magisterka.malecki.test.core;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.Message;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.MessageReader;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeartRate extends Thread {
    private int heartRate = 0;
    private Message lastMessage;
    private boolean read = true;
    private boolean lastMessageTimer = false;
    private MessageReader messageReader;

    public HeartRate(MessageReader aMessageReader) {
        messageReader = aMessageReader;
    }

    @Override
    public void run() {
        while (read && lastMessage == null) {
            lastMessage = messageReader.getLastMessage();
        }

        while (read) {
            Message message = messageReader.getLastMessage();

            if (message != null) {
                if (message.getTime() == lastMessage.getTime()
                    && System.currentTimeMillis() - lastMessage.getTime() > 10000) {
                    lastMessageTimer = false;
                } else if (message.getTime() == lastMessage.getTime()
                    && System.currentTimeMillis() - lastMessage.getTime() > 3000) {
                    lastMessageTimer = false;
                } else {
                    lastMessage = message;
                    heartRate = lastMessage.getHr();
                    lastMessageTimer = true;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void stopRead() {
        read = false;
        heartRate = 0;
    }

    public int getHR() {
        return heartRate;
    }

    public boolean isValid() {
        return lastMessageTimer;
    }


}