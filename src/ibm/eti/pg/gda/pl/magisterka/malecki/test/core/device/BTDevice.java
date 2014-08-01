/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author SebaTab
 */
public class BTDevice {
    private ArrayList<RemoteDevice> devices;
    private DiscoveryListener listener;
    private final Object inquiryCompletedEvent = new Object();
    private String status="";
      
    public ArrayList<RemoteDevice> getDevices() {
        devices = new ArrayList<RemoteDevice>();  
        
        listener = new DiscoveryListener() {
            @Override
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
                devices.add(btDevice);
                try {
                    System.out.println("     name " + btDevice.getFriendlyName(false));
                } catch (IOException cantGetDeviceName) {
                }
            }
            
            @Override
            public void inquiryCompleted(int discType) {
                System.out.println("Device Inquiry completed!");
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }

            @Override
            public void serviceSearchCompleted(int transID, int respCode) {
            }

            @Override
            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        };
        
        synchronized(inquiryCompletedEvent) {
            try {
                boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().
                        startInquiry(DiscoveryAgent.GIAC, listener);
                if (started) {
                    System.out.println("wait for device inquiry to complete...");
                    inquiryCompletedEvent.wait();
                    System.out.println(devices.size() +  " device(s) found");
                }
            } catch (InterruptedException | BluetoothStateException ex) {
                Logger.getLogger(BTDevice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return devices;
    }
    
    public StreamConnection connectByFriendlyName(String deviceFriendlyName) {
        if (devices == null) {
            getDevices();
        }
        for (RemoteDevice device : devices) {
            String name;
            try {
                name = device.getFriendlyName(false);
            } catch (Exception e) {
                name = device.getBluetoothAddress();
            }

            if (name.equals(deviceFriendlyName)) {
                System.out.println("Trying connect witch " + device.getBluetoothAddress() +"\n");
                return connect(device.getBluetoothAddress());
            }
        }
        System.out.println("Device " + deviceFriendlyName + " not found");
        return null;            
    }
    
    public StreamConnection connect(String deviceAddress) {
        //Polar iWL add: 0022D000F0A7
 
        StreamConnection connection;
        try {
            System.out.println("Started");
            connection = (StreamConnection) Connector.open("btspp://" +  
                    deviceAddress + ":1;authenticate=false;encrypt=false;master=true");
        } catch (Exception e) { 
            System.out.println("Błąd połączenia z urządzeniem");
            return null;
        }
        
        return connection;
    }
}
