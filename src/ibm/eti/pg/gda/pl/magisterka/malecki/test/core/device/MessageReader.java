package ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author SebaTab
 */
public class MessageReader implements Runnable {

    private boolean debug = true;
    private StreamConnection connection;
    private Message message;
    private boolean isRead = true;
    private PolarMessage polarMessageTmp;
    private String deviceType;
    private InputStream inputStream;

    public MessageReader(StreamConnection aConnection, String aDeviceType) {
        this.connection = aConnection;
        this.deviceType = aDeviceType;
    }

    private void read() {
        try {
            inputStream = connection.openInputStream();

            if (deviceType.startsWith("Polar")) {
                readPolar();
            }
        } catch (IOException ex) {
            Logger.getLogger(BTDevice.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
                connection.close();
            } catch (IOException ex) {
                Logger.getLogger(BTDevice.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    private void readPolar() throws IOException {
        long i;
        while (isRead) {
            i = System.currentTimeMillis();
            int msg = inputStream.read();
            if (msg == 254) {
                printMessage("\n" + System.currentTimeMillis() + ": ");
                polarMessageTmp = new PolarMessage(System.currentTimeMillis());
            } else if (polarMessageTmp != null
                    && polarMessageTmp.setNextValue(msg)) {
                message = new PolarMessage(System.currentTimeMillis());
                message.setHr(polarMessageTmp.getHr());
            }
            if (i - System.currentTimeMillis() > 3000) {
                System.out.println("Zbyt długi czas oczekiwania");
            }
            printMessage(" " + msg);
        }
    }

    private void printMessage(String messageOut) {
        if (debug) {
            System.out.print(messageOut);
        }
    }

    @Override
    public void run() {
        read();
    }

    public void stopRead() {
        isRead = false;
        try {
            inputStream.close();
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(BTDevice.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    public Message getLastMessage() {
        return message;
    }
}
