/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.api;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.device.BTDevice;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;
import java.util.List;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author SebaTab
 */
public class BTDeviceResource {

    private BTDevice btDevice = new BTDevice();
    private final Main main;
    private GetDevices gdev;

    public BTDeviceResource(Main aMain) {
        this.main = aMain;
    }

    public void findDevices() {
        gdev = new GetDevices();
        gdev.setName("GetDevices Thread");
        gdev.start();
    }

    public boolean isFindDevicesActive() {
        return gdev.isAlive();
    }

    public boolean isInquryStarted() {
        return btDevice.isInquiryStarted();
    }

    public void stopFindDevices() {
        btDevice.stopFindDevices();
    }

    public List<RemoteDevice> getDevices() {
        return btDevice.getDevices();
    }

    public void connectByFriendlyName(String deviceName,
                                      String deviceType) {
        ConnectByFriendlyName cbfn =
                new ConnectByFriendlyName(deviceName, deviceType);
        cbfn.setName("Connection Thread");
        cbfn.start();
    }

    public void connect(String deviceAddress, String deviceType) {
        System.out.println("Trying connect with" + deviceAddress);
        Connect connect = new Connect(deviceAddress, deviceType);
        connect.setName("Connection Thread");
        connect.start();
    }

    private class ConnectByFriendlyName extends Thread {
        private String deviceName;
        private String deviceType;

        public ConnectByFriendlyName(String aDeviceName,
                                     String aDeviceType) {
            super();
            this.deviceName = aDeviceName;
            this.deviceType = aDeviceType;

        }

        @Override
        public void run() {
            StreamConnection connection =
                    btDevice.connectByName(deviceName);
            if (connection == null) {
                connectedFail();
            } else {
                connected(connection, deviceType);
            }
        }
    }

    private class Connect extends Thread {
        private String deviceAddress;
        private String deviceType;

        public Connect() {
            super();
        }

        public Connect(String aDeviceAddress, String aDeviceType) {
            super();
            this.deviceAddress = aDeviceAddress;
            this.deviceType = aDeviceType;
        }

        @Override
        public void run() {
            StreamConnection connection = btDevice.connect(deviceAddress);
            if (connection == null) {
                connectedFail();
            } else {
                connected(connection, deviceType);
            }
        }
    }

    private class GetDevices extends Thread {
        @Override
        public void run() {
            btDevice.findDevices();
        }
    }

    private void connected(StreamConnection connection, String deviceType) {
        main.getMessageResource().startRead(connection, deviceType);
        main.connectionEstabilished();
    }

    private void connectedFail() {
        main.connectionFailure();
    }
}
