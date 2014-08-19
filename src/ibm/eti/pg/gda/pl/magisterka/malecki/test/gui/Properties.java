/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

/**
 *
 * @author SebaTab
 */
public class Properties extends JDialog {

    private JLabel labLanguage = setLabel("language");
    private JLabel labDeviceName = setLabel("deviceName");
    private JLabel labDeviceAddress = setLabel("deviceAddress");
    private JLabel labDeviceType = setLabel("deviceType");
    private final JLabel devAddr;
    private final JLabel devName;
    private final JLabel devType;
    private final JComboBox langList;
    private final Main main;
    private JDialog dial;
    private DeviceSearchingListener devList;

    public Properties(Main aMain, boolean modal){
        super(aMain.getFrame(), modal);
        main = aMain;
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(400, 250));
        setResizable(false);

        add(labLanguage);

        langList = new JComboBox(
                Config.labels.getString("Properties.languageList").split(","));
        langList.setPreferredSize(new Dimension(200, 30));
        langList.setSelectedIndex(setLanguageList());
        add(langList);

        add(labDeviceType);
        devType = new JLabel(Config.DEVICE_TYPE);
        devType.setPreferredSize(new Dimension(200, 30));
        add(devType);

        add(labDeviceName);
        devName = new JLabel(Config.DEVICE_NAME);
        devName.setPreferredSize(new Dimension(200, 30));
        add(devName);

        add(labDeviceAddress);
        devAddr = new JLabel(Config.DEVICE_ADDRESS);
        devAddr.setPreferredSize(new Dimension(200, 30));
        add(devAddr);

        JLabel freeSpace = new JLabel("");
        freeSpace.setPreferredSize(new Dimension(200, 30));
        add(freeSpace);

        JButton setDev = new JButton(Config.labels.getString("Properties.deviceSelect"));
        setDev.setPreferredSize(new Dimension(150, 30));
        setDev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDevices();
            }
        });
        add(setDev);

        JButton cancel = new JButton(Config.labels.getString("Properties.cancel"));
        cancel.setPreferredSize(new Dimension(100, 30));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(cancel);

        JButton save = new JButton(Config.labels.getString("Properties.save"));
        save.setPreferredSize(new Dimension(100, 30));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProperties();
                dispose();
            }
        });
        add(save);

        pack();
        setVisible(true);
    }

    private JLabel setLabel(String txt) {
        JLabel lab = new JLabel(Config.labels.getString("Properties." + txt));
        lab.setPreferredSize(new Dimension(150, 30));
        return lab;
    }

    private int setLanguageList() {
        int result = 0;

        if (Config.LOCALE.getLanguage().equalsIgnoreCase("en")
                && Config.LOCALE.getCountry().equalsIgnoreCase("US")) {
            result = 0;
        } else if (Config.LOCALE.getLanguage().equalsIgnoreCase("pl")
                 && Config.LOCALE.getCountry().equalsIgnoreCase("PL")) {
            result = 1;
        }
        return result;
    }

    private void setLanguage(java.util.Properties prop) {
        if (langList.getSelectedIndex() == 0) {
            prop.setProperty("localeLanguage", "en");
            prop.setProperty("localeCountry", "US");
        } else if (langList.getSelectedIndex() == 1) {
            prop.setProperty("localeLanguage", "pl");
            prop.setProperty("localeCountry", "PL");
        }
    }

    private void saveProperties() {
        java.util.Properties prop = new java.util.Properties();

        prop.setProperty("deviceAddress", "0022D000F0A7");
        prop.setProperty("deviceName", "Polar iWL");
        prop.setProperty("deviceType", "Polar Wear-Link");
        setLanguage(prop);

        Config.saveConfig(prop);
    }

    private void getDevices() {
        final JProgressBar aJProgressBar = new JProgressBar();
        aJProgressBar.setIndeterminate(true);
        dial = new JDialog(this, true);
        dial.setTitle("Please wait. Searching BT devices....");
        //aJProgressBar.setMaximumSize(new Dimension(300, 200));
        aJProgressBar.setPreferredSize(new Dimension(450, 50));
        //dial.add(new JLabel("Please wait. Searching BT devices...."), BorderLayout.NORTH);
        dial.add(aJProgressBar, BorderLayout.SOUTH);
        dial.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        JButton cancel = new JButton(Config.labels.getString("Properties.cancel"));
        cancel.setPreferredSize(new Dimension(100, 30));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findFinshed();
            }
        });
        dial.add(cancel);
        dial.pack();

        main.getBtdeviceResource().findDevices();

        devList = new DeviceSearchingListener();
        devList.setName("DeviceSearchingLitener Thread");
        devList.start();

        dial.setVisible(true);
    }

    private void findFinshed() {
        //System.out.println("Found " + main.getBtdeviceResource().getDevices().size());
        //main.getBtdeviceResource().stopFindDevices();
        devList.stopListen();
        dial.dispose();
    }

    private class DeviceSearchingListener extends Thread {
        private boolean work = true;
        @Override
        public void run() {
            while(work) {
                if (!main.getBtdeviceResource().isFindDevicesActive()
                        && !main.getBtdeviceResource().isInquryStarted()) {
                    //findFinshed();
                    break;
                }
            }
            System.out.println("Found " + main.getBtdeviceResource().getDevices().size());
            dial.dispose();
        }

        public void stopListen() {
            work = false;
        }
    }





}
