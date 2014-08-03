/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.Message;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.PolarMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author SebaTab
 */
public class MessageReader implements Runnable{

    private boolean debug = false;
    private StreamConnection connection;
    private Message message;
    private boolean read = true;
    private PolarMessage polarMessageTmp;
    private String deviceType;
    private InputStream inputStream;
    
    public MessageReader(StreamConnection connection, String deviceType) {
        this.connection = connection;
        this.deviceType = deviceType;
    }
    
    private void read() {
        try {
            inputStream = connection.openInputStream();
            
            if (deviceType.startsWith("Polar")) {
                readPolar(inputStream);
            }
        } catch (IOException ex) {
            Logger.getLogger(BTDevice.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
                connection.close();
            } catch (IOException ex) {
                Logger.getLogger(BTDevice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void readPolar(InputStream inputStream) throws IOException {
        long i;
        while(read){
            i=System.currentTimeMillis();
            int msg = inputStream.read();
            if (msg == 254) {
                printMessage("\n" + System.currentTimeMillis() + ": ");
                polarMessageTmp = new PolarMessage(System.currentTimeMillis());
            }
            else if (polarMessageTmp != null) {
                if (polarMessageTmp.setNextValue(msg)) {
                    message = new PolarMessage(System.currentTimeMillis());
                    message.setHr(polarMessageTmp.getHr());
                }                   
            }
            if (i-System.currentTimeMillis() > 3000) {
                System.out.println("Zbyt długi czas oczekiwania");
            }
            printMessage(" "+msg);
        }
    }
    
    private void printMessage(String message) {
        if (debug) {
            System.out.print(message);
        }
    }

    @Override
    public void run() {
        read();
    }
    
    public void stopRead() {
        read = false;
        try {
            inputStream.close();
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(BTDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Message getLastMessage() {
        return message;
    }    
}
