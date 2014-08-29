/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.dialogs;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.Config;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.bluetooth.RemoteDevice;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

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

    public Properties(Main aMain, boolean modal) {
        super(aMain.getFrame(), modal);
        main = aMain;
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(400, 250));
        setSize(new Dimension(400, 250));
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - getWidth()) / 2;
        final int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

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
                if (main.getBtdeviceResource().getDevices() == null
                        || main.getBtdeviceResource().getDevices().size() == 0) {
                    findDevices();
                } else {
                    selectionStart();
                }
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
        if (devAddr.getText() != null) {
            prop.setProperty("deviceAddress", devAddr.getText());
        }
        if (devName.getText() != null) {
            prop.setProperty("deviceName", devName.getText());
        }
        prop.setProperty("deviceType", "Polar Wear-Link");
        setLanguage(prop);

        Config.saveConfig(prop);
    }

    private void findDevices() {
        dial = new JDialog(this, true);
        dial.setLayout(new FlowLayout());
        dial.setPreferredSize(new Dimension(450, 150));
        dial.setSize(new Dimension(450, 150));
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - dial.getWidth()) / 2;
        final int y = (screenSize.height - dial.getHeight()) / 2;
        dial.setLocation(x, y);
        dial.setResizable(false);
        dial.setTitle(Config.labels.getString("Properties.searching"));

        final JProgressBar aJProgressBar = new JProgressBar();
        aJProgressBar.setIndeterminate(true);
        aJProgressBar.setPreferredSize(new Dimension(420, 60));
        dial.add(aJProgressBar);
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

    private void selectionStart() {
        final JDialog select = new JDialog(this, true);
        select.setLayout(new FlowLayout());
        select.setPreferredSize(new Dimension(450, 200));
        select.setSize(new Dimension(450, 200));
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - select.getWidth()) / 2;
        final int y = (screenSize.height - select.getHeight()) / 2;
        select.setLocation(x, y);
        //select.setResizable(false);
        select.setTitle(Config.labels.getString("Properties.selection"));

        List<RemoteDevice> devices = main.getBtdeviceResource().getDevices();
        DevicesModel model = new DevicesModel(devices);
        final JTable jtable = new JTable(model);
        jtable.getColumnModel().setColumnMargin(10);
        jtable.getColumnModel().getColumn(0).setMaxWidth(30);
        JScrollPane jscroll = new JScrollPane(jtable);
        jscroll.setPreferredSize(new Dimension(440, 100));
        select.add(jscroll);

        JButton setDev = new JButton(Config.labels.getString("Properties.deviceSelect"));
        setDev.setPreferredSize(new Dimension(150, 30));
        setDev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = jtable.getSelectedRow();
                devName.setText((String) jtable.getValueAt(row, 1));
                devAddr.setText((String) jtable.getValueAt(row, 2));
                select.dispose();
            }
        });
        select.add(setDev);

        JButton findtDev = new JButton(Config.labels.getString("Properties.deviceFindAgain"));
        findtDev.setPreferredSize(new Dimension(150, 30));
        findtDev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select.dispose();
                findDevices();
            }
        });
        select.add(findtDev);

        JButton cancel = new JButton(Config.labels.getString("Properties.cancel"));
        cancel.setPreferredSize(new Dimension(100, 30));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select.dispose();
            }
        });
        select.add(cancel);


        //select.pack();
        select.setVisible(true);
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
            while (work) {
                if (!main.getBtdeviceResource().isFindDevicesActive()
                        && !main.getBtdeviceResource().isInquryStarted()) {
                    break;
                }
            }
            System.out.println("Found " + main.getBtdeviceResource().getDevices().size());
            dial.dispose();
            if (work) {
                selectionStart();
            }
        }

        public void stopListen() {
            work = false;
        }
    }




    private class DevicesModel extends AbstractTableModel
                                implements TableModel {
        private List<RemoteDevice> devices;

        public DevicesModel(List<RemoteDevice> aDevices) {
            super();
            devices = aDevices;
        }

        @Override
        public int getRowCount() {
            return devices.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
                RemoteDevice device = devices.get(rowIndex);
            Object value = null;
            switch(columnIndex) {
                case 0:
                    value = rowIndex + 1;
                    break;
                case 1:
                    try {
                        value = device.getFriendlyName(false);
                    } catch (IOException ex) {
                        value = "Not available";
                    }
                    break;
                case 2:
                    value = device.getBluetoothAddress();
                default:
                    break;
            }
            return value;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public String getColumnName(int columnIndex) {
            String[] columns = new String[]{"Lp", "Device name", "Device address"};
            return columns[columnIndex];
        }
    }
}
