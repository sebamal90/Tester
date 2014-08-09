/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.api;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.BTDevice;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author SebaTab
 */
public class BTDeviceResource {

    private BTDevice btDevice = new BTDevice();
    private final Main main;
    
    public BTDeviceResource(Main main) {
        this.main = main;
    }
    
    public void getDevices() {
        GetDevices gd = new GetDevices();
        gd.start();
    }
    
    public void connectByFriendlyName(String deviceFriendlyName, String deviceType) {
        ConnectByFriendlyName cbfn = new ConnectByFriendlyName(deviceFriendlyName, deviceType);
        cbfn.start();
    }
    
    public void connect(String deviceAddress, String deviceType) {
        System.out.println("Trying connect with" + deviceAddress);
        Connect connect = new Connect(deviceAddress, deviceType);
        connect.start();
    }
    
    private class ConnectByFriendlyName extends Thread {
        private String deviceFriendlyName;
        private String deviceType;
        
        public ConnectByFriendlyName(String deviceFriendlyName, String deviceType) {
            this.deviceFriendlyName = deviceFriendlyName;
            this.deviceType = deviceType;
        }
        
        @Override
        public void run() {
            StreamConnection connection;
            if ((connection = btDevice.connectByFriendlyName(deviceFriendlyName)) != null) {               
                main.getMessageResource().startRead(connection, deviceType);
                main.connectionEstabilished();
            } 
        }  
    }
    
    private class Connect extends Thread {
        private String deviceAddress;
        private String deviceType;
        
        public Connect(String deviceAddress, String deviceType) {
            this.deviceAddress = deviceAddress;
            this.deviceType = deviceType;
        }
        
        @Override
        public void run() {
            StreamConnection connection;
            if ((connection = btDevice.connect(deviceAddress)) != null) {
                main.getMessageResource().startRead(connection, deviceType);
                main.connectionEstabilished();
            } 
        }
    }
    
    private class GetDevices extends Thread {
        @Override
        public void run() {
            btDevice.getDevices();
        }  
    }
}
